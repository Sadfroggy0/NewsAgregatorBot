package timofey.xmlParser.refactor;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.entities.NewsArticle;

import java.io.File;
import java.util.List;

public abstract class Parser extends DefaultHandler {
    private File xmlDocument;
    abstract List<NewsArticle> parse();


}
