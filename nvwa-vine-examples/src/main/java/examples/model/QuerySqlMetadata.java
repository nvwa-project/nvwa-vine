package examples.model;

import java.util.List;

public record QuerySqlMetadata(
        String table,
        String[] select,
        List<QueryCondition> where,
        List<QueryOrderBy> orderBy
) {
}
