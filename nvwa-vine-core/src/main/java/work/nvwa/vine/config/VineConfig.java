package work.nvwa.vine.config;

import work.nvwa.vine.SerializationType;
import work.nvwa.vine.prompt.VinePromptConfig;

public record VineConfig(
        VinePromptConfig promptConfig,
        SerializationType defaultSerializationType
) {
}
