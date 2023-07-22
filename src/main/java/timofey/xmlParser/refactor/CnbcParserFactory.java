package timofey.xmlParser.refactor;

public class CnbcParserFactory extends ParserFactory {
    @Override
    Parser createFactory() {
        return new CnbcParser();
    }
}
