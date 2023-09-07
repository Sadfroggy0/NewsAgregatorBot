package timofey.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.xml.sax.SAXException;
import timofey.config.SourceInit;
import timofey.db.services.NewsArticleServiceImpl;
import timofey.entities.NewsArticle;
import timofey.keyboard.TopicsKeyboard;
import timofey.utils.enums.Commands;
import timofey.utils.enums.Resources;
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
    @Autowired
    @Qualifier("defaultMenuKeyboard")
    InlineKeyboardMarkup defaultKeyboard;
    @Autowired
    @Qualifier("subscriptionMenu")
    InlineKeyboardMarkup subMenu;

    @Autowired
    @Qualifier("optionCnbcChoice")
    InlineKeyboardMarkup optionCnbcChoice;

    @Autowired
    @Qualifier("sourceMultipleChoice")
    InlineKeyboardMarkup sourceMultipleChoice;
    @Autowired
    SourceInit rssResources;
    @Autowired
    NewsArticleServiceImpl newsArticleService;
    private List<SendMessage> messageList;
    private static final int MAX_MESSAGE_SIZE = 4096;


    public CallBackQueryHandler() {
        this.replyMessage = new SendMessage();

    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public List<SendMessage> getReplyMessage() throws ParserConfigurationException, SAXException, IOException {
        messageList = new ArrayList<>();

        Long userChatId = callbackQuery.getFrom().getId();
        String userMessage = callbackQuery.getData();
        replyMessage.setChatId(userChatId);
        if(!userMessage.isEmpty()){
            if (Arrays.stream(Resources.values()).map(x->x.name().toLowerCase()).collect(Collectors.toList()).contains(userMessage.toLowerCase())){
                Map <String,String> filterdMap = new HashMap<>();
                for (Map.Entry<String, String> entry : rssResources.getResourceMap().entrySet()) {
                    if (entry.getKey().toLowerCase().contains(userMessage.toLowerCase())) {
                        filterdMap.put(entry.getKey(), entry.getValue());
                    }
                }
                TopicsKeyboard topicsKeyboard = new TopicsKeyboard(filterdMap);
                replyMessage.setText("Выберите тему новостей");
                replyMessage.setReplyMarkup(topicsKeyboard.getTopicKeyboard());
                messageList.add(replyMessage);
            }
            else if (rssResources.getResourceMap().containsKey(userMessage)){
                for (String key : rssResources.getResourceMap().keySet()) {
                    String topic = key.split("\\.")[1];
                    if(userMessage.equals(key)){

                        ParserFactory factory = null;
                        String sourceType = key.split("\\.")[0];
                        if(sourceType.equals(Resources.Reuters.name().toLowerCase()))
                            factory =  AbstractParserFactory.initParserFactory(Resources.Reuters);
                        else if (sourceType.equals(Resources.CNBC.name().toLowerCase()))
                            factory = AbstractParserFactory.initParserFactory(Resources.CNBC);

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
                messageList.add(replyMessage);
            }
            else if(userMessage.equals(Commands.certainSourceSub.name())){
                replyMessage.setReplyMarkup(sourceMultipleChoice);
                replyMessage.setText("Выбор ресурса для подписки");
                messageList.add(replyMessage);

            }
            else if(userMessage.equals(Commands.cnbcSub.name())){
                replyMessage.setReplyMarkup(optionCnbcChoice);
                replyMessage.setText("Выбирите темы");
                messageList.add(replyMessage);

            }
            else if (userMessage.equals(Commands.reutersSub)) {

            }

        }
        return messageList;
    }


}
