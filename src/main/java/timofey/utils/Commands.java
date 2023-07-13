package timofey.utils;

public enum Commands {
    start ("/start"),
    latest("/latest");
    private String rawCommand;

    Commands(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public String getRawCommand() {
        return rawCommand;
    }

}
