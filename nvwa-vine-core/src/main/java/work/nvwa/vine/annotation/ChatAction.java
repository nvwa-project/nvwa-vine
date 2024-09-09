package work.nvwa.vine.annotation;

import work.nvwa.vine.SerializationType;
import work.nvwa.vine.example.ChatActionExample;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChatAction {

    /**
     * The action mission objective.
     * If not set, use the method name as the mission
     *
     * @return action mission
     */
    String mission() default "";

    /**
     * The action system message prompt prefix.
     * If not set, use the default value in the annotation @ChatActionService.systemPromptPrefix
     *
     * @return system message prompt prefix
     */
    String systemPromptPrefix() default "";

    /**
     * The action user message prompt prefix.
     * If not set, use the default value in the annotation @ChatActionService.userPromptPrefix
     *
     * @return user message prompt prefix
     */
    String userPromptPrefix() default "";

    /**
     * The action client level.
     * If not set, use the default value in the annotation @ChatActionService.clientLevel
     *
     * @return client level
     */
    String clientLevel() default "";

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
    Class<? extends ChatActionExample>[] examples() default {};
}
