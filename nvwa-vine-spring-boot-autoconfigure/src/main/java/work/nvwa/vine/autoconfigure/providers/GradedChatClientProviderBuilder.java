package work.nvwa.vine.autoconfigure.providers;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.observation.VineChatLogger;
import work.nvwa.vine.config.VineConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public class GradedChatClientProviderBuilder {


    public Map<String, List<SingletonVineChatClient>> buildGradedChatClients(
            Collection<ChatClientProvider> providers,
            Map<String, Map<String, List<Map<String, Object>>>> clients,
            VineChatLogger vineChatLogger, VineConfig vineConfig
    ) {
        Map<String, List<SingletonVineChatClient>> gradedClientMap = new HashMap<>();
        clients.forEach((key, gradedClientConfigMap) -> {
            List<SingletonVineChatClient> vineChatClients = providers.stream().flatMap(provider -> {
                List<Map<String, Object>> clientConfigs = gradedClientConfigMap.get(provider.getProviderName());
                if (CollectionUtils.isEmpty(clientConfigs)) {
                    return Stream.empty();
                }
                return clientConfigs.stream().map(clientConfigMap -> {
                    ChatModel chatModel = provider.buildChatModel(clientConfigMap);
                    if (chatModel == null) {
                        return null;
                    }
                    return new SingletonVineChatClient(chatModel, vineChatLogger, vineConfig);
                }).filter(Objects::nonNull);
            }).toList();
            gradedClientMap.put(key, vineChatClients);
        });
        return gradedClientMap;
    }

}
