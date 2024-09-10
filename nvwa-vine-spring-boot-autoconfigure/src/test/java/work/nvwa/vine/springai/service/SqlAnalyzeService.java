package work.nvwa.vine.springai.service;

import work.nvwa.vine.annotation.VineService;
import work.nvwa.vine.annotation.VineField;

import java.util.List;

@VineService
public interface SqlAnalyzeService {

    QuerySqlMetadata analyzeSql(String sql);

    enum SortOrder {
        Descend, Ascend
    }

    enum OperatorType {
        Equal, NotEqual, GreaterThan, GreaterThanOrEqual, LessThan, LessThanOrEqual, Like, NotLike,
        In, NotIn, Between, NotBetween, IsNull, IsNotNull, IsTrue, IsFalse,
    }

    record QuerySqlMetadata(
            String table,
            @VineField(description = "return sorted by ascii code")
            String[] select,
            @VineField(description = "The conditions within each group are combined with AND, while the groups are combined with OR")
            List<QueryConditionGroup> where,
            List<QueryOrderBy> orderBy
    ) {
    }

    record QueryConditionGroup(
            List<QueryCondition> conditions
    ) {
    }

    record QueryCondition(
            String field,
            OperatorType operator,
            String value
    ) {
    }

    record QueryOrderBy(
            String field,
            SortOrder order
    ) {
    }
}
