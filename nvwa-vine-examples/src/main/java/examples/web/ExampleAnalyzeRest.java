package examples.web;

import examples.action.SqlAnalyzeChatActionService;
import examples.model.QuerySqlMetadata;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleAnalyzeRest {

    private final SqlAnalyzeChatActionService sqlAnalyzeChatActionService;

    public ExampleAnalyzeRest(SqlAnalyzeChatActionService sqlAnalyzeChatActionService) {
        this.sqlAnalyzeChatActionService = sqlAnalyzeChatActionService;
    }

    @PostMapping("/api/analyze/sql")
    public QuerySqlMetadata analyzeSql(@RequestBody String sql) {
        Assert.notNull(sql, "SQL request body can not be null");
        return sqlAnalyzeChatActionService.analyzeSql(sql);
    }
}
