package examples.model;


/**
 * @author Geng Rong
 */
public record QueryOrderBy(
        String field,
        SortOrder order
) {}