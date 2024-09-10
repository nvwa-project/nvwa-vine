package work.nvwa.vine.autoconfigure.providers.qianfan;

import work.nvwa.vine.autoconfigure.CommonChatModelProperties;


/**
 * @author Geng Rong
 */
public class QianFanChatModelProperties extends CommonChatModelProperties {

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
