package timofey.entities;
import jakarta.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "news")
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    private String topic;

    @Column(name = "pub_date")
    private Timestamp pubDate;
    @Transient
    private String description;
    @Column(name = "resource_id")
    private Long resourceId;

    public Long getId() {
        return id;
    }

    public NewsArticle setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NewsArticle setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLink() {
        return link;
    }

    public NewsArticle setLink(String link) {
        this.link = link;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public NewsArticle setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Timestamp getPubDate() {
        return pubDate;
    }

    public NewsArticle setPubDate(Timestamp pubDate) {
        this.pubDate = pubDate;
        return this;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public NewsArticle setResourceId(Long resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NewsArticle setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        String result;
        result = "**" + this.title + "**" + "\n" +
                this.description + "\n" +
                "[Источник](" + this.link + ")" + "\n" +
                this.pubDate + "\n" +"\n";

        return result;
    }
}
