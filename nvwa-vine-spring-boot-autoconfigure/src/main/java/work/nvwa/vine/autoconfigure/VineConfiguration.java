package work.nvwa.vine.autoconfigure;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.nvwa.vine.autoconfigure.providers.ChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.GradedChatClientProviderBuilder;
import work.nvwa.vine.autoconfigure.providers.deepseek.DeepseekChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.minimax.MiniMaxChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.moonshot.MoonshotChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.openai.OpenAiChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.qianfan.QianFanChatClientProvider;
import work.nvwa.vine.autoconfigure.providers.zhipu.ZhiPuChatClientProvider;
import work.nvwa.vine.chat.client.GradedVineChatClient;
import work.nvwa.vine.chat.client.SingletonVineChatClient;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.config.EnableVine;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.prompt.VinePromptConfig;
import work.nvwa.vine.prompt.VinePrompter;

import java.util.List;
import java.util.Map;

@EnableVine
@Configuration
@EnableConfigurationProperties(VineProperties.class)
public class VineConfiguration {


    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.openai.api.OpenAiApi")
    public DeepseekChatClientProvider deepseekChatClientProvider() {
        return new DeepseekChatClientProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.openai.api.OpenAiApi")
    public OpenAiChatClientProvider openAiChatClientProvider() {
        return new OpenAiChatClientProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.minimax.api.MiniMaxApi")
    public MiniMaxChatClientProvider miniMaxChatClientProvider() {
        return new MiniMaxChatClientProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.moonshot.api.MoonshotApi")
    public MoonshotChatClientProvider moonshotChatClientProvider() {
        return new MoonshotChatClientProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.qianfan.api.QianFanApi")
    public QianFanChatClientProvider qianFanChatClientProvider() {
        return new QianFanChatClientProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.ai.zhipuai.api.ZhiPuAiApi")
    public ZhiPuChatClientProvider zhiPuChatClientProvider() {
        return new ZhiPuChatClientProvider();
    }

    @Bean
    @ConditionalOnBean(ChatModel.class)
    public VineChatClient singletonVineChatClient(ChatModel chatModel) {
        return new SingletonVineChatClient(chatModel);
    }

    @Bean
    @ConditionalOnMissingBean(ChatModel.class)
    public VineChatClient gradedVineChatClient(List<ChatClientProvider> providers, VineProperties vineProperties) {
        GradedChatClientProviderBuilder gradedChatClientProviderBuilder = new GradedChatClientProviderBuilder();
        Map<String, List<SingletonVineChatClient>> gradedClientsMap = gradedChatClientProviderBuilder.buildGradedChatClients(providers, vineProperties.getClients());
        return new GradedVineChatClient(gradedClientsMap);
    }

    @Bean
    public SchemaContext schemaContext(VineProperties vineProperties) {
        VinePromptProperties vinePromptProperties = vineProperties.getPrompt();
        if (vinePromptProperties == null) {
            vinePromptProperties = new VinePromptProperties();
        }
        VinePromptConfig vinePromptConfig = new VinePromptConfig(
                vinePromptProperties.getNewLine(),
                vinePromptProperties.getHeaderSymbol(),
                vinePromptProperties.getDelimiterSymbol(),
                vinePromptProperties.getPeriodSymbol(),
                vinePromptProperties.getDefaultValue(),
                vinePromptProperties.getNullable(),
                vinePromptProperties.getInputParametersTitle(),
                vinePromptProperties.getExampleTitle(),
                vinePromptProperties.getExampleParametersTitle(),
                vinePromptProperties.getExampleReturnTitle(),
                vinePromptProperties.getReturnSchemaTitle(),
                vinePromptProperties.getReturnJsonFormat(),
                vinePromptProperties.getReturnYamlFormat()
        );
        VinePrompter vinePrompter = new VinePrompter(vinePromptConfig);
        return new SchemaContext(vinePrompter);
    }
}
