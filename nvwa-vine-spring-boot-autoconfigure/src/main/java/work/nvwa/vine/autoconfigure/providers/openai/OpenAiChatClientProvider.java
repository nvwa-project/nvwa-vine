package work.nvwa.vine.autoconfigure.providers.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import work.nvwa.vine.autoconfigure.CommonChatModelProperties;
import work.nvwa.vine.autoconfigure.providers.ChatClientProvider;
import work.nvwa.vine.autoconfigure.utils.SpringPropertiesUtils;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.observation.VineChatLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * @author Geng Rong
 */
public class OpenAiChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(OpenAiChatClientProvider.class);

    @Override
    public Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties, VineChatLogger vineChatLogger) {
        return chatClientProperties.stream().map(clientConfigMap -> {
            CommonChatModelProperties clientProperties = new CommonChatModelProperties();
            try {
                SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
                OpenAiApi springApi;
                if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                    springApi = new OpenAiApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
                } else {
                    springApi = new OpenAiApi(clientProperties.getApiKey());
                }

                ChatModel chatModel;
                if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                    chatModel = new OpenAiChatModel(springApi);
                } else {
                    OpenAiChatOptions options = new OpenAiChatOptions();
                    SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
                    chatModel = new OpenAiChatModel(springApi, options);
                }
                return new SingletonVineChatClient(chatModel, vineChatLogger);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
            }
            return null;
        }).filter(Objects::nonNull);
    }

    @Override
    public String getProviderName() {
        return "openai";
    }
}
