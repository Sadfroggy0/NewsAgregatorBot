package timofey.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public interface BotCustomInterface {
    void scheduledMessage();
}
