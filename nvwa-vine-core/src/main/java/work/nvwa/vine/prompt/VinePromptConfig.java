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
        String descriptionSymbol,
        String defaultValue,
        String nullable,
        String missionTitle,
        String inputParametersTitle,
        String exampleTitle,
        String exampleParametersTitle,
        String exampleReturnTitle,
        String schemasTitle,
        String returnSchemaTitle,
        String finalResultTitle,
        String returnJsonFormat,
        String returnYamlFormat,
        String thoughtPrompt,
        String continueMessage
) {
}
