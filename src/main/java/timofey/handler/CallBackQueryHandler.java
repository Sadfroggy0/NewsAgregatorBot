package timofey.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.xml.sax.SAXException;
import timofey.config.SourceInit;
import timofey.db.services.NewsArticleServiceImpl;
import timofey.entities.NewsArticle;
import timofey.keyboard.TopicsKeyboard;
import timofey.utils.Resources;
import timofey.xmlParser.XMLParser;
import timofey.xmlParser.XmlParserCnbcTemplate;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class CallBackQueryHandler {
    private CallbackQuery callbackQuery;
    private SendMessage replyMessage;
    @Autowired
    InlineKeyboardMarkup defaultKeyboard;
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

                        XMLParser xmlParser = new XmlParserCnbcTemplate(rssResources.getResourceMap().get(key));
                        List<NewsArticle> list = xmlParser.parseXml();
                        list.remove(0);
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
        }
        return messageList;
    }


}
