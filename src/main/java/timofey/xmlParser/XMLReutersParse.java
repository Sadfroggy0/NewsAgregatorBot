package timofey.xmlParser;

import timofey.config.SourceInit;
import timofey.entities.NewsArticle;

import java.util.List;

public class XMLReutersParse extends XMLParserByUrl{
    public XMLReutersParse(String url, SourceInit rssResources) {
        super(url, rssResources);
    }

    @Override
    protected NewsArticle[] parseXML(String xml) {
        return new NewsArticle[0];
    }

    List<NewsArticle> getArticles() {
        return null;
    }

}
