package timofey.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import timofey.config.TelegramConfig;
import timofey.handler.CallBackQueryHandler;
import timofey.handler.MessageHandler;


@Configuration
@PropertySource("classpath:config/application.properties")
public class RabbitConfiguration {
    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);
    @Value("${rabbitmq.server}")
    private String rabbitmqServer;
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(rabbitmqServer);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    //объявляем очередь с именем queue1
    @Bean
    public Queue myQueue1() {
        return new Queue("queue1");
    }
    @Bean
    public RabbitMqListener getMqListener(TelegramConfig telegramConfig,
                                            MessageHandler messageHandler,
                                            CallBackQueryHandler callBackQueryHandler){
        return  new RabbitMqListener(
                telegramConfig,
                messageHandler,
                callBackQueryHandler);
    }
}
