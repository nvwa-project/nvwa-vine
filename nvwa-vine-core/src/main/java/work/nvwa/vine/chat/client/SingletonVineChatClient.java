package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.metadata.VineFunctionMetadata;
import work.nvwa.vine.util.JsonUtils;
import work.nvwa.vine.util.MessageUtils;
import work.nvwa.vine.util.YamlUtils;

import java.util.List;

/**
 * @author Geng Rong
 */
public class SingletonVineChatClient implements VineChatClient {

    private final ChatModel chatModel;

    public SingletonVineChatClient(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public <T> T call(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        String assistantMessageText = chatModel.call(messageArray);
        try {
            return convert(assistantMessageText, vineFunctionMetadata);
        } catch (JsonProcessingException e) {
            Message[] regenerateMessageArray = new Message[messages.size() + 2];
            System.arraycopy(messageArray, 0, regenerateMessageArray, 0, messageArray.length);
            regenerateMessageArray[messageArray.length] = new AssistantMessage(assistantMessageText);
            String errorMessage = "I tried to convert the message to Json format failed, please check error message, regenerate the message. \n the error message is: " + e.getMessage();
            regenerateMessageArray[messageArray.length + 1] = new UserMessage(errorMessage);
            String regenerateAssistantMessageText = chatModel.call(regenerateMessageArray);
            try {
                return convert(regenerateAssistantMessageText, vineFunctionMetadata);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public ChatMessage callMessage(List<ChatMessage> messages, VineFunctionMetadata vineFunctionMetadata) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        String assistantMessageText = chatModel.call(messageArray);
        return ChatMessage.assistantMessage(assistantMessageText);
    }

    private <T> T convert(String assistantMessageText, VineFunctionMetadata vineFunctionMetadata) throws JsonProcessingException {
        if (vineFunctionMetadata.serializationType() == SerializationType.Yaml) {
            return (T) YamlUtils.fromYamlWithoutEnclosure(assistantMessageText, vineFunctionMetadata.returnTypeRef());
        } else if (vineFunctionMetadata.serializationType() == SerializationType.Json) {
            return (T) JsonUtils.fromJsonWithoutEnclosure(assistantMessageText, vineFunctionMetadata.returnTypeRef());
        } else {
            throw new IllegalArgumentException("Unsupported SerializationType " + vineFunctionMetadata.serializationType());
        }
    }
}
