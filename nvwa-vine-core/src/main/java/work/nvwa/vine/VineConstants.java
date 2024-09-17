package work.nvwa.vine;

/**
 * @author Geng Rong
 */
public interface VineConstants {
    String BASIC_CHAT_CLIENT_LEVEL = "basic";


    String DEFAULT_NEW_LINE = "\n";
    String DEFAULT_HEADER_SYMBOL = "#";
    String DEFAULT_DELIMITER_SYMBOL = ",";
    String DEFAULT_PERIOD_SYMBOL = ".";
    String DEFAULT_ITEM_SYMBOL = "-";
    String DEFAULT_DESCRIPTION_SYMBOL = ":";
    String DEFAULT_DEFAULT_VALUE = "default value is";
    String DEFAULT_NULLABLE = "nullable";
    String DEFAULT_MISSION_TITLE = "Mission objective";
    String DEFAULT_INPUT_PARAMETERS_TITLE = "Input parameters";
    String DEFAULT_EXAMPLE_TITLE = "Example";
    String DEFAULT_EXAMPLE_PARAMETERS_TITLE = "Example parameters";
    String DEFAULT_EXAMPLE_RETURN_TITLE = "Example return";
    String DEFAULT_SCHEMAS_TITLE = "Schemas";
    String DEFAULT_RETURN_SCHEMA_TITLE = "Return schema";
    String DEFAULT_FINAL_RESULT_TITLE = "Only return the final result";
    String DEFAULT_RETURN_JSON_FORMAT = "Only return the JSON format, remove the ```json``` enclosure";
    String DEFAULT_RETURN_YAML_FORMAT = "Only return the YAML format, remove the ```yaml``` enclosure, string value should be quoted and Ensure that any necessary escape characters";
    String DEFAULT_THOUGHT_PROMPT = "Please do not return the final result, you need analyze the user's input parameters and thinking, only return your thoughts and analysis process.\n" +
            "When the user sends the return schema, then return the final result based on the mission objective, input parameters, and your thinking process.";
}
