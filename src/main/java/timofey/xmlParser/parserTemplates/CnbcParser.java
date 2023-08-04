package timofey.xmlParser.parserTemplates;

import org.xml.sax.Attributes;
import timofey.entities.NewsArticle;
import timofey.xmlParser.Parser;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CnbcParser extends Parser {

    @Override
    public void startDocument() {
        System.out.println("XML PARSING HAS BEEN STARTED");
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (getElementValue() == null) {
            clearElementValue();
        } else {
            getElementValue().append(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case ITEM:
                getArticleList().add(new NewsArticle());
                break;
            case TITLE:
                clearElementValue();
                break;
            case LINK:
                clearElementValue();
                break;
            case DESCRIPTION:
                clearElementValue();
                break;
            case PUB_DATE:
                clearElementValue();
                break;
            case ID:
                clearElementValue();
                break;

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(getArticleList().size() > 0) {
            switch (qName) {
                case TITLE:
                    getLastArticle().setTitle(getElementValue().toString());
                    break;
                case LINK:
                    getLastArticle().setLink(getElementValue().toString());
                    break;
                case DESCRIPTION:
                    getLastArticle().setDescription(getElementValue().toString());
                    break;
                case PUB_DATE:
                    String stringDate = getElementValue().toString();
                    Timestamp timestampDate = new Timestamp(new Date(stringDate).getTime());
                    getLastArticle().setPubDate(timestampDate);
                    break;
                case ID:
                    getLastArticle().setId(Long.valueOf(getElementValue().toString()));
                    break;
            }
        }
    }

    protected NewsArticle getLastArticle(){
        List<NewsArticle> list = getArticleList();
        int index = list.size() - 1;
        return list.get(index);
    }
    @Override
    public void endDocument() {
        System.out.println("PARSING IS STOPPED");
    }



}
