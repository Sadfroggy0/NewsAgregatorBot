package timofey.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

//    @RabbitListener(queues = "queue1")
//    public String handleQueue1(String message) {
//        logger.info("Received from queue1: " + message);
//        return "received on queue handler and handled message: " + message;
//    }
    @RabbitListener(queues = "CNBC")
    public String handleCnbcQueue(String message) {
        logger.info("Received from CNBC: " + message);
        return "received on queue handler and handled message: " + message;
    }
    @RabbitListener(queues = "Reuters")
    public String handleReutersQueue(String message) {
        logger.info("Received from Reuters: " + message);
        return "received on queue handler and handled message: " + message;
    }
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
    public String handleSimpleMessageQueue(String message) {
        logger.info("Received from SimpleMessageQueue: " + message);
        return "received on queue handler and handled message: " + message;
    }






}
