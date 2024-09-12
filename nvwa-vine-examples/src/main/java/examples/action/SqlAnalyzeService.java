package examples.action;


import examples.model.QuerySqlMetadata;
import work.nvwa.vine.annotation.VineService;


/**
 * @author Geng Rong
 */
@VineService
public interface SqlAnalyzeService {
    QuerySqlMetadata analyzeSql(String sql);
}





