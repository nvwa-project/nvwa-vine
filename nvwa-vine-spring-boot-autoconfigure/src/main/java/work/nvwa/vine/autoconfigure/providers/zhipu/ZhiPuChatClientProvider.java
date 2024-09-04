package work.nvwa.vine.autoconfigure.providers.zhipu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
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

public class ZhiPuChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(ZhiPuChatClientProvider.class);

    @Override
    public Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties) {
        return chatClientProperties.stream().map(clientConfigMap -> {
            CommonChatModelProperties clientProperties = new CommonChatModelProperties();
            try {
                SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
                ZhiPuAiApi springApi;
                if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                    springApi = new ZhiPuAiApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
                } else {
                    springApi = new ZhiPuAiApi(clientProperties.getApiKey());
                }

                ChatModel chatModel;
                if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                    chatModel = new ZhiPuAiChatModel(springApi);
                } else {
                    ZhiPuAiChatOptions options = new ZhiPuAiChatOptions();
                    SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
                    chatModel = new ZhiPuAiChatModel(springApi, options);
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
        return "zhipu";
    }
}
