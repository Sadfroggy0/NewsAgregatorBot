package timofey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TelegramConfig {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    public String getBotToken() {
        return botToken;
    }
    public String getBotName() {
        return botName;
    }
}
