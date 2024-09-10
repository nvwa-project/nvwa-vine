package work.nvwa.vine.chat.client;

import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.metadata.ChatActionMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Geng Rong
 */
public class GradedVineChatClient implements VineChatClient {
    private final Map<String, RoundRobinClientPool> gradedClientMap;

    public GradedVineChatClient(Map<String, List<SingletonVineChatClient>> gradedClients) {
        Map<String, RoundRobinClientPool> gradedClientMap = new HashMap<>();
        gradedClients.forEach((level, clients) -> {
            RoundRobinClientPool pool = new RoundRobinClientPool(clients);
            gradedClientMap.put(level, pool);
        });
        this.gradedClientMap = gradedClientMap;
    }

    @Override
    public <T> T call(List<ChatMessage> messages, ChatActionMetadata chatActionMetadata) {
        SingletonVineChatClient chatClient = gradedClientMap.get(chatActionMetadata.clientLevel()).next();
        return chatClient.call(messages, chatActionMetadata);
    }
}
