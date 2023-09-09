package timofey.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.xml.sax.SAXException;
import timofey.automailing.UpdatesChecker;
import timofey.handler.CallBackQueryHandler;
import timofey.handler.MessageHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Component
public class BotConfig extends TelegramLongPollingBot implements BotCustomInterface {

    private MessageHandler messageHandler;
    private CallBackQueryHandler callBackQueryHandler;
    private TelegramConfig telegramConfig;
    private UpdatesChecker checker;


    @Autowired
    public BotConfig(
            TelegramConfig telegramConfig,
            MessageHandler messageHandler,
            CallBackQueryHandler callBackQueryHandler,
            UpdatesChecker checker

    ) {
        this.telegramConfig = telegramConfig;
        this.messageHandler = messageHandler;
        this.callBackQueryHandler = callBackQueryHandler;
        this.checker = checker;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.setChatId(update.getMessage().getChatId());
            messageHandler.setMessage(update.getMessage());
            try {
                execute(messageHandler.getSendMessage());

            } catch (TelegramApiException e) {

            }
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            callBackQueryHandler.setCallbackQuery(callback);

//            update.getCallbackQuery().getMessage().getChatId();
//            update.getCallbackQuery().getMessage().getMessageId();
//            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//            editMessageReplyMarkup.setChatId(message.getChatId());

            try {
                List<SendMessage> sendMessageList = callBackQueryHandler.getReplyMessage();
                for (SendMessage message : sendMessageList
                ) {
                    message.setParseMode("Markdown");
                    execute(message);
                }

            } catch (TelegramApiException | ParserConfigurationException | SAXException | IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }
    @Override
    @Scheduled(fixedRate = 120000)
    public void scheduledMessage(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                SendMessage sendMessage = checker.check();
                sendMessage.setParseMode("Markdown");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }
}