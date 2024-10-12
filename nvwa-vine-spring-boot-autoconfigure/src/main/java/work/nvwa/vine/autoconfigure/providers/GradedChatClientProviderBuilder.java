package work.nvwa.vine.autoconfigure.providers;

import org.springframework.util.CollectionUtils;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.observation.VineChatLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public class GradedChatClientProviderBuilder {


    public Map<String, List<SingletonVineChatClient>> buildGradedChatClients(
            Collection<ChatClientProvider> providers,
            Map<String, Map<String, List<Map<String, Object>>>> clients,
            VineChatLogger vineChatLogger
    ) {
        Map<String, List<SingletonVineChatClient>> gradedClientMap = new HashMap<>();
        clients.forEach((key, gradedClientConfigMap) -> {
            List<SingletonVineChatClient> vineChatClients = providers.stream().flatMap(provider -> {
                List<Map<String, Object>> clientConfigs = gradedClientConfigMap.get(provider.getProviderName());
                if (CollectionUtils.isEmpty(clientConfigs)) {
                    return Stream.empty();
                }
                return provider.buildChatModels(clientConfigs, vineChatLogger);
            }).toList();
            gradedClientMap.put(key, vineChatClients);
        });
        return gradedClientMap;
    }

}
