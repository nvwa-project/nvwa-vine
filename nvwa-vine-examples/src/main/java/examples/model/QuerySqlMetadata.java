package examples.model;

import java.util.List;


/**
 * @author Geng Rong
 */
public record QuerySqlMetadata(
        String table,
        String[] select,
        List<QueryCondition> where,
        List<QueryOrderBy> orderBy
) {
}
