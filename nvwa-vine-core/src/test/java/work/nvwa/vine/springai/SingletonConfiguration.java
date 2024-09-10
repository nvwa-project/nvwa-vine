package work.nvwa.vine.springai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.config.EnableVine;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.prompt.VinePromptConfig;
import work.nvwa.vine.prompt.VinePrompter;

import static work.nvwa.vine.VineConstants.*;


/**
 * @author Geng Rong
 */
@EnableVine
@ActiveProfiles("singleton")
@SpringBootConfiguration
public class SingletonConfiguration {

    @Bean
    public SchemaContext schemaContext() {
        VinePromptConfig vinePromptConfig = new VinePromptConfig(
                DEFAULT_NEW_LINE,
                DEFAULT_HEADER_SYMBOL,
                DEFAULT_DELIMITER_SYMBOL,
                DEFAULT_PERIOD_SYMBOL,
                DEFAULT_ITEM_SYMBOL,
                DEFAULT_ITEM_DESCRIPTION_SYMBOL,
                DEFAULT_DEFAULT_VALUE,
                DEFAULT_NULLABLE,
                DEFAULT_MISSION_TITLE,
                DEFAULT_INPUT_PARAMETERS_TITLE,
                DEFAULT_EXAMPLE_TITLE,
                DEFAULT_EXAMPLE_PARAMETERS_TITLE,
                DEFAULT_EXAMPLE_RETURN_TITLE,
                DEFAULT_RETURN_SCHEMA_TITLE,
                DEFAULT_RETURN_JSON_FORMAT,
                DEFAULT_RETURN_YAML_FORMAT
        );
        VinePrompter vinePrompter = new VinePrompter(vinePromptConfig);
        return new SchemaContext(vinePrompter);
    }

    @Bean
    public VineChatClient vineChatClient() {
        OpenAiApi openAiApi = new OpenAiApi(System.getenv("OPENAI_BASE_URL"), System.getenv("OPENAI_API_KEY"));
        ChatModel chatModel = new OpenAiChatModel(openAiApi, OpenAiChatOptions.builder().withModel(System.getenv("OPENAI_API_KEY")).build());
        return new SingletonVineChatClient(chatModel);
    }

}
