package work.nvwa.vine.prompt;

/**
 * @author Geng Rong
 */
public record VinePromptConfig(
        String newLine,
        String headerSymbol,
        String delimiterSymbol,
        String periodSymbol,
        String itemSymbol,
        String itemDescriptionSymbol,
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
