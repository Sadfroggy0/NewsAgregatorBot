package timofey.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.xml.sax.SAXException;
import timofey.config.SourceInit;
import timofey.entities.NewsArticle;
import timofey.keyboard.TopicsKeyboard;
import timofey.utils.Resources;
import timofey.xmlParser.XMLParser;
import timofey.xmlParser.XmlParserCnbcTemplate;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class CallBackQueryHandler {
    private CallbackQuery callbackQuery;
    private SendMessage replyMessage;
    @Autowired
    ReplyKeyboardMarkup replyKeyboardMarkup;
    @Autowired
    SourceInit rssResources;


    public CallBackQueryHandler() {
        this.replyMessage = new SendMessage();

    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public SendMessage getReplyMessage() throws ParserConfigurationException, SAXException, IOException {

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
            }
            else if (rssResources.getResourceMap().containsKey(userMessage)){
                for (String key : rssResources.getResourceMap().keySet()) {
                    if(userMessage.equals(key)){
                        StringBuilder sb = new StringBuilder();
                        sb.append(key + "\n");

                        XMLParser xmlParser = new XmlParserCnbcTemplate(rssResources.getResourceMap().get(key));
                        List<NewsArticle> list = xmlParser.parseXml();
                        for (int i = 0; i < list.size(); i++){
                            NewsArticle article = list.get(i);
                            sb.append(article.getTitle() + "\n" + article.getLink() + "\n" + article.getDescription() + "\n");
                        }
                        replyMessage.setText(sb.toString().trim());

                    }
                }
            }
        }
        return replyMessage;
    }
}
