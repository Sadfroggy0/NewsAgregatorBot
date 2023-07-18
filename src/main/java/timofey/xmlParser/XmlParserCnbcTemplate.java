package timofey.xmlParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.entities.NewsArticle;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParserCnbcTemplate extends DefaultHandler{
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";

    private List<NewsArticle> articles;
    private StringBuilder elementValue;

    public XmlParserCnbcTemplate(String url) throws ParserConfigurationException, SAXException, IOException {
        XMLParser xmlParserByUrl = new XMLParser(url, this);
        System.out.println();
    }

    private NewsArticle getCurrentArticle(){
        List<NewsArticle> copyList = articles;
        int index = copyList.size() - 1;
        return copyList.get(index);
    }

    public List<NewsArticle> getArticles() {
        return this.articles;
    }

        @Override
        public void startDocument() {
            System.out.println("XML PARSING HAS BEEN STARTED");
        }


        @Override
        public void characters(char[] ch, int start, int length){
            if (elementValue == null) {
                elementValue = new StringBuilder();
            } else {
                elementValue.append(ch, start, length);


            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName){
                case "channel":
                    articles = new ArrayList<>();
                case ITEM:
                    articles.add(new NewsArticle());
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
            switch (qName){
                case TITLE:
                    getCurrentArticle().setTitle(elementValue.toString());
                    break;
                case LINK:
                    getCurrentArticle().setLink(elementValue.toString());
                    break;
                case DESCRIPTION:
                    getCurrentArticle().setDescription(elementValue.toString());
                    break;
                case PUB_DATE:
                    getCurrentArticle().setPubDate(elementValue.toString());
                    break;
                case ID:
                    getCurrentArticle().setId(Long.valueOf(elementValue.toString()));
                    break;
            }
        }

        @Override
        public void endDocument() {
            System.out.println("PARSING IS STOPPED");
        }

    }