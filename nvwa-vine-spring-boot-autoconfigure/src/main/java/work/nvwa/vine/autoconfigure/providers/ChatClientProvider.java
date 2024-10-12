package work.nvwa.vine.autoconfigure.providers;

import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.observation.VineChatLogger;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public interface ChatClientProvider {
    Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties, VineChatLogger vineChatLogger);

    String getProviderName();
}
