package work.nvwa.vine.chat;

public record ChatMessage(ChatMessageType type, String content) {

    public static ChatMessage systemMessage(String content) {
        return new ChatMessage(ChatMessageType.SYSTEM, content);
    }

    public static ChatMessage userMessage(String content) {
        return new ChatMessage(ChatMessageType.USER, content);
    }
}