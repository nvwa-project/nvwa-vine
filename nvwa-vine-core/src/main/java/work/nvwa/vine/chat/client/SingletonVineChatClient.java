package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.retry.support.RetryTemplate;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.chat.observation.DefaultVineChatLogger;
import work.nvwa.vine.chat.observation.VineChatLogger;
import work.nvwa.vine.config.VineConfig;
import work.nvwa.vine.metadata.VineFunctionMetadata;
import work.nvwa.vine.util.JsonUtils;
import work.nvwa.vine.util.MessageUtils;
import work.nvwa.vine.util.YamlUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Geng Rong
 */
public class SingletonVineChatClient implements VineChatClient {

    private final Logger logger = LoggerFactory.getLogger(SingletonVineChatClient.class);

    private final ChatModel chatModel;

    private final VineChatLogger vineChatLogger;

    private final VineConfig vineConfig;

    private final RetryTemplate globalRetryTemplate;

    public SingletonVineChatClient(ChatModel chatModel) {
        this(chatModel, null, null);
    }

    public SingletonVineChatClient(ChatModel chatModel, VineChatLogger vineChatLogger, VineConfig vineConfig) {
        this.chatModel = chatModel;
        this.vineChatLogger = vineChatLogger instanceof DefaultVineChatLogger ? null : vineChatLogger;
        this.vineConfig = vineConfig;
        if (vineConfig != null) {
            this.globalRetryTemplate = RetryTemplate.builder().maxAttempts(3).build();
        } else {
            this.globalRetryTemplate = RetryTemplate.builder().maxAttempts(0).build();
        }
    }

    @Override
    public <T> T call(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        String assistantMessageText = callAsString(messages, vineFunctionMetadata);
        try {
            return convert(assistantMessageText, vineFunctionMetadata);
        } catch (JsonProcessingException e) {
            List<ChatMessage> regenerateMessageArray = new LinkedList<>(messages);
            regenerateMessageArray.add(ChatMessage.assistantMessage(assistantMessageText));
            String errorReTryMessage = "I tried to convert the message to Json format failed, please check error message, regenerate the message. \n the error message is: " + e.getMessage();
            regenerateMessageArray.add(ChatMessage.userMessage(errorReTryMessage));
            String regenerateAssistantMessageText = callAsString(regenerateMessageArray, vineFunctionMetadata);
            try {
                return convert(regenerateAssistantMessageText, vineFunctionMetadata);
            } catch (JsonProcessingException ex) {
                logger.error("convert the message failed, the error message is: {}. \nThe assistant message is: {}", ex.getMessage(), regenerateAssistantMessageText);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public ChatMessage callMessage(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        return ChatMessage.assistantMessage(callAsString(messages, vineFunctionMetadata));
    }

    private String callAsString(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        if (vineChatLogger == null) {
            return retryCall(messages, vineFunctionMetadata);
        }
        String logRecordId = vineChatLogger.start(messages);
        try {
            String assistantMessageText = retryCall(messages, vineFunctionMetadata);
            vineChatLogger.success(logRecordId, assistantMessageText);
            return assistantMessageText;
        } catch (Throwable e) {
            vineChatLogger.fail(logRecordId, e.getMessage());
            throw e;
        }
    }

    private String retryCall(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        if (vineFunctionMetadata.maxRetryAttempts() < 0) {
            return globalRetryTemplate.execute(context -> chatModel.call(messageArray));
        }
        return RetryTemplate.builder().maxAttempts(vineFunctionMetadata.maxRetryAttempts()).build().execute(context -> chatModel.call(messageArray));
    }

    private <T> T convert(String assistantMessageText, VineFunctionMetadata vineFunctionMetadata) throws JsonProcessingException {
        if (vineFunctionMetadata.returnTypeRef().getType() == String.class) {
            return (T) assistantMessageText;
        } else if (vineFunctionMetadata.serializationType() == SerializationType.Yaml) {
            return (T) YamlUtils.fromYamlWithoutEnclosure(assistantMessageText, vineFunctionMetadata.returnTypeRef());
        } else if (vineFunctionMetadata.serializationType() == SerializationType.Json) {
            return (T) JsonUtils.fromJsonWithoutEnclosure(assistantMessageText, vineFunctionMetadata.returnTypeRef());
        } else {
            throw new IllegalArgumentException("Unsupported SerializationType " + vineFunctionMetadata.serializationType());
        }
    }
}
