package work.nvwa.vine.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;
import work.nvwa.vine.SerializationType;

import static work.nvwa.vine.VineConstants.BASIC_CHAT_CLIENT_LEVEL;


/**
 * @author Geng Rong
 */
public record VineFunctionMetadata(
        String userPrompt,
        String systemPrompt,
        String schemaPrompt,
        String missionObjective,
        String clientLevel,
        Boolean enableThought,
        SerializationType serializationType,
        TypeReference<?> returnTypeRef,
        Integer maxRetryAttempts,
        Integer maxTokens,
        Integer maxContinuation
) {
    public VineFunctionMetadata(String userPrompt, String systemPrompt, String schemaPrompt, String missionObjective, String clientLevel,
                                Boolean enableThought, SerializationType serializationType, TypeReference<?> returnTypeRef,
                                Integer maxRetryAttempts, Integer maxTokens, Integer maxContinuation) {
        this.userPrompt = userPrompt;
        this.systemPrompt = systemPrompt;
        this.schemaPrompt = schemaPrompt;
        this.missionObjective = missionObjective;
        this.enableThought = enableThought;
        if (StringUtils.hasLength(clientLevel)) {
            this.clientLevel = clientLevel;
        } else {
            this.clientLevel = BASIC_CHAT_CLIENT_LEVEL;
        }
        this.serializationType = serializationType;
        this.returnTypeRef = returnTypeRef;
        this.maxRetryAttempts = maxRetryAttempts;
        this.maxTokens = maxTokens;
        this.maxContinuation = maxContinuation;
    }
}