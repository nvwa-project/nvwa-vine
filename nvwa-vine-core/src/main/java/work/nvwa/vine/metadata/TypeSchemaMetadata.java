package work.nvwa.vine.metadata;

import work.nvwa.vine.prompt.VinePrompter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Geng Rong
 */
public class TypeSchemaMetadata {
    private final String name;
    private final List<Type> types;
    private String basicInfo;
    private String subFieldsInfo;
    private String fullSchemaInfo;
    private List<FieldSchemaMetadata> subFields;

    public TypeSchemaMetadata(String name, Type type) {
        this(name, Collections.singletonList(type));
    }

    public TypeSchemaMetadata(String name, List<Type> types) {
        this(name, types, "", "", "", new ArrayList<>());
    }

    public TypeSchemaMetadata(String name, List<Type> types, String basicInfo, String subFieldsInfo, String fullSchemaInfo, List<FieldSchemaMetadata> subFields) {
        this.name = name;
        this.types = types;
        this.basicInfo = basicInfo;
        this.subFieldsInfo = subFieldsInfo;
        this.fullSchemaInfo = fullSchemaInfo;
        this.subFields = subFields;
    }

    public void fillingInfo(VinePrompter vinePrompter, String basicInfo, List<FieldSchemaMetadata> subFields) {
        this.basicInfo = basicInfo;
        this.subFields = subFields;
        generateSchemaInfo(vinePrompter);
    }

    public void generateSchemaInfo(VinePrompter vinePrompter) {
        StringBuilder subFieldsInfo = new StringBuilder();
        StringBuilder fullSchemaInfo = new StringBuilder(vinePrompter.header(3, basicInfo));
        if (!subFields.isEmpty()) {
            subFieldsInfo.append(subFields.stream().map(FieldSchemaMetadata::getSchema).collect(Collectors.joining(vinePrompter.newLine())));
            fullSchemaInfo.append(vinePrompter.newLine()).append(subFieldsInfo);
        }
        this.subFieldsInfo = subFieldsInfo.toString();
        this.fullSchemaInfo = fullSchemaInfo.toString();
    }

    public String getName() {
        return name;
    }

    public List<Type> getTypes() {
        return types;
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
