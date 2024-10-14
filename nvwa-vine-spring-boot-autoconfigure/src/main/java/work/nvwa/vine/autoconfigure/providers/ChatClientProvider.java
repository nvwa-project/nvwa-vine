package work.nvwa.vine.autoconfigure.providers;

import org.springframework.ai.chat.model.ChatModel;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.observation.VineChatLogger;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public interface ChatClientProvider {
    ChatModel buildChatModel(Map<String, Object> clientConfigMap);

    String getProviderName();
}
