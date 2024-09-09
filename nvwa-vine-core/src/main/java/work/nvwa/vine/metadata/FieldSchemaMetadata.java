package work.nvwa.vine.metadata;

import work.nvwa.vine.SchemaFieldType;
import work.nvwa.vine.annotation.ChatSchemaField;

import java.lang.reflect.Field;

public class FieldSchemaMetadata {
    private final String name;
    private final String type;
    private final TypeSchemaMetadata typeSchemaMetadata;
    private final String description;
    private final String defaultValue;
    private final boolean nullable;

    public FieldSchemaMetadata(Field field, TypeSchemaMetadata typeSchemaMetadata) {
        ChatSchemaField schemaField = field.getAnnotation(ChatSchemaField.class);
        this.name = field.getName();
        this.typeSchemaMetadata = typeSchemaMetadata;
        if (schemaField != null) {
            this.description = schemaField.description();
            this.defaultValue = schemaField.defaultValue();
            this.nullable = schemaField.nullable();
            if (SchemaFieldType.AUTO.equals(schemaField.type())) {
                type = typeSchemaMetadata.getName();
            } else {
                type = schemaField.type();
            }
        } else {
            this.description = "";
            this.defaultValue = "";
            this.nullable = false;
            type = typeSchemaMetadata.getName();
        }
    }

    public String getSchema() {
        StringBuilder schema = new StringBuilder(name).append(": ");
        schema.append(type);
        if (nullable) {
            schema.append(", nullable");
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
