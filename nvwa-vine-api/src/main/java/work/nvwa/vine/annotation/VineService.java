package work.nvwa.vine.annotation;


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
     * The action system message prompt prefix.
     *
     * @return system message prompt prefix
     */
    String systemPromptPrefix() default "";

    /**
     * The action user message prompt prefix.
     *
     * @return user message prompt prefix
     */
    String userPromptPrefix() default "";

    /**
     * The action client level.
     *
     * @return client level
     */
    String clientLevel() default "";
}
