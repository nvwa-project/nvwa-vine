package work.nvwa.vine.chat.observation;

import work.nvwa.vine.chat.ChatMessage;

import java.util.List;

/**
 * @author Geng Rong
 */
public interface VineChatLogger {
    String start(String methodName, String mission, List<ChatMessage> messages);

    void success(String logId, String assistantMessageText);

    void fail(String logId, String errorMessage);
}
