package timofey.xmlParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import timofey.config.SourceInit;
import timofey.entities.NewsArticle;

public class XMLCNBCParse extends XMLParserByUrl{
    public XMLCNBCParse(String url, SourceInit rssResources) {
        super(url, rssResources);
    }

    @Override
    protected NewsArticle[] parseXML(String xml) {
        return null;
    }
    private class SAXParser extends DefaultHandler {
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
        }

    }


}
