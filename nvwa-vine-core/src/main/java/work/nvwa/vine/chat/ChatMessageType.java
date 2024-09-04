package work.nvwa.vine.chat;

public enum ChatMessageType {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system"),
    TOOL("tool");

    private final String value;

    private ChatMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
