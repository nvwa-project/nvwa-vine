package work.nvwa.vine.chat.client;


import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.metadata.ChatActionMetadata;

import java.util.List;

public interface VineChatClient {
    <T> T call(List<ChatMessage> messages, ChatActionMetadata chatActionMetadata);
}
