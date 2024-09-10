<div align="center">

<h1>Nvwa Vine 🌿</h1>

让代码访问 AI 变得简单且高效

简体中文 · [English](./README.md)

</div>

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/work.nvwa/nvwa-vine-bom/badge.png)](https://search.maven.org/artifact/work.nvwa/nvwa-vine-bom/)

- ⚡️ 简单易用, 快速高效
- ⚙️️ 代码驱动, 易于管理
- 💰 分级模型, 节省成本
- 👁 可观测性, 易于调试


## 名字的由来

Vine 是英文"藤"的意思, 此项目为 Nvwa 项目的一部分, 意为女娲藤.

女娲是中国上古神话中的创世女神, 传说女娲参照自己的外貌用黄河的泥土捏制泥人, 再施加神力, 泥人便变成了人类.

但神州大地广袤无垠, 捏泥人的速度太慢, 女娲疲惫不堪, 便从崖壁上拉下一条枯藤，伸入泥潭里，向地面挥洒，泥点溅落的地方, 便成一片人类，不久，大地上就布满了人类的踪迹.

藤极大的提高了女娲创造人类的效率, Vine 项目也希望能够帮助开发者更高效地构建 AI 交互应用.

## 为什么使用 Vine

我们在构建 AI 交互应用时, 通常需要处理用户输入, 生成对应的输出, 以及处理输出的结果.
对于简单的业务, 我们可以使用 Prompt 模板, 来生成对应的提示词.

但是是在一些复杂场景, 我们可能需要根据输入, 上下文, 会话以及 RAG 查询到的信息, 构建相对复杂的提示词, 同时用复杂的结构体来接收 AI 的输出.

业务越复杂, 用户输入的场景也就越多, 随着业务的增长, 我们需要不断地维护提示词, 以及对应的处理逻辑.

比如当我们调整一个提示词的输入或者输出结构时, 不仅要调整相关的代码, 还要调整描述结构的提示词, 以及对应的 `few-shot` 数据.

且因为提示词与代码是分离的, 就容易出现提示词与代码不一致, 更新提示词时容易出错, 代码逻辑复杂, 难以维护等问题, 就像文档与代码不一致一样, 这就需要我们构建一个 AI 提示词框架.

作为十余年架构经验的全栈开发者, 我坚信代码是最好的文档. 同理, 在任务原子化之后, 针对结构化的 AI 交互场景, 代码也是可以最好的提示词.

Vine 提供了代码驱动的方式, 通过代码来生成提示词, 以及处理用户输入, 从而极大地减少了提示词与代码不一致的问题, 并减少构建提示词的复杂度.

以尽可能少的额外代码, 帮助您更快地构建 AI 交互, 更好地管理提示词工程.

## 快速开始

增加依赖, Vine 可以通过 [Maven 中央仓库](https://central.sonatype.com/artifact/work.nvwa/nvwa-vine-bom) 找到.

当然, 您也可以 clone 代码自行构建.

```xml

<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>work.nvwa</groupId>
                <artifactId>nvwa-vine-bom</artifactId>
                <version>${vine.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>work.nvwa</groupId>
            <artifactId>nvwa-vine-spring-boot-starter-spring-ai</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

参考 Spring AI 的使用方式, 完成 Chat Model 的相关配置.

例如使用 OpenAI 的方式, 通过 `application.yml` 配置:

```yaml
spring:
  ai:
    openai:
      apiKey: <your-api-key>
```

创建访问 AI 的接口, 并增加 `@ChatActionService` 注解, 方法入参即为用户输入的变化部分, 返回值即为 AI 结构化输出.

如果业务场景或者返回结构比较复杂, 可以使用相应的注解来增加描述.

```java
@VineService
public interface SqlAnalyzeService {
    QuerySqlMetadata analyzeSql(String sql);
}

public record QuerySqlMetadata(
        String table,
        String[] select,
        List<QueryCondition> where,
        List<QueryOrderBy> orderBy
) {
}

public record QueryCondition(
        String field,
        OperatorType operator,
        String value
) {
}

public record QueryOrderBy(
        String field,
        SortOrder order
) {
}

public enum SortOrder {
    Descend, Ascend
}

public enum OperatorType {
    Equal, NotEqual, GreaterThan, GreaterThanOrEqual, LessThan, LessThanOrEqual, Like, NotLike, IsNull, IsNotNull, IsTrue, IsFalse,
}
```

在需要调用 AI 的地方, 注入接口, 调用对应方法即可.

```java
@Component
public class ChatServiceTest {
    private final SqlAnalyzeService sqlAnalyzeService;

    public ChatServiceTest(SqlAnalyzeService sqlAnalyzeService) {
        this.sqlAnalyzeService = sqlAnalyzeService;
    }

    public void businessMethod() {
        // needAnalyzeSql 为用户输入的变化部分
        // 例如: "select b,c,a from test_table where a = 1 and b like 'Zhang%' and c <= 3 order by created_time desc"
        QuerySqlMetadata sqlMetadata = sqlAnalyzeService.analyzeSql(needAnalyzeSql);
        // use sqlMetadata do something...
    }
}
```

## 工作原理
Vine 本身原理非常简单, 将提示词结构化, 通过代码生成提示词, 以及处理用户输入, 避免手写提示词, 从而减少提示词与代码不一致的问题.

TBD

## 高级特性

### 提示词配置

TBD

### 分级模型 & 模型池

TBD

### 序列化方式

TBD

### 可观测性

TBD(尚未合入)

## Java 版本兼容

Vine 基于 Spring AI 构建, 而 Spring AI 基于 Spring framework 6 和 Spring Boot 3 构建, 因此需要在 Java 17 及以上运行.

同时 Vine 也支持 Kotlin, Nvwa 项目就是全项目 Kotlin 写的, Vine 本身也是 Nvwa 项目分离出来的一部分特性, 考虑到 Kotlin 接受范围相对较小, 所以将 Vine 项目分离出来时用 Java 重写了.

## 许可证

Nvwa Vine 使用 Apache License 2.0 许可证. 请参阅 [许可证](./LICENSE) 以获取完整的许可证文本.

