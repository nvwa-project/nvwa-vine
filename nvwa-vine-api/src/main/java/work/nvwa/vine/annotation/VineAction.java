package work.nvwa.vine.annotation;

import work.nvwa.vine.VineActionExample;
import work.nvwa.vine.SerializationType;

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
public @interface VineAction {

    /**
     * The action mission objective.
     * If not set, use the method name as the mission
     *
     * @return action mission
     */
    String mission() default "";

    /**
     * The action system message prompt prefix.
     * If not set, use the default value in the annotation {@link VineService} parameter systemPromptPrefix
     *
     * @return system message prompt prefix
     */
    String systemPromptPrefix() default "";

    /**
     * The action user message prompt prefix.
     * If not set, use the default value in the annotation {@link VineService} parameter userPromptPrefix
     *
     * @return user message prompt prefix
     */
    String userPromptPrefix() default "";

    /**
     * The action client level.
     * If not set, use the default value in the annotation {@link VineService} parameter clientLevel
     *
     * @return client level
     */
    String clientLevel() default "";

    /**
     * The action parameters and return value serialization type.
     * default use json format, support yaml format
     *
     * @return serialization type
     */
    SerializationType serializationType() default SerializationType.Json;

    /**
     * The action few-shot learning examples. which will be set to the system message prompt
     *
     * @return action examples
     */
    Class<? extends VineActionExample>[] examples() default {};
}
