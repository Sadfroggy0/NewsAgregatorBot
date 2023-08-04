package timofey.xmlParser.factories;

import timofey.xmlParser.Parser;
import timofey.xmlParser.ParserFactory;
import timofey.xmlParser.parserTemplates.CnbcParser;

public class CnbcParserFactory implements ParserFactory {
    @Override
    public Parser createFactory() {
        return new CnbcParser();
    }
}
