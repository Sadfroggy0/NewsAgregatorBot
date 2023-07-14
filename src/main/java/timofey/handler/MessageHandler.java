package timofey.handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import timofey.utils.Commands;

import java.util.Arrays;
import java.util.stream.Collectors;


@Component
public class MessageHandler {
    private Long chatId;
    private String messageText;
    @Autowired
    @Qualifier("defaultMenuKeyboard")
    private InlineKeyboardMarkup defaultMenuKeyboard;
    private SendMessage replyMessage;


    public MessageHandler(){

        this.replyMessage = new SendMessage();
    }

    public SendMessage getSendMessage() {

        replyMessage.setChatId(chatId);

        //является ли сообщение командой
        if (Arrays.stream(Commands.values()).map(x -> x.getRawCommand()).collect(Collectors.joining()).contains(messageText)){
            if(messageText.equals(Commands.start.getRawCommand())) {
                replyMessage.setText("Выберите новостной источник:");
                replyMessage.setReplyMarkup(defaultMenuKeyboard);
                return replyMessage;

            } else if (messageText.equals(Commands.latest.getRawCommand())) {
                replyMessage.setText("Вы выбрали получение новстоей за сутки");
                replyMessage.setReplyMarkup(null);
                return replyMessage;
            }
        }
        else {
            replyMessage.setText("Неизвестная команда");
            replyMessage.setReplyMarkup(null);
        }

        return replyMessage;
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


}
