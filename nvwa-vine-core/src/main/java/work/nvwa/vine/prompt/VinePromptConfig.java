package work.nvwa.vine.prompt;

public record VinePromptConfig(
        String newLine,
        String headerSymbol,
        String delimiterSymbol,
        String periodSymbol,
        String defaultValue,
        String nullable,
        String missionTitle,
        String inputParametersTitle,
        String exampleTitle,
        String exampleParametersTitle,
        String exampleReturnTitle,
        String returnSchemaTitle,
        String returnJsonFormat,
        String returnYamlFormat
) {
}
