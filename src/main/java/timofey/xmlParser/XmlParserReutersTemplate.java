package timofey.xmlParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.entities.NewsArticle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class XmlParserReutersTemplate extends XMLParser {


    public XmlParserReutersTemplate(String url) {
        super(url);
    }

    @Override
    public List<NewsArticle> parseXml() throws ParserConfigurationException, SAXException, IOException {
        DefaultHandler defaultHandler = new ReutersParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(getXmlDocument(), defaultHandler);
        String tempFileName = getXmlDocument().getName();
        boolean isDeleted = this.deleteTempFile();
        System.out.println(tempFileName + " id deleted: " + isDeleted);
        return getArticleList();
    }

    class ReutersParser extends DefaultHandler{

    }

}
