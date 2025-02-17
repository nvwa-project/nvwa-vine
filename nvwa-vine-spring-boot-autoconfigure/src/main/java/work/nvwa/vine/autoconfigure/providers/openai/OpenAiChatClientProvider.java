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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * @author Geng Rong
 */
public class OpenAiChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(OpenAiChatClientProvider.class);

    @Override
    public ChatModel buildChatModel(Map<String, Object> clientConfigMap) {
        CommonChatModelProperties clientProperties = new CommonChatModelProperties();
        try {
            SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
            OpenAiApi springApi;
            if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                springApi = new OpenAiApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
            } else {
                springApi = new OpenAiApi(clientProperties.getApiKey());
            }

            if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                return new OpenAiChatModel(springApi);
            }

            OpenAiChatOptions options = new OpenAiChatOptions();
            SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
            return new OpenAiChatModel(springApi, options);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
        }
        return null;
    }

    @Override
    public String getProviderName() {
        return "openai";
    }
}
