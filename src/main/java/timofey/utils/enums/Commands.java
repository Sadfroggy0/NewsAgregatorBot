package timofey.utils.enums;

public enum Commands {
    start ("/start"),
    latest("/latest"),
    subscription("subscription"),
    subscribe("subscribe"),
    certainSourceSub("certainSourceSub"),
    cnbcSub("cnbcSub"),
    reutersSub("reutersSub");
    private String rawCommand;

    Commands(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public String getRawCommand() {
        return rawCommand;
    }

}
