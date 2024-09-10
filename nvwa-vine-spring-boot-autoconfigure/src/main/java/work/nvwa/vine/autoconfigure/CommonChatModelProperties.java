package work.nvwa.vine.autoconfigure;

import java.util.Map;


/**
 * @author Geng Rong
 */
public class CommonChatModelProperties {
    private String baseUrl;
    private String apiKey;
    private Map<String, Object> options;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
