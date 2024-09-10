package work.nvwa.vine.util;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import work.nvwa.vine.chat.ChatMessage;


/**
 * @author Geng Rong
 */
public final class MessageUtils {
    private MessageUtils() {
    }

    public static Message convert(ChatMessage message) {
        return switch (message.type()) {
            case SYSTEM -> new SystemMessage(message.content());
            case USER -> new UserMessage(message.content());
            case ASSISTANT -> new AssistantMessage(message.content());
            default -> throw new IllegalArgumentException("Unsupported message type: " + message.type());
        };
    }
}
