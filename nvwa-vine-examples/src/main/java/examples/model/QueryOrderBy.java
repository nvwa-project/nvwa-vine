package examples.model;

public record QueryOrderBy(
        String field,
        SortOrder order
) {}