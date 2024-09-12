package work.nvwa.vine.chat.client;


import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.metadata.VineFunctionMetadata;

import java.util.List;

/**
 * @author Geng Rong
 */
public interface VineChatClient {
    <T> T call(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata);
}
