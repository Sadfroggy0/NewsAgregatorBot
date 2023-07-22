package timofey.xmlParser;

import timofey.utils.enums.Resources;
public class AbstractParserFactory {
    public static ParserFactory initParserFactory(Resources resource){
        switch (resource){
            case CNBC:
                return new CnbcParserFactory();
            case Reuters:
                break;
            case RBK:
                break;
            default:
                break;
        }
        return null;
    }
}
