package timofey.xmlParser;

import timofey.utils.enums.Resources;
import timofey.xmlParser.factories.CnbcParserFactory;
import timofey.xmlParser.factories.ReutersParserFactory;

public class AbstractParserFactory {
    public static ParserFactory initParserFactory(Resources resource){
        switch (resource){
            case CNBC:
                return new CnbcParserFactory();
            case Reuters:
                return new ReutersParserFactory();
            case RBK:
                break;
            default:
                break;
        }
        return null;
    }
}
