package work.nvwa.vine.config;

import work.nvwa.vine.SerializationType;
import work.nvwa.vine.prompt.VinePromptConfig;
import work.nvwa.vine.retry.RetryConfig;

public record VineConfig(
        VinePromptConfig promptConfig,
        RetryConfig retryConfig,
        SerializationType defaultSerializationType
) {
}
