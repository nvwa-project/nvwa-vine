package work.nvwa.vine.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;
import work.nvwa.vine.SerializationType;

import java.util.List;
import java.util.Map;


/**
 * @author Geng Rong
 */
@ConfigurationProperties(VineProperties.CONFIG_PREFIX)
public class VineProperties {
    public static final String CONFIG_PREFIX = "nvwa.vine";

    private VinePromptProperties prompt;
    private SerializationType defaultSerializationType;
    private Integer maxTokens;
    private Integer maxContinuation;
    private ChatRetryProperties retry;
    private Map<String, Map<String, List<Map<String, Object>>>> clients;

    public VinePromptProperties getPrompt() {
        return prompt;
    }

    public void setPrompt(VinePromptProperties prompt) {
        this.prompt = prompt;
    }

    public SerializationType getDefaultSerializationType() {
        return defaultSerializationType;
    }

    public void setDefaultSerializationType(SerializationType defaultSerializationType) {
        this.defaultSerializationType = defaultSerializationType;
    }

    public ChatRetryProperties getRetry() {
        return retry;
    }

    public void setRetry(ChatRetryProperties retry) {
        this.retry = retry;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getMaxContinuation() {
        return maxContinuation;
    }

    public void setMaxContinuation(Integer maxContinuation) {
        this.maxContinuation = maxContinuation;
    }

    public Map<String, Map<String, List<Map<String, Object>>>> getClients() {
        return clients;
    }

    public void setClients(Map<String, Map<String, List<Map<String, Object>>>> clients) {
        this.clients = clients;
    }
}
