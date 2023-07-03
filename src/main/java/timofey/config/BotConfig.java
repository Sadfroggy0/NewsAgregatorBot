package timofey.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotConfig extends TelegramLongPollingBot {

    private TelegramConfig telegramConfig;
    @Qualifier("defaultMenuKeyboard")
    InlineKeyboardMarkup defaultMenuKeyboard;


    @Autowired
    public BotConfig(
            TelegramConfig telegramConfig,
            InlineKeyboardMarkup defaultMenuKeyboard
    ) {
        this.telegramConfig = telegramConfig;
        this.defaultMenuKeyboard = defaultMenuKeyboard;

    }
    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }
    public String getBotToken(){
        return telegramConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived( Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText("Some response");
            sendMessage.setReplyMarkup(defaultMenuKeyboard);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {

            }
        }
        else if(update.hasCallbackQuery()){
            System.out.println(update.getCallbackQuery().getData());
        }

    }
}