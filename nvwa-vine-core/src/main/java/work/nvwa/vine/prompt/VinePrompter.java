package work.nvwa.vine.prompt;

public final class VinePrompter {

    private final VinePromptConfig config;

    public VinePrompter(VinePromptConfig config) {
        this.config = config;
    }

    public String delimiter() {
        return config.delimiterSymbol() + " ";
    }

    public String delimiter(String text) {
        return delimiter() + text;
    }

    public String newLine() {
        return newLine(1);
    }

    public String newLine(int count) {
        return config.newLine().repeat(count);
    }

    public String newLine(String text) {
        return newLine(1, text);
    }

    public String newLine(int count, String text) {
        return newLine(count) + text;
    }

    public String header(int level) {
        return config.headerSymbol().repeat(level) + " ";
    }

    public String header(int level, String text) {
        return header(level) + text;
    }

    public String missionTitle() {
        return config.missionTitle();
    }

    public String inputParameterTitle() {
        return config.inputParametersTitle();
    }

    public String exampleTitle() {
        return config.exampleTitle();
    }

    public String exampleParametersTitle() {
        return config.exampleParametersTitle();
    }

    public String exampleReturnTitle() {
        return config.exampleReturnTitle();
    }

    public String returnSchemaTitle() {
        return config.returnSchemaTitle();
    }

    public String returnJsonFormat() {
        return config.returnJsonFormat();
    }

    public String returnYamlFormat() {
        return config.returnYamlFormat();
    }
}
