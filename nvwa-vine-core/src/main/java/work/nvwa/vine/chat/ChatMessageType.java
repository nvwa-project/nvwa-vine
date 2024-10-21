package work.nvwa.vine.chat;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Geng Rong
 */
public enum ChatMessageType {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system"),
    TOOL("tool");

    @JsonValue
    private final String value;

    private ChatMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
