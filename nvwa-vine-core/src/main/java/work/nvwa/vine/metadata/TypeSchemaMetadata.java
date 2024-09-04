package work.nvwa.vine.metadata;

import java.util.ArrayList;
import java.util.List;

public class TypeSchemaMetadata {
    private final String name;
    private final String basicInfo;
    private final String subFieldsInfo;
    private final String fullSchemaInfo;
    private final List<FieldSchemaMetadata> subFields;

    public TypeSchemaMetadata(String name) {
        this(name, "", "", "", new ArrayList<>());
    }

    public TypeSchemaMetadata(String name, String basicInfo, String subFieldsInfo, String fullSchemaInfo, List<FieldSchemaMetadata> subFields) {
        this.name = name;
        this.basicInfo = basicInfo;
        this.subFieldsInfo = subFieldsInfo;
        this.fullSchemaInfo = fullSchemaInfo;
        this.subFields = subFields;
    }

    public String getName() {
        return name;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public String getSubFieldsInfo() {
        return subFieldsInfo;
    }

    public String getFullSchemaInfo() {
        return fullSchemaInfo;
    }

    public List<FieldSchemaMetadata> getSubFields() {
        return subFields;
    }
}
