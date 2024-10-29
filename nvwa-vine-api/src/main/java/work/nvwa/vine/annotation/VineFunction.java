package work.nvwa.vine.annotation;

import work.nvwa.vine.SerializationType;
import work.nvwa.vine.VineFunctionExample;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Geng Rong
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VineFunction {

    /**
     * The function mission objective.
     * If not set, use the method name as the mission
     *
     * @return function mission
     */
    String mission() default "";

    /**
     * The chat system message prompt prefix.
     * If not set, use the default value in the annotation {@link VineService} parameter systemPromptPrefix
     *
     * @return system message prompt prefix
     */
    String systemPromptPrefix() default "";

    /**
     * The chat user message prompt prefix.
     * If not set, use the default value in the annotation {@link VineService} parameter userPromptPrefix
     *
     * @return user message prompt prefix
     */
    String userPromptPrefix() default "";

    /**
     * The chat model client level.
     * If not set, use the default value in the annotation {@link VineService} parameter clientLevel
     *
     * @return chat model client level
     */
    String clientLevel() default "";

    /**
     * The maximum number of retries when the chat model call fails.
     * The default value is -1, which means using the annotation {@link VineService} parameter maxRetryAttempts
     *
     * @return maximum number of retries
     */
    int maxRetryAttempts() default -1;

    /**
     * Whether to enable thought mode, if thought mode is enabled, the large model will be guided to think before answering questions, which will improve the quality of the answers
     *
     * @return enable thought
     */
    boolean enableThought() default false;

    /**
     * The maximum number of tokens to generate
     * Default is -1, which means using the annotation {@link VineService} parameter maxTokens
     *
     * @return max tokens limit
     */
    int maxTokens() default -1;

    /**
     * The number of continuations.
     * when the output length exceeds maxTokens, the model will continue to be called to generate until the output length
     * is less than or equal to maxTokens or the number of continuations reaches maxContinuation.
     * Default is -1, which means using the annotation {@link VineService} parameter maxContinuation
     *
     * @return max continuation
     */
    int maxContinuation() default -1;

    /**
     * The action parameters and return value serialization type
     * use the default value in the global configuration or the annotation {@link VineService} parameter serializationType
     * if not use the default value, which will override
     * Supports json, yaml format
     *
     * @return serialization type
     */
    SerializationType serializationType() default SerializationType.Default;

    /**
     * The action few-shot learning examples. which will be set to the system message prompt
     *
     * @return action examples
     */
    Class<? extends VineFunctionExample>[] examples() default {};
}
