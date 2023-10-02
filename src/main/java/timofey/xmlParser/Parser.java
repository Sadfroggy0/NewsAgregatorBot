package timofey.xmlParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.entities.NewsArticle;
import timofey.utils.httpServices.CustomHttpRequest;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * главный класс, от которого наследуются разные парсеры
 * DefaultHandler - класс библиоткеи SaxParser для парсинга XML документов
 *
 * пример создания парсера для источника
 * ParserFactory factory = AbstractParserFactory.initParserFactory(Sources.CNBC);
 * Parser parser = factory.createFactory();
 */
public abstract class Parser extends DefaultHandler {
    /**
     * Дефолтные XML теги, которые присущи большинству RSS-лент
     */
    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String PUB_DATE = "pubDate";

    private File xmlDocument;
    private StringBuilder elementValue;
    private List<NewsArticle> articleList;

    public Parser(){
        this.elementValue = new StringBuilder();
        this.articleList = new ArrayList<>();
    }
     public List<NewsArticle> parse(String url){

         CustomHttpRequest request = new CustomHttpRequest();
         HttpResponse response = request.getBodyFromRequest(url);
         this.xmlDocument = request.convertStringToXMLDocument(response);

         SAXParserFactory factory = SAXParserFactory.newInstance();
         try {
             SAXParser parser = factory.newSAXParser();
             parser.parse(this.xmlDocument, this);
         }
         catch (ParserConfigurationException | SAXException | IOException e){
             e.printStackTrace();
         }

         String tempFileName = this.xmlDocument.getName();
         boolean isDeleted = this.xmlDocument.delete();
         System.out.println(tempFileName + " is deleted: " + isDeleted);

         return this.articleList;
     };


    public StringBuilder getElementValue() {
        return elementValue;
    }

    public void clearElementValue() {
        this.elementValue = new StringBuilder();
    }

    public List<NewsArticle> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<NewsArticle> articleList) {
        this.articleList = articleList;
    }
}
