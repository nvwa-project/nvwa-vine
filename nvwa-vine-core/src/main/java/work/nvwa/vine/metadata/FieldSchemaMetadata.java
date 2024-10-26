package work.nvwa.vine.metadata;

import work.nvwa.vine.SchemaFieldType;
import work.nvwa.vine.annotation.VineField;

import java.lang.reflect.Field;

/**
 * @author Geng Rong
 */
public class FieldSchemaMetadata {
    private final String name;
    private final String type;
    private final TypeSchemaMetadata typeSchemaMetadata;
    private final String description;
    private final String defaultValue;
    private final boolean required;

    public FieldSchemaMetadata(Field field, TypeSchemaMetadata typeSchemaMetadata) {
        VineField schemaField = field.getAnnotation(VineField.class);
        this.name = field.getName();
        this.typeSchemaMetadata = typeSchemaMetadata;
        if (schemaField != null) {
            this.description = schemaField.description();
            this.defaultValue = schemaField.defaultValue();
            this.required = schemaField.required();
            if (SchemaFieldType.AUTO.equals(schemaField.type())) {
                type = typeSchemaMetadata.getName();
            } else {
                type = schemaField.type();
            }
        } else {
            this.description = "";
            this.defaultValue = "";
            this.required = true;
            type = typeSchemaMetadata.getName();
        }
    }

    public String getSchema() {
        StringBuilder schema = new StringBuilder(name).append(": ");
        schema.append(type);
        if (required) {
            schema.append(", required field");
        }
        if (!description.isEmpty()) {
            schema.append(", ").append(description).append(". ");
        }
        if (!defaultValue.isEmpty()) {
            schema.append("default value is ").append(defaultValue);
        }
        return schema.toString();
    }

    public TypeSchemaMetadata getTypeSchemaMetadata() {
        return typeSchemaMetadata;
    }
}
