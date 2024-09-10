package work.nvwa.vine.springai;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.nvwa.vine.springai.service.SqlAnalyzeService;

import java.util.List;


/**
 * @author Geng Rong
 */
@SpringBootTest(classes = SingletonConfiguration.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
public class ChatServiceTest {

    protected static final String TEST_TABLE = "test_table";
    protected static final String TEST_SQL = "select b,c,a from " + TEST_TABLE + " where a = 1 and b like 'Zhang%' or c <= 3 order by created_time desc";

    @Autowired
    private SqlAnalyzeService sqlAnalyzeService;

    @Test
    public void test() {
        SqlAnalyzeService.QuerySqlMetadata sqlMetadata = sqlAnalyzeService.analyzeSql(TEST_SQL);
        checkSqlMetadata(sqlMetadata);
    }

    protected void checkSqlMetadata(SqlAnalyzeService.QuerySqlMetadata sqlMetadata) {
        Assertions.assertNotNull(sqlMetadata);
        Assertions.assertEquals(TEST_TABLE, sqlMetadata.table());
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, sqlMetadata.select());
        Assertions.assertEquals(2, sqlMetadata.where().size());

        List<SqlAnalyzeService.QueryCondition> conditions1 = sqlMetadata.where().get(0).conditions();
        Assertions.assertEquals(2, conditions1.size());
        Assertions.assertEquals("a", conditions1.get(0).field());
        Assertions.assertEquals(SqlAnalyzeService.OperatorType.Equal, conditions1.get(0).operator());
        Assertions.assertEquals("1", conditions1.get(0).value());

        Assertions.assertEquals("b", conditions1.get(1).field());
        Assertions.assertEquals(SqlAnalyzeService.OperatorType.Like, conditions1.get(1).operator());
        Assertions.assertEquals("Zhang%", conditions1.get(1).value());

        List<SqlAnalyzeService.QueryCondition> conditions2 = sqlMetadata.where().get(1).conditions();
        Assertions.assertEquals(1, conditions2.size());

        Assertions.assertEquals("c", conditions2.get(0).field());
        Assertions.assertEquals(SqlAnalyzeService.OperatorType.LessThanOrEqual, conditions2.get(0).operator());
        Assertions.assertEquals("3", conditions2.get(0).value());

        Assertions.assertEquals(1, sqlMetadata.orderBy().size());
        Assertions.assertEquals("created_time", sqlMetadata.orderBy().get(0).field());
        Assertions.assertEquals(SqlAnalyzeService.SortOrder.Descend, sqlMetadata.orderBy().get(0).order());
    }
}
