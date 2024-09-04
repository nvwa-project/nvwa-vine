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
import work.nvwa.vine.chat.client.SingletonVineChatClient;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class QianFanChatClientProvider implements ChatClientProvider {

    private final Logger logger = LoggerFactory.getLogger(QianFanChatClientProvider.class);

    @Override
    public Stream<SingletonVineChatClient> buildChatModels(Collection<Map<String, Object>> chatClientProperties) {
        return chatClientProperties.stream().map(clientConfigMap -> {
            QianFanChatModelProperties clientProperties = new QianFanChatModelProperties();
            try {
                SpringPropertiesUtils.copyProperties(clientProperties, clientConfigMap);
                QianFanApi qianFanApi;
                if (StringUtils.hasText(clientProperties.getBaseUrl())) {
                    qianFanApi = new QianFanApi(clientProperties.getBaseUrl(), clientProperties.getApiKey(), clientProperties.getSecretKey());
                } else {
                    qianFanApi = new QianFanApi(clientProperties.getApiKey(), clientProperties.getSecretKey());
                }

                ChatModel chatModel;
                if (CollectionUtils.isEmpty(clientProperties.getOptions())) {
                    chatModel = new QianFanChatModel(qianFanApi);
                } else {
                    QianFanChatOptions options = new QianFanChatOptions();
                    SpringPropertiesUtils.copyProperties(options, clientProperties.getOptions());
                    chatModel = new QianFanChatModel(qianFanApi, options);
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
        return "qianfan";
    }
}
