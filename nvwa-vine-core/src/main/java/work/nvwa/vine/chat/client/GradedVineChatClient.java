package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.type.TypeReference;
import work.nvwa.vine.chat.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public <T> T call(List<ChatMessage> messages, TypeReference<T> responseType, String chatModelLevel) {
        SingletonVineChatClient chatClient = gradedClientMap.get(chatModelLevel).next();
        return chatClient.call(messages, responseType, chatModelLevel);
    }
}
