package work.nvwa.vine.chat;


/**
 * @author Geng Rong
 */
public record ChatMessage(ChatMessageType type, String content) {

    public static ChatMessage systemMessage(String content) {
        return new ChatMessage(ChatMessageType.SYSTEM, content);
    }

    public static ChatMessage userMessage(String content) {
        return new ChatMessage(ChatMessageType.USER, content);
    }

    public static ChatMessage assistantMessage(String content) {
        return new ChatMessage(ChatMessageType.ASSISTANT, content);
    }
}
