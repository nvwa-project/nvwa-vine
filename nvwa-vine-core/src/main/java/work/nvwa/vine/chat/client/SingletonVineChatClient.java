package work.nvwa.vine.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.util.JsonUtils;
import work.nvwa.vine.util.MessageUtils;

import java.util.List;

public class SingletonVineChatClient implements VineChatClient {

    private final ChatModel chatModel;

    public SingletonVineChatClient(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public <T> T call(List<ChatMessage> messages, TypeReference<T> responseType, String chatModelLevel) {
        Message[] messageArray = messages.stream().map(MessageUtils::convert).toArray(Message[]::new);
        String responseText = chatModel.call(messageArray);
        try {
            return JsonUtils.fromJsonWithoutEnclosure(responseText, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
