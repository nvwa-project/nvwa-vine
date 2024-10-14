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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * @author Geng Rong
 */
public class ZhiPuChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(ZhiPuChatClientProvider.class);

    @Override
    public ChatModel buildChatModel(Map<String, Object> clientConfigMap) {
        CommonChatModelProperties clientProperties = new CommonChatModelProperties();
        try {
            SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
            ZhiPuAiApi springApi;
            if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                springApi = new ZhiPuAiApi(clientProperties.getBaseUrl(), clientProperties.getApiKey());
            } else {
                springApi = new ZhiPuAiApi(clientProperties.getApiKey());
            }

            if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                return new ZhiPuAiChatModel(springApi);
            }

            ZhiPuAiChatOptions options = new ZhiPuAiChatOptions();
            SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
            return new ZhiPuAiChatModel(springApi, options);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
        }
        return null;
    }

    @Override
    public String getProviderName() {
        return "zhipu";
    }
}
