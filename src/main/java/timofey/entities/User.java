package timofey.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tg_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telegram_user_id")
    private Long telegramUserId;
    @Column(name = "user_name")
    private String userName;
    @Column(name="subscribed_resources")
    private List<Long> subResources;
    @Column(name = "subscription_date")
    private Timestamp subscriptionDate;
    @Column(name = "chat_id")
    private Long chatId;
    public User(){}
    public User(Long telegramUserId, String userName,Timestamp subscriptionDate, Long chatId){
        this.telegramUserId = telegramUserId;
        this.userName = userName;
        this.subscriptionDate = subscriptionDate;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getTelegramUserID() {
        return telegramUserId;
    }

    public User setTelegramUserID(Long telegramUserID) {
        this.telegramUserId = telegramUserID;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public List<Long> getSubResources() {
        return subResources;
    }

    public User setSubResources(List<Long> subResources) {
        this.subResources = subResources;
        return this;
    }

    public Timestamp getSubscriptionDate() {
        return subscriptionDate;
    }

    public User setSubscriptionDate(Timestamp subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
        return this;
    }

    public Long getChatId() {
        return chatId;

    }

    public User setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }
}
