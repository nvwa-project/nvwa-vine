package work.nvwa.vine.autoconfigure.providers;

import work.nvwa.vine.chat.client.SingletonVineChatClient;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public interface ChatClientProvider {
    Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties);

    String getProviderName();
}
