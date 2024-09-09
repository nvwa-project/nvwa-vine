package work.nvwa.vine.annotation;

import work.nvwa.vine.SchemaFieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChatSchemaField {
    String type() default SchemaFieldType.AUTO;

    String description() default "";

    String defaultValue() default "";

    boolean nullable() default false;

    int order() default 0;

    boolean returnIgnore() default false;
}
