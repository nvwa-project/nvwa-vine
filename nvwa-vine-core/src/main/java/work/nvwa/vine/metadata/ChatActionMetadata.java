package work.nvwa.vine.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static work.nvwa.vine.VineConstants.BASIC_CHAT_CLIENT_LEVEL;

public class ChatActionMetadata {
    private final String userPrompt;

    private final String systemPrompt;

    private final String clientLevel;

    private final TypeReference<?> returnTypeRef;

    public ChatActionMetadata(String userPrompt, String systemPrompt, String clientLevel, TypeReference<?> returnTypeRef) {
        this.userPrompt = userPrompt;
        this.systemPrompt = systemPrompt;
        if (StringUtils.hasLength(clientLevel)) {
            this.clientLevel = clientLevel;
        } else {
            this.clientLevel = BASIC_CHAT_CLIENT_LEVEL;
        }
        this.returnTypeRef = returnTypeRef;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt(Map<String, Object> parameters) {
        String userMessage = userPrompt;
        if (parameters != null && !parameters.isEmpty()) {
            String parametersPrompt = parameters.entrySet().stream()
                    .map(entry -> "### " + entry.getKey() + "\n" + entry.getValue())
                    .collect(Collectors.joining("\n"));
            if (!parametersPrompt.isEmpty()) {
                userMessage += "\n\n## Input parameters\n\n" + parametersPrompt;
            }
        }
        if (StringUtils.hasLength(userMessage)) {
            userMessage += "\n\n";
        }
        userMessage += "## Return JSON only";
        return userMessage;
    }

    public String getClientLevel() {
        return clientLevel;
    }

    public TypeReference<?> getReturnTypeRef() {
        return returnTypeRef;
    }
}