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
     * The maximum number of tokens to generate
     * Default is -1, which means no limit.
     *
     * @return max tokens limit
     */
    int maxTokens() default -1;

    /**
     * The action parameters and return value serialization type.
     * default use yaml format, support json format
     *
     * @return serialization type
     */
    SerializationType serializationType() default SerializationType.Yaml;

    /**
     * The action few-shot learning examples. which will be set to the system message prompt
     *
     * @return action examples
     */
    Class<? extends VineFunctionExample>[] examples() default {};
}
