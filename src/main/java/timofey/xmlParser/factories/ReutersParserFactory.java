package timofey.xmlParser.factories;

import timofey.xmlParser.Parser;
import timofey.xmlParser.ParserFactory;
import timofey.xmlParser.parserTemplates.ReutersParser;

public class ReutersParserFactory implements ParserFactory {

    @Override
    public Parser createFactory() {
        return new ReutersParser();
    }
}
