package timofey.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.xml.sax.SAXException;
import timofey.config.SourceInit;
import timofey.db.services.NewsArticleServiceImpl;
import timofey.entities.NewsArticle;
import timofey.keyboard.TopicsKeyboard;
import timofey.utils.enums.Commands;
import timofey.utils.enums.Sources;
import timofey.xmlParser.AbstractParserFactory;
import timofey.xmlParser.Parser;
import timofey.xmlParser.ParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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
    private List<SendMessage> messageList;
    private static final int MAX_MESSAGE_SIZE = 4096;
    private Stack<InlineKeyboardMarkup> keyboardHistory;

    public CallBackQueryHandler(
            @Qualifier("defaultMenuKeyboard") InlineKeyboardMarkup defaultKeyboard,
            @Qualifier("subscriptionMenu")InlineKeyboardMarkup subMenu,
            @Qualifier("optionCnbcChoice") InlineKeyboardMarkup optionCnbcChoice,
            @Qualifier("optionReutersChoice") InlineKeyboardMarkup optionReutersChoice,
            @Qualifier("sourceMultipleChoice") InlineKeyboardMarkup sourceMultipleChoice,
            @Qualifier("cnbcTopicsKeyboard") InlineKeyboardMarkup cnbcTopicsKeyboard,
            @Qualifier("reutersTopicsKeyboard") InlineKeyboardMarkup reutersTopicsKeyboard,
            SourceInit sourceInit,
            NewsArticleServiceImpl newsArticleService
            ) {

        this.keyboardHistory = new Stack<>();
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


    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public List<SendMessage> getReplyMessage() throws ParserConfigurationException, SAXException, IOException {
        if(keyboardHistory.size() == 0) keyboardHistory.add(defaultKeyboard);

        messageList = new ArrayList<>();
        Long userChatId = callbackQuery.getFrom().getId();
        String userMessage = callbackQuery.getData();
        replyMessage.setChatId(userChatId);
        if(!userMessage.isEmpty()){
            if (Arrays.stream(Sources.values()).map(x->x.name().toLowerCase()).collect(Collectors.toList()).contains(userMessage.toLowerCase())){
//                Map <String,String> filterdMap = new HashMap<>();
//                for (Map.Entry<String, String> entry : rssResources.getResourceMap().entrySet()) {
//                    if (entry.getKey().toLowerCase().contains(userMessage.toLowerCase())) {
//                        filterdMap.put(entry.getKey(), entry.getValue());
//                    }
//                }
//                TopicsKeyboard topicsKeyboard = new TopicsKeyboard(filterdMap);
                if(userMessage.equals(Sources.CNBC.name())){
                    replyMessage.setReplyMarkup(cnbcTopicsKeyboard);
                    keyboardHistory.add(cnbcTopicsKeyboard);
                }
                else if(userMessage.equals(Sources.Reuters.name())){
                    replyMessage.setReplyMarkup(reutersTopicsKeyboard);
                    keyboardHistory.add(reutersTopicsKeyboard);
                }
                else replyMessage.setReplyMarkup(defaultKeyboard);
                replyMessage.setText("Выберите тему новостей");
                messageList.add(replyMessage);
            }
            else if (rssResources.getResourceMap().containsKey(userMessage)){
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
                            replyMessage.setChatId(userChatId);
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
                        replyMessage.setReplyMarkup(defaultKeyboard);
                        messageList.add(replyMessage);
                    }

                }
            }

            else if (userMessage.equals(Commands.subscription.name())) {
                replyMessage.setReplyMarkup(subMenu);
                replyMessage.setText("Параметры подписки");
                keyboardHistory.add(subMenu);

                messageList.add(replyMessage);
            }
            else if(userMessage.equals(Commands.certainSourceSub.name())){
                replyMessage.setReplyMarkup(sourceMultipleChoice);
                replyMessage.setText("Выбор ресурса для подписки");
                keyboardHistory.add(sourceMultipleChoice);

                messageList.add(replyMessage);

            }
            else if(userMessage.equals(Commands.cnbcSub.name())){
                replyMessage.setReplyMarkup(optionCnbcChoice);
                replyMessage.setText("Выберите темы");
                keyboardHistory.add(optionCnbcChoice);

                messageList.add(replyMessage);
            }
            else if (userMessage.equals(Commands.reutersSub.name())) {
                replyMessage.setReplyMarkup(optionReutersChoice);
                replyMessage.setText("Выберите темы");
                keyboardHistory.add(optionReutersChoice);

                messageList.add(replyMessage);
            }
            else if(userMessage.equals("back")) {
                InlineKeyboardMarkup previousKeyboard = back2previousKeyboard();
                replyMessage.setReplyMarkup(previousKeyboard);
                messageList.add(replyMessage);
                System.out.println(keyboardHistory.toString());
            }

        }
        return messageList;
    }

    private InlineKeyboardMarkup back2previousKeyboard() {
        if(keyboardHistory.size() > 1){
            keyboardHistory.pop();
            return keyboardHistory.peek();
        }
        else return defaultKeyboard;
    }

}
