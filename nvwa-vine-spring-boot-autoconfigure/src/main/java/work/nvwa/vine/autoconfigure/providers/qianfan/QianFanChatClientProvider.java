package work.nvwa.vine.autoconfigure.providers.qianfan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.qianfan.QianFanChatModel;
import org.springframework.ai.qianfan.QianFanChatOptions;
import org.springframework.ai.qianfan.api.QianFanApi;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import work.nvwa.vine.autoconfigure.providers.ChatClientProvider;
import work.nvwa.vine.autoconfigure.utils.SpringPropertiesUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * @author Geng Rong
 */
public class QianFanChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(QianFanChatClientProvider.class);

    @Override
    public ChatModel buildChatModel(Map<String, Object> clientConfigMap) {
        QianFanChatModelProperties clientProperties = new QianFanChatModelProperties();
        try {
            SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
            QianFanApi qianFanApi;
            if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                qianFanApi = new QianFanApi(clientProperties.getBaseUrl(), clientProperties.getApiKey(), clientProperties.getSecretKey());
            } else {
                qianFanApi = new QianFanApi(clientProperties.getApiKey(), clientProperties.getSecretKey());
            }

            if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                return new QianFanChatModel(qianFanApi);
            }

            QianFanChatOptions options = new QianFanChatOptions();
            SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
            return new QianFanChatModel(qianFanApi, options);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            logger.error("Failed to build [{}] chat model from properties: {}", getProviderName(), clientConfigMap);
        }
        return null;
    }

    @Override
    public String getProviderName() {
        return "qianfan";
    }
}
