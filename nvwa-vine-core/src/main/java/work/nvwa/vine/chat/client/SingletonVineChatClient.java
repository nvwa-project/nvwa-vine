package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.chat.observation.DefaultVineChatLogger;
import work.nvwa.vine.chat.observation.VineChatLogger;
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

    public SingletonVineChatClient(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.vineChatLogger = null;
    }

    public SingletonVineChatClient(ChatModel chatModel, VineChatLogger vineChatLogger) {
        this.chatModel = chatModel;
        this.vineChatLogger = vineChatLogger instanceof DefaultVineChatLogger ? null : vineChatLogger;
    }

    @Override
    public <T> T call(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        String assistantMessageText = callMessage(messages);
        try {
            return convert(assistantMessageText, vineFunctionMetadata);
        } catch (JsonProcessingException e) {
            List<ChatMessage> regenerateMessageArray = new LinkedList<>(messages);
            regenerateMessageArray.add(ChatMessage.assistantMessage(assistantMessageText));
            String errorReTryMessage = "I tried to convert the message to Json format failed, please check error message, regenerate the message. \n the error message is: " + e.getMessage();
            regenerateMessageArray.add(ChatMessage.userMessage(errorReTryMessage));
            String regenerateAssistantMessageText = callMessage(regenerateMessageArray);
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
        return ChatMessage.assistantMessage(callMessage(messages));
    }

    private String callMessage(List<ChatMessage> messages) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        if (vineChatLogger == null) {
            return chatModel.call(messageArray);
        }
        String logRecordId = vineChatLogger.start(messages);
        try {
            String assistantMessageText = chatModel.call(messageArray);
            vineChatLogger.success(logRecordId, assistantMessageText);
            return assistantMessageText;
        } catch (Throwable e) {
            vineChatLogger.fail(logRecordId, e.getMessage());
            throw e;
        }
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
