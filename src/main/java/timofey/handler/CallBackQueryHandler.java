package timofey.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class CallBackQueryHandler {
    private CallbackQuery callbackQuery;
    private SendMessage replyMessage;
    @Autowired
    ReplyKeyboardMarkup replyKeyboardMarkup;

    public CallBackQueryHandler() {
        this.replyMessage = new SendMessage();
    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public SendMessage getReplyMessage() {

        Long id = callbackQuery.getFrom().getId();
        replyMessage.setChatId(id);
        replyMessage.setReplyMarkup(replyKeyboardMarkup);
        replyMessage.setText("Вы выбрали новостоной источник X");
        return replyMessage;
    }
}
