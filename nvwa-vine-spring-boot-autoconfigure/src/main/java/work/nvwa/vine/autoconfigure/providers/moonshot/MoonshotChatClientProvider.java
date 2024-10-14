package work.nvwa.vine.autoconfigure.providers.moonshot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.moonshot.MoonshotChatModel;
import org.springframework.ai.moonshot.MoonshotChatOptions;
import org.springframework.ai.moonshot.api.MoonshotApi;
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
public class MoonshotChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(MoonshotChatClientProvider.class);

    @Override
    public ChatModel buildChatModel(Map<String, Object> clientConfigMap) {
        CommonChatModelProperties clientProperties = new CommonChatModelProperties();
        try {
            SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
            MoonshotApi springApi;
            if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                springApi = new MoonshotApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
            } else {
                springApi = new MoonshotApi(clientProperties.getApiKey());
            }

            if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                return new MoonshotChatModel(springApi);
            }

            MoonshotChatOptions options = new MoonshotChatOptions();
            SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
            return new MoonshotChatModel(springApi, options);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
        }
        return null;
    }

    @Override
    public String getProviderName() {
        return "moonshot";
    }
}
