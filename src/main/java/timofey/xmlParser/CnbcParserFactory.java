package timofey.xmlParser;

import timofey.xmlParser.parserTemplates.CnbcParser;

public class CnbcParserFactory implements ParserFactory {
    @Override
    public Parser createFactory() {
        return new CnbcParser();
    }
}
