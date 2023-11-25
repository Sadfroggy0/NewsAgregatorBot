//package timofey.config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.xml.sax.SAXException;
//import timofey.automailing.UpdatesChecker;
//import timofey.handler.CallBackQueryHandler;
//import timofey.handler.MessageHandler;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//import java.util.List;
//
///**
// * класс, откуда приложение отправляет сообщения, приходящие из обработчиков
// *
// */
//@Component
//public class BotConfig extends TelegramLongPollingBot  {
//
//    private MessageHandler messageHandler;
//    private CallBackQueryHandler callBackQueryHandler;
//    private TelegramConfig telegramConfig;
//
//
//
//    @Autowired
//    public BotConfig(
//            TelegramConfig telegramConfig,
//            MessageHandler messageHandler,
//            CallBackQueryHandler callBackQueryHandler
//    ) {
//        this.telegramConfig = telegramConfig;
//        this.messageHandler = messageHandler;
//        this.callBackQueryHandler = callBackQueryHandler;
//
//    }
//    @Override
//    public String getBotUsername() {
//        return telegramConfig.getBotName();
//    }
//    public String getBotToken(){
//        return telegramConfig.getBotToken();
//    }
//
//
//    @Override
//    public void onUpdateReceived( Update update) {
//
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            messageHandler.setChatId(update.getMessage().getChatId());
//            messageHandler.setMessage(update.getMessage());
//            try {
//                execute(messageHandler.getSendMessage());
//
//            } catch (TelegramApiException e) {
//
//            }
//        }
//        else if (update.hasCallbackQuery()) {
//            CallbackQuery callback = update.getCallbackQuery();
//            callBackQueryHandler.setCallbackQuery(callback);
//            try {
//                List<SendMessage> sendMessageList = callBackQueryHandler.getReplyMessage();
//                for (SendMessage message : sendMessageList
//                ) {
//                    InlineKeyboardMarkup temp = (InlineKeyboardMarkup) message.getReplyMarkup();
//                    /**TODO
//                    //такая ерунда для того, чтобы менять клавиатуру на предыдущем сообщении
//                    // чтобы не присылать новое сообщение с клавой, а редактировать старое
//                    //приходит несколько сообщений, потому что макс длина сообщения 4096 символов
//                     точно нужно переделать, выдает ошибку в логах при редактировании сообщения
//                     */
//                    if (message.getText().contains("http")){ // это условие для проверки, есть ли ссылка в ообщении
//                       if(message.getReplyMarkup() == null){
//                           message.setParseMode("Markdown");
//                           execute(message);
//                       }
//                       else {
//                           Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
//                           Long chatId = update.getCallbackQuery().getMessage().getChatId();
//                           // изменение сообщений
//                           EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                           editMessageReplyMarkup.setChatId(chatId);
//                           editMessageReplyMarkup.setMessageId(messageId);
//                           editMessageReplyMarkup.setReplyMarkup(temp);
//
//                           message.setParseMode("Markdown");
//                           execute(message);
//                           execute(editMessageReplyMarkup);
//                       }
//                    }
//                    else {
//                        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
//                        Long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//                        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                        editMessageReplyMarkup.setChatId(chatId);
//                        editMessageReplyMarkup.setMessageId(messageId);
//                        editMessageReplyMarkup.setReplyMarkup(temp);
//
//                        EditMessageText editMessageText = new EditMessageText();
//                        editMessageText.setMessageId(messageId);
//                        editMessageText.setChatId(chatId);
//                        editMessageText.setText(message.getText());
//
//                        editMessageText.setParseMode("Markdown");
//                        execute(editMessageText);
//                        execute(editMessageReplyMarkup);
//                    }
//                }
//            } catch (TelegramApiException | ParserConfigurationException | SAXException | IOException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//}