package work.nvwa.vine.autoconfigure;

/**
 * @author Geng Rong
 */
public class ChatRetryProperties {

    private int maxAttempts = 3;

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
