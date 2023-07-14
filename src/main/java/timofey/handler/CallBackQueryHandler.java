package timofey.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import timofey.config.SourceInit;
import timofey.keyboard.TopicsKeyboard;
import timofey.utils.Resources;

import java.util.HashMap;
import java.util.Map;


@Component
public class CallBackQueryHandler {
    private CallbackQuery callbackQuery;
    private SendMessage replyMessage;
    @Autowired
    ReplyKeyboardMarkup replyKeyboardMarkup;
    @Autowired
    SourceInit resources;


    public CallBackQueryHandler() {
        this.replyMessage = new SendMessage();

    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public SendMessage getReplyMessage() {

        Long userChatId = callbackQuery.getFrom().getId();
        String userMessage = callbackQuery.getData();
        replyMessage.setChatId(userChatId);

        if(!userMessage.isEmpty()){

            Map <String,String> filterdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : resources.getResourceMap().entrySet()) {
                if (entry.getKey().toLowerCase().contains(userMessage.toLowerCase())) {
                    filterdMap.put(entry.getKey(), entry.getValue());
                }
            }
            TopicsKeyboard topicsKeyboard = new TopicsKeyboard(filterdMap);
            replyMessage.setReplyMarkup(topicsKeyboard.getTopicKeyboard());

            if(userMessage.equals(Resources.CNBC.name())){
                replyMessage.setText("Вы выбрали новостоной источник CNBC");
            }
            else if(userMessage.equals(Resources.Reuters.name())){
                replyMessage.setText("Вы выбрали новостоной источник Reuters");

            }
            else if(userMessage.equals(Resources.RBK.name())){
                replyMessage.setText("Вы выбрали новостной источник RBK");
            }
            else {
                replyMessage.setText("Ошибка выбора источника");
                replyMessage.setReplyMarkup(null);
            }

        }
        return replyMessage;
    }
}
