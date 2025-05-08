package timofey.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import timofey.config.TelegramConfig;
import timofey.handler.CallBackQueryHandler;
import timofey.handler.MessageHandler;

import java.util.List;

@EnableRabbit
@Component
public class RabbitMqListener {
    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    private MessageHandler messageHandler;
    private CallBackQueryHandler callBackQueryHandler;
    private TelegramConfig telegramConfig;

    public RabbitMqListener(
            TelegramConfig telegramConfig,
            MessageHandler messageHandler,
            CallBackQueryHandler callBackQueryHandler
    ){
        this.telegramConfig = telegramConfig;
        this.messageHandler = messageHandler;
        this.callBackQueryHandler = callBackQueryHandler;

    }

//    @RabbitListener(queues = "queue1")
//    public String handleQueue1(String message) {
//        logger.info("Received from queue1: " + message);
//        return "received on queue handler and handled message: " + message;
//    }
    @RabbitListener(queues = "CNBC")
    @RabbitListener(queues = "Reuters")
    public String handleCnbcQueue(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        callBackQueryHandler.setCallbackQuery(callback);
        List<SendMessage> sendMessageList = null;

            try {
                sendMessageList = callBackQueryHandler.getReplyMessage();
            }
            catch (Exception e ){
                e.printStackTrace();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String sendMessageJson = null;

        try {
            sendMessageJson = objectMapper.writeValueAsString(sendMessageList);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info("Received from CNBC: " + update);

        return sendMessageJson;
    }

//    @RabbitListener(queues = "Reuters")
//    public String handleReutersQueue(String message) {
//        logger.info("Received from Reuters: " + message);
//        return "received on queue handler and handled message: " + message;
//    }
    @RabbitListener(queues = "ReutersSubscription")
    public String handleReutersSubscriptionQueue(String message) {
        logger.info("Received from ReutersSubscription: " + message);
        return "received on queue handler and handled message: " + message;
    }
    @RabbitListener(queues = "CNBCSubscription")
    public String handleCNBCSubscriptionQueue(String message) {
        logger.info("Received from CNBCSubscription: " + message);
        return "received on queue handler and handled message: " + message;
    }
    @RabbitListener(queues = "SimpleMessageQueue")
    public String handleSimpleMessageQueue(Update update) {
        logger.info("Received from SimpleMessageQueue: " + update);

        messageHandler.setChatId(update.getMessage().getChatId());
        messageHandler.setMessage(update.getMessage());
        SendMessage sendMessage = messageHandler.getSendMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        String sendMessageJson = null;

        try {
            sendMessageJson = objectMapper.writeValueAsString(sendMessage);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return sendMessageJson;
    }

}
