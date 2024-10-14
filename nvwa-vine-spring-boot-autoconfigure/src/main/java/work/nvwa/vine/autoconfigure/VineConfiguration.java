package work.nvwa.vine.autoconfigure;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.nvwa.vine.SerializationType;
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
import work.nvwa.vine.chat.observation.DefaultVineChatLogger;
import work.nvwa.vine.chat.observation.VineChatLogger;
import work.nvwa.vine.config.EnableVine;
import work.nvwa.vine.config.VineConfig;
import work.nvwa.vine.context.InvocationContext;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.prompt.VinePromptConfig;
import work.nvwa.vine.prompt.VinePrompter;
import work.nvwa.vine.retry.RetryConfig;

import java.util.List;
import java.util.Map;


/**
 * @author Geng Rong
 */
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
    @ConditionalOnMissingBean(VineChatLogger.class)
    public VineChatLogger vineChatLogger() {
        return new DefaultVineChatLogger();
    }

    @Bean
    @ConditionalOnBean(ChatModel.class)
    public VineChatClient singletonVineChatClient(ChatModel chatModel, VineConfig vineConfig, VineChatLogger vineChatLogger) {
        return new SingletonVineChatClient(chatModel, vineChatLogger, vineConfig);
    }

    @Bean
    @ConditionalOnMissingBean(ChatModel.class)
    public VineChatClient gradedVineChatClient(List<ChatClientProvider> providers, VineProperties vineProperties, VineConfig vineConfig, VineChatLogger vineChatLogger) {
        GradedChatClientProviderBuilder gradedChatClientProviderBuilder = new GradedChatClientProviderBuilder();
        Map<String, List<SingletonVineChatClient>> gradedClientsMap = gradedChatClientProviderBuilder.buildGradedChatClients(providers, vineProperties.getClients(), vineChatLogger, vineConfig);
        return new GradedVineChatClient(gradedClientsMap);
    }

    @Bean
    public VineConfig vineConfig(VineProperties vineProperties) {
        VinePromptProperties vinePromptProperties = vineProperties.getPrompt();
        if (vinePromptProperties == null) {
            vinePromptProperties = new VinePromptProperties();
        }
        VinePromptConfig vinePromptConfig = new VinePromptConfig(
                vinePromptProperties.getNewLine(),
                vinePromptProperties.getHeaderSymbol(),
                vinePromptProperties.getDelimiterSymbol(),
                vinePromptProperties.getPeriodSymbol(),
                vinePromptProperties.getItemSymbol(),
                vinePromptProperties.getDescriptionSymbol(),
                vinePromptProperties.getDefaultValue(),
                vinePromptProperties.getNullable(),
                vinePromptProperties.getMissionTitle(),
                vinePromptProperties.getInputParametersTitle(),
                vinePromptProperties.getExampleTitle(),
                vinePromptProperties.getExampleParametersTitle(),
                vinePromptProperties.getExampleReturnTitle(),
                vinePromptProperties.getSchemasTitle(),
                vinePromptProperties.getReturnSchemaTitle(),
                vinePromptProperties.getFinalResultTitle(),
                vinePromptProperties.getReturnJsonFormat(),
                vinePromptProperties.getReturnYamlFormat(),
                vinePromptProperties.getThoughtPrompt()
        );
        SerializationType defaultSerializationType = vineProperties.getDefaultSerializationType();
        if (defaultSerializationType == null) {
            defaultSerializationType = SerializationType.Yaml;
        }
        if (vineProperties.getRetry() == null) {
            vineProperties.setRetry(new ChatRetryProperties());
        }
        RetryConfig retryConfig = new RetryConfig(vineProperties.getRetry().getMaxAttempts());
        return new VineConfig(vinePromptConfig, retryConfig, defaultSerializationType);
    }

    @Bean
    public SchemaContext schemaContext(VineConfig vineConfig) {
        VinePrompter vinePrompter = new VinePrompter(vineConfig.promptConfig());
        return new SchemaContext(vinePrompter);
    }

    @Bean
    public InvocationContext invocationContext(VineConfig vineConfig, VineChatClient chatClient, SchemaContext schemaContext) {
        return new InvocationContext(vineConfig, chatClient, schemaContext);
    }

}
