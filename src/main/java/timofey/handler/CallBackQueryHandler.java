package timofey.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.xml.sax.SAXException;
import timofey.config.SourceInit;
import timofey.db.services.NewsArticleServiceImpl;
import timofey.db.services.UserServiceImpl;
import timofey.entities.NewsArticle;
import timofey.entities.User;
import timofey.meta.usersMeta.KeyboardMeta;
import timofey.meta.usersMeta.SubscribedResources;
import timofey.utils.enums.Commands;
import timofey.utils.enums.Sources;
import timofey.xmlParser.AbstractParserFactory;
import timofey.xmlParser.Parser;
import timofey.xmlParser.ParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * класс для обработки callback сообщений
 * Есть Callback, а есть просто текст
 *
 */

@Component
public class CallBackQueryHandler {
    private CallbackQuery callbackQuery;
    private SendMessage replyMessage;
    private InlineKeyboardMarkup defaultKeyboard;
    private InlineKeyboardMarkup subMenu;
    private InlineKeyboardMarkup optionCnbcChoice;
    private InlineKeyboardMarkup optionReutersChoice;
    private InlineKeyboardMarkup sourceMultipleChoice;
    private InlineKeyboardMarkup cnbcTopicsKeyboard;
    private InlineKeyboardMarkup reutersTopicsKeyboard;
    private SourceInit rssResources;
    private NewsArticleServiceImpl newsArticleService;
    private UserServiceImpl userService;
    private SubscribedResources subscribedResources;
    private List<SendMessage> messageList;
    private static final int MAX_MESSAGE_SIZE = 4096;
    private KeyboardMeta keyboardMeta;

    public CallBackQueryHandler(
            @Qualifier("defaultMenuKeyboard") InlineKeyboardMarkup defaultKeyboard,
            @Qualifier("subscriptionMenu")InlineKeyboardMarkup subMenu,
            @Qualifier("optionCnbcChoice") InlineKeyboardMarkup optionCnbcChoice,
            @Qualifier("optionReutersChoice") InlineKeyboardMarkup optionReutersChoice,
            @Qualifier("sourceMultipleChoice") InlineKeyboardMarkup sourceMultipleChoice,
            @Qualifier("cnbcTopicsKeyboard") InlineKeyboardMarkup cnbcTopicsKeyboard,
            @Qualifier("reutersTopicsKeyboard") InlineKeyboardMarkup reutersTopicsKeyboard,
            KeyboardMeta keyboardMeta,
            SourceInit sourceInit,
            NewsArticleServiceImpl newsArticleService,
            UserServiceImpl userService,
            SubscribedResources subscribedResources
            ) {

        this.replyMessage = new SendMessage();

        this.defaultKeyboard = defaultKeyboard;
        this.subMenu = subMenu;
        this.optionCnbcChoice = optionCnbcChoice;
        this.optionReutersChoice = optionReutersChoice;
        this.sourceMultipleChoice = sourceMultipleChoice;
        this.cnbcTopicsKeyboard = cnbcTopicsKeyboard;
        this.reutersTopicsKeyboard = reutersTopicsKeyboard;
        this.rssResources = sourceInit;
        this.newsArticleService = newsArticleService;
        this.userService = userService;
        this.subscribedResources = subscribedResources;
        this.keyboardMeta = keyboardMeta;
    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public List<SendMessage> getReplyMessage() throws ParserConfigurationException, SAXException, IOException {

        messageList = new ArrayList<>();
        Long chatId = callbackQuery.getMessage().getChatId();
        User user = userService.findByChatId(chatId);
        if (user == null) {
            Long userId = callbackQuery.getFrom().getId();
            String userName = callbackQuery.getFrom().getUserName();
            user = new User()
                    .setTelegramUserID(userId)
                    .setChatId(chatId)
                    .setUserName(userName);
            userService.save(user);
        }
        String userMessage = callbackQuery.getData();
        replyMessage.setChatId(chatId);
        Stack<InlineKeyboardMarkup> usersKeyboards = keyboardMeta.getKeyboardByUserId(chatId);
        if(usersKeyboards.size() == 0) usersKeyboards.add(defaultKeyboard);

        if(!userMessage.isEmpty()){
            //идет проверка на тип сообщения, переделать на switch case для удобного понимания
            if (Arrays.stream(Sources.values()).map(x->x.name().toLowerCase()).toList().contains(userMessage.toLowerCase()))
                selectCertainSource(userMessage, usersKeyboards);

            else if (rssResources.getResourceMap().containsKey(userMessage))
                selectTopic(chatId, userMessage, usersKeyboards);

            else if (userMessage.equals(Commands.subscription.name()))
                goIntoMenuSection(subMenu, "Параметры подписки", usersKeyboards);

            else if(userMessage.equals(Commands.certainSourceSub.name()))
                goIntoMenuSection(sourceMultipleChoice, "Выбор ресурса для подписки", usersKeyboards);

            else if(userMessage.equals(Commands.cnbcSub.name()))
                goIntoTopicSubscriptionMenu(chatId, optionCnbcChoice, usersKeyboards);

            else if (userMessage.equals(Commands.reutersSub.name()))
                goIntoTopicSubscriptionMenu(chatId, optionReutersChoice, usersKeyboards);

            else if(userMessage.equals("back")) {
                goBack2previousKeyboard(chatId);
            }
            else if (usersKeyboards.lastElement().equals(optionCnbcChoice) ||
                    usersKeyboards.lastElement().equals(optionReutersChoice)) {
                subscribeToCertainSourceTopic(chatId, userMessage, usersKeyboards);
            }

        }
        keyboardMeta.updateMeta(chatId, usersKeyboards);
        return messageList;
    }

    private void subscribeToCertainSourceTopic(Long chatId, String userMessage, Stack<InlineKeyboardMarkup> usersKeyboards) {
        String sourceType;
        if (usersKeyboards.lastElement().equals(optionReutersChoice))
            sourceType = "reuters";
        else
            sourceType = "cnbc";
        String fullTopicName = sourceType + "." + userMessage;
        if (rssResources.getResourceMap().containsKey(fullTopicName)) {
            List<String> userResources = subscribedResources.getUserResourcesNamesByChatId(chatId);
            if (userResources.contains(fullTopicName)) {
                subscribedResources.unsubscribeUserFromResourceByChatIdAndResourceName(chatId, fullTopicName);
                makeButtonDeselected(userMessage, usersKeyboards.lastElement().getKeyboard());
            }
            else {
                subscribedResources.subscribeUserToResourceByChatIdAndResourceName(chatId, fullTopicName);
                makeButtonSelected(userMessage, usersKeyboards.lastElement().getKeyboard());
            }
            messageList.add(replyMessage);
        }
    }

    private void selectTopic(Long chatId, String userMessage, Stack<InlineKeyboardMarkup> usersKeyboards) {
        for (String key : rssResources.getResourceMap().keySet()) {
            String topic = key.split("\\.")[1];
            if(userMessage.equals(key)){

                ParserFactory factory = null;
                String sourceType = key.split("\\.")[0];
                if(sourceType.equals(Sources.Reuters.name().toLowerCase()))
                    factory =  AbstractParserFactory.initParserFactory(Sources.Reuters);
                else if (sourceType.equals(Sources.CNBC.name().toLowerCase()))
                    factory = AbstractParserFactory.initParserFactory(Sources.CNBC);

                Parser parser = factory.createFactory();
                List<NewsArticle> list = parser.parse(rssResources.getResourceMap().get(key));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++){
                    NewsArticle article = list.get(i);
                    article.setTopic(topic);
                    replyMessage = new SendMessage();
                    replyMessage.setChatId(chatId);
                    if(sb.toString().length() + article.toString().length() <= MAX_MESSAGE_SIZE ){
                        sb.append(article.toString());
                    }
                    else {
                        replyMessage.setText(sb.toString());
                        replyMessage.setReplyMarkup(null);
                        messageList.add(replyMessage);
                        sb = new StringBuilder();
                    }
                }
                replyMessage.setText(sb.toString());
                replyMessage.setReplyMarkup(usersKeyboards.peek());
                messageList.add(replyMessage);
            }

        }
    }

    private void goIntoTopicSubscriptionMenu(Long chatId,
                                             InlineKeyboardMarkup menuMarkup,
                                             Stack<InlineKeyboardMarkup> usersKeyboards
    ) {
        List<String> userResources = subscribedResources.getUserResourcesNamesByChatId(chatId);
        for (String resource : userResources) {
            String buttonText = resource.split("\\.")[1];
            makeButtonSelected(buttonText, menuMarkup.getKeyboard());
        }
        goIntoMenuSection(menuMarkup, "Выберите темы", usersKeyboards);
    }

    private void goIntoMenuSection(InlineKeyboardMarkup menuMarkup,
                                   String replyText,
                                   Stack<InlineKeyboardMarkup> usersKeyboards
    ) {
        replyMessage.setReplyMarkup(menuMarkup);
        replyMessage.setText(replyText);
        usersKeyboards.add(menuMarkup);

        messageList.add(replyMessage);
    }

    private void selectCertainSource(String userMessage, Stack<InlineKeyboardMarkup> usersKeyboards) {
        if(userMessage.equals(Sources.CNBC.name()))
            goIntoMenuSection(cnbcTopicsKeyboard, "Выберите тему новостей", usersKeyboards);
        else if(userMessage.equals(Sources.Reuters.name()))
            goIntoMenuSection(reutersTopicsKeyboard, "Выберите тему новостей", usersKeyboards);
        else {
            replyMessage.setReplyMarkup(defaultKeyboard);
            messageList.add(replyMessage);
        }
    }

    private void goBack2previousKeyboard(Long chatId) {
        InlineKeyboardMarkup previousKeyboard;
        Stack<InlineKeyboardMarkup> history = keyboardMeta.getKeyboardByUserId(chatId);
        if(history != null && history.size() > 1){
            history.pop();
            previousKeyboard = history.peek();
        }
        else
            previousKeyboard = defaultKeyboard;

        replyMessage.setReplyMarkup(previousKeyboard);
        messageList.add(replyMessage);
    }

    private void makeButtonSelected(String buttonText, List<List<InlineKeyboardButton>> keyboardMarkup) {
        String selectedButtonText = "✔️ " + buttonText;
        changeButtonText(buttonText, selectedButtonText, keyboardMarkup);
    }

    private void makeButtonDeselected(String buttonText, List<List<InlineKeyboardButton>> keyboardMarkup) {
        String selectedButtonText = "✔️ " + buttonText;
        changeButtonText(selectedButtonText, buttonText, keyboardMarkup);
    }

    private void changeButtonText(String oldButtonText, String newButtonText, List<List<InlineKeyboardButton>> keyboardMarkup) {
        for (List<InlineKeyboardButton> row : keyboardMarkup) {
            for (InlineKeyboardButton button : row) {
                if (button.getText().equals(oldButtonText)) {
                    button.setText(newButtonText);
                }
            }
        }
    }

}
