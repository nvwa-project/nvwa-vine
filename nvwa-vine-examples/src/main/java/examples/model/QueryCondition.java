package examples.model;


/**
 * @author Geng Rong
 */
public record QueryCondition(
        String field,
        OperatorType operator,
        String value
) {}