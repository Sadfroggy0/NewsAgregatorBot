package timofey.xmlParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.entities.NewsArticle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;

import java.sql.Timestamp;
import java.util.*;

public class XmlParserCnbcTemplate extends XMLParser{
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";


    @Override
    public List<NewsArticle> parseXml() throws ParserConfigurationException, SAXException, IOException {
        DefaultHandler defaultHandler = new CnbcParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(getXmlDocument(), defaultHandler);
        String tempFileName = getXmlDocument().getName();
        boolean isDeleted = this.deleteTempFile();
        System.out.println(tempFileName + " id deleted: " + isDeleted);

        return getArticleList();

    }

    public XmlParserCnbcTemplate(String url) throws ParserConfigurationException, IOException, SAXException {
        super(url);

    }

    class CnbcParser extends DefaultHandler {

        private List<NewsArticle> articleList = getArticleList();

        @Override
        public void startDocument() {
            System.out.println("XML PARSING HAS BEEN STARTED");
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (elementValue == null) {
                elementValue = new StringBuilder();
            } else {
                elementValue.append(ch, start, length);
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName) {
//                case "channel":
////                    articleList = new ArrayList<>();
                case ITEM:
                    articleList.add(new NewsArticle());
                    break;
                case TITLE:
                    elementValue = new StringBuilder();
                    break;
                case LINK:
                    elementValue = new StringBuilder();
                    break;
                case DESCRIPTION:
                    elementValue = new StringBuilder();
                    break;
                case PUB_DATE:
                    elementValue = new StringBuilder();
                    break;
                case ID:
                    elementValue = new StringBuilder();
                    break;

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if(getArticleList().size() > 0) {
                switch (qName) {
                    case TITLE:
                        getLastArticle().setTitle(elementValue.toString());
                        break;
                    case LINK:
                        getLastArticle().setLink(elementValue.toString());
                        break;
                    case DESCRIPTION:
                        getLastArticle().setDescription(elementValue.toString());
                        break;
                    case PUB_DATE:
                        String stringDate = elementValue.toString();
                        Timestamp timestampDate = new Timestamp(new Date(stringDate).getTime());
                        getLastArticle().setPubDate(timestampDate);
                        break;
                    case ID:
                        getLastArticle().setId(Long.valueOf(elementValue.toString()));
                        break;
                }
            }
        }

        @Override
        public void endDocument() {
            System.out.println("PARSING IS STOPPED");
        }
    }
}
