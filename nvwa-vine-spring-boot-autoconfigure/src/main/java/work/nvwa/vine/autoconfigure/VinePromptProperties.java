package work.nvwa.vine.autoconfigure;


import static work.nvwa.vine.VineConstants.*;


public class VinePromptProperties {

    private String newLine = DEFAULT_NEW_LINE;
    private String headerSymbol = DEFAULT_HEADER_SYMBOL;
    private String delimiterSymbol = DEFAULT_DELIMITER_SYMBOL;
    private String periodSymbol = DEFAULT_PERIOD_SYMBOL;
    private String defaultValue = DEFAULT_DEFAULT_VALUE;
    private String nullable = DEFAULT_NULLABLE;
    private String inputParametersTitle = DEFAULT_INPUT_PARAMETERS_TITLE;
    private String returnSchemaTitle = DEFAULT_RETURN_SCHEMA_TITLE;

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

    public String getInputParametersTitle() {
        return inputParametersTitle;
    }

    public void setInputParametersTitle(String inputParametersTitle) {
        this.inputParametersTitle = inputParametersTitle;
    }

    public String getReturnSchemaTitle() {
        return returnSchemaTitle;
    }

    public void setReturnSchemaTitle(String returnSchemaTitle) {
        this.returnSchemaTitle = returnSchemaTitle;
    }
}
