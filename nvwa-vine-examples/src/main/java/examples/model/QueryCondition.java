package examples.model;

public record QueryCondition(
        String field,
        OperatorType operator,
        String value
) {}