package examples.action;


import examples.model.QuerySqlMetadata;
import work.nvwa.vine.annotation.VineService;


/**
 * @author Geng Rong
 */
@VineService
public interface SqlAnalyzeChatActionService {
    QuerySqlMetadata analyzeSql(String sql);
}





