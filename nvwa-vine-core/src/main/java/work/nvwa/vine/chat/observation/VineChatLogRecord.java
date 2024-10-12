package work.nvwa.vine.chat.observation;

import work.nvwa.vine.chat.ChatMessage;

import java.util.List;

/**
 * @author Geng Rong
 */
public class VineChatLogRecord {
    private String id;
    private List<ChatMessage> messages;
    private String assistantMessageText;
    private VineChatLogRecordState state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getAssistantMessageText() {
        return assistantMessageText;
    }

    public void setAssistantMessageText(String assistantMessageText) {
        this.assistantMessageText = assistantMessageText;
    }

    public VineChatLogRecordState getState() {
        return state;
    }

    public void setState(VineChatLogRecordState state) {
        this.state = state;
    }
}
