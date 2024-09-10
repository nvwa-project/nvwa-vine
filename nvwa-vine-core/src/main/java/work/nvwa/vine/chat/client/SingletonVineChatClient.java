package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.metadata.ChatActionMetadata;
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
    public <T> T call(List<ChatMessage> messages, ChatActionMetadata chatActionMetadata) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        String responseText = chatModel.call(messageArray);
        try {
            if (chatActionMetadata.serializationType() == SerializationType.Yaml) {
                return (T) YamlUtils.fromYamlWithoutEnclosure(responseText, chatActionMetadata.returnTypeRef());
            } else if (chatActionMetadata.serializationType() == SerializationType.Json) {
                return (T) JsonUtils.fromJsonWithoutEnclosure(responseText, chatActionMetadata.returnTypeRef());
            } else {
                throw new IllegalArgumentException("Unsupported SerializationType " + chatActionMetadata.serializationType());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
