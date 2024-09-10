package work.nvwa.vine.autoconfigure.providers.minimax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.minimax.MiniMaxChatModel;
import org.springframework.ai.minimax.MiniMaxChatOptions;
import org.springframework.ai.minimax.api.MiniMaxApi;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import work.nvwa.vine.autoconfigure.CommonChatModelProperties;
import work.nvwa.vine.autoconfigure.providers.ChatClientProvider;
import work.nvwa.vine.autoconfigure.utils.SpringPropertiesUtils;
import work.nvwa.vine.chat.client.SingletonVineChatClient;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public class MiniMaxChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(MiniMaxChatClientProvider.class);

    @Override
    public Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties) {
        return chatClientProperties.stream().map(clientConfigMap -> {
            CommonChatModelProperties clientProperties = new CommonChatModelProperties();
            try {
                SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
                MiniMaxApi springApi;
                if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                    springApi = new MiniMaxApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
                } else {
                    springApi = new MiniMaxApi(clientProperties.getApiKey());
                }

                ChatModel chatModel;
                if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                    chatModel = new MiniMaxChatModel(springApi);
                } else {
                    MiniMaxChatOptions options = new MiniMaxChatOptions();
                    SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
                    chatModel = new MiniMaxChatModel(springApi, options);
                }
                return new SingletonVineChatClient(chatModel);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
            }
            return null;
        }).filter(Objects::nonNull);
    }

    @Override
    public String getProviderName() {
        return "minimax";
    }
}
