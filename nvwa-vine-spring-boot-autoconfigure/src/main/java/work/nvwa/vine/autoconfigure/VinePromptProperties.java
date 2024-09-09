package work.nvwa.vine.autoconfigure;


import static work.nvwa.vine.VineConstants.*;


public class VinePromptProperties {

    private String newLine = DEFAULT_NEW_LINE;
    private String headerSymbol = DEFAULT_HEADER_SYMBOL;
    private String delimiterSymbol = DEFAULT_DELIMITER_SYMBOL;
    private String periodSymbol = DEFAULT_PERIOD_SYMBOL;
    private String defaultValue = DEFAULT_DEFAULT_VALUE;
    private String nullable = DEFAULT_NULLABLE;
    private String missionTitle = DEFAULT_MISSION_TITLE;
    private String inputParametersTitle = DEFAULT_INPUT_PARAMETERS_TITLE;
    private String exampleTitle = DEFAULT_EXAMPLE_TITLE;
    private String exampleParametersTitle = DEFAULT_EXAMPLE_PARAMETERS_TITLE;
    private String exampleReturnTitle = DEFAULT_EXAMPLE_RETURN_TITLE;
    private String returnSchemaTitle = DEFAULT_RETURN_SCHEMA_TITLE;
    private String returnJsonFormat = DEFAULT_RETURN_JSON_FORMAT;
    private String returnYamlFormat = DEFAULT_RETURN_YAML_FORMAT;

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getHeaderSymbol() {
        return headerSymbol;
    }

    public void setHeaderSymbol(String headerSymbol) {
        this.headerSymbol = headerSymbol;
    }

    public String getDelimiterSymbol() {
        return delimiterSymbol;
    }

    public void setDelimiterSymbol(String delimiterSymbol) {
        this.delimiterSymbol = delimiterSymbol;
    }

    public String getPeriodSymbol() {
        return periodSymbol;
    }

    public void setPeriodSymbol(String periodSymbol) {
        this.periodSymbol = periodSymbol;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getMissionTitle() {
        return missionTitle;
    }

    public void setMissionTitle(String missionTitle) {
        this.missionTitle = missionTitle;
    }

    public String getInputParametersTitle() {
        return inputParametersTitle;
    }

    public void setInputParametersTitle(String inputParametersTitle) {
        this.inputParametersTitle = inputParametersTitle;
    }

    public String getExampleTitle() {
        return exampleTitle;
    }

    public void setExampleTitle(String exampleTitle) {
        this.exampleTitle = exampleTitle;
    }

    public String getExampleParametersTitle() {
        return exampleParametersTitle;
    }

    public void setExampleParametersTitle(String exampleParametersTitle) {
        this.exampleParametersTitle = exampleParametersTitle;
    }

    public String getExampleReturnTitle() {
        return exampleReturnTitle;
    }

    public void setExampleReturnTitle(String exampleReturnTitle) {
        this.exampleReturnTitle = exampleReturnTitle;
    }

    public String getReturnSchemaTitle() {
        return returnSchemaTitle;
    }

    public void setReturnSchemaTitle(String returnSchemaTitle) {
        this.returnSchemaTitle = returnSchemaTitle;
    }

    public String getReturnJsonFormat() {
        return returnJsonFormat;
    }

    public void setReturnJsonFormat(String returnJsonFormat) {
        this.returnJsonFormat = returnJsonFormat;
    }

    public String getReturnYamlFormat() {
        return returnYamlFormat;
    }

    public void setReturnYamlFormat(String returnYamlFormat) {
        this.returnYamlFormat = returnYamlFormat;
    }
}
