package examples.action;


import examples.model.QuerySqlMetadata;
import work.nvwa.vine.annotation.ChatActionService;

@ChatActionService
public interface SqlAnalyzeChatActionService {
    QuerySqlMetadata analyzeSql(String sql);
}





