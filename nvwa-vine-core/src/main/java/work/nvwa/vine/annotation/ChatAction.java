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
    String task() default "";

    String clientLevel() default "";

    SerializationType serializationType() default SerializationType.Yaml;

    Class<? extends ChatActionExample>[] examples() default {};
}
