package work.nvwa.vine.chat.client;


import com.fasterxml.jackson.core.type.TypeReference;
import work.nvwa.vine.chat.ChatMessage;

import java.util.List;

public interface VineChatClient {
    <T> T call(List<ChatMessage> messages, TypeReference<T> responseType, String chatClientLevel);
}
