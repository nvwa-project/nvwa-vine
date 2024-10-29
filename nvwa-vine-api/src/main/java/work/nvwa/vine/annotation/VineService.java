package work.nvwa.vine.annotation;


import work.nvwa.vine.SerializationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Geng Rong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VineService {

    /**
     * The chat system message prompt prefix.
     *
     * @return system message prompt prefix
     */
    String systemPromptPrefix() default "";

    /**
     * The chat user message prompt prefix.
     *
     * @return user message prompt prefix
     */
    String userPromptPrefix() default "";

    /**
     * The chat model client level.
     *
     * @return client level
     */
    String clientLevel() default "";

    /**
     * The maximum number of retries when the chat model call fails.
     * The default value is -1, which means using the global configuration.
     *
     * @return maximum number of retries
     */
    int maxRetryAttempts() default -1;

    /**
     * The maximum number of tokens to generate
     * Default is -1, which means using the global configuration
     *
     * @return max tokens limit
     */
    int maxTokens() default -1;

    /**
     * The number of continuations.
     * when the output length exceeds maxTokens, the model will continue to be called to generate until the output length
     * is less than or equal to maxTokens or the number of continuations reaches maxContinuation.
     * Default is -1, which means using the global configuration
     *
     * @return max continuation
     */
    int maxContinuation() default -1;

    /**
     * The action parameters and return value serialization type.
     * If not set, use the default value in the global configuration
     * Supports json, yaml format
     *
     * @return serialization type
     */
    SerializationType serializationType() default SerializationType.Default;
}
