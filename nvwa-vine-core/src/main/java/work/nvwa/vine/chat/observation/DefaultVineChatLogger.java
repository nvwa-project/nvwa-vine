package work.nvwa.vine.chat.observation;

import work.nvwa.vine.chat.ChatMessage;

import java.util.List;

/**
 * @author Geng Rong
 */
public class DefaultVineChatLogger implements VineChatLogger {

    @Override
    public String start(String methodName, String mission, List<ChatMessage> messages) {
        return "";
    }

    @Override
    public void success(String logId, String assistantMessageText) {

    }

    @Override
    public void fail(String logId, String errorMessage) {

    }
}
