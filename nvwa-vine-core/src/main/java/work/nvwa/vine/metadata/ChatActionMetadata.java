package work.nvwa.vine.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;
import work.nvwa.vine.SerializationType;

import static work.nvwa.vine.VineConstants.BASIC_CHAT_CLIENT_LEVEL;

public class ChatActionMetadata {
    private final String userPrompt;

    private final String systemPrompt;

    private final String clientLevel;

    private final SerializationType serializationType;

    private final TypeReference<?> returnTypeRef;

    public ChatActionMetadata(String userPrompt, String systemPrompt, String clientLevel, SerializationType serializationType, TypeReference<?> returnTypeRef) {
        this.userPrompt = userPrompt;
        this.systemPrompt = systemPrompt;
        if (StringUtils.hasLength(clientLevel)) {
            this.clientLevel = clientLevel;
        } else {
            this.clientLevel = BASIC_CHAT_CLIENT_LEVEL;
        }
        this.serializationType = serializationType;
        this.returnTypeRef = returnTypeRef;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public String getClientLevel() {
        return clientLevel;
    }

    public SerializationType getSerializationType() {
        return serializationType;
    }

    public TypeReference<?> getReturnTypeRef() {
        return returnTypeRef;
    }
}