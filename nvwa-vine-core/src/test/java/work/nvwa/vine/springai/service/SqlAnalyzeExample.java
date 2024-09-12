package work.nvwa.vine.springai.service;

import work.nvwa.vine.VineFunctionExample;
import work.nvwa.vine.ExampleCase;

import java.util.List;


/**
 * @author Geng Rong
 */
public class SqlAnalyzeExample implements VineFunctionExample {
    @Override
    public ExampleCase exampleCase() {
        return new ExampleCase(
                new Object[]{"select a,b,c from table_a where a = 1 and b like 'Zhang%' or c <= 3 order by created_time desc"},
                new SqlAnalyzeService.QuerySqlMetadata(
                        "table_a",
                        new String[]{"a", "b", "c"},
                        List.of(
                                new SqlAnalyzeService.QueryConditionGroup(
                                        List.of(
                                                new SqlAnalyzeService.QueryCondition("a", SqlAnalyzeService.OperatorType.Equal, "1"),
                                                new SqlAnalyzeService.QueryCondition("b", SqlAnalyzeService.OperatorType.Like, "Zhang%")
                                        )
                                ),
                                new SqlAnalyzeService.QueryConditionGroup(
                                        List.of(
                                                new SqlAnalyzeService.QueryCondition("c", SqlAnalyzeService.OperatorType.LessThanOrEqual, "3")
                                        )
                                )
                        ),
                        List.of(
                                new SqlAnalyzeService.QueryOrderBy("created_time", SqlAnalyzeService.SortOrder.Descend)
                        )
                )
        );
    }
}
