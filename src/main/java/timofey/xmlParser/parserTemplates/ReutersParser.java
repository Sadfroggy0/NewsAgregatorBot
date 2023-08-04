package timofey.xmlParser.parserTemplates;

import org.apache.commons.lang3.StringEscapeUtils;
import timofey.xmlParser.Parser;

import java.sql.Timestamp;
import java.util.Date;

public class ReutersParser extends CnbcParser {
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
                    getLastArticle().setDescription(
                            StringEscapeUtils.unescapeHtml4(
                                    getElementValue()
                                            .toString()
                                            .replaceAll("<a[^>]*>[^<]*</a>", "")
//                                                            .replaceAll("<p[^>]*>[^<]*</a>", "")
                                            .replace("<p>","")
                                            .replace("</p>","")
//                                                            .replace("<a>","")
//                                                            .replace("</a>","")
                            )
                    );
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
}
