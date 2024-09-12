package examples.web;

import examples.action.SqlAnalyzeService;
import examples.model.QuerySqlMetadata;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Geng Rong
 */
@RestController
public class ExampleAnalyzeRest {

    private final SqlAnalyzeService sqlAnalyzeService;

    public ExampleAnalyzeRest(SqlAnalyzeService sqlAnalyzeService) {
        this.sqlAnalyzeService = sqlAnalyzeService;
    }

    @PostMapping("/api/analyze/sql")
    public QuerySqlMetadata analyzeSql(@RequestBody String sql) {
        Assert.notNull(sql, "SQL request body can not be null");
        return sqlAnalyzeService.analyzeSql(sql);
    }
}
