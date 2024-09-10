<div align="center">

<h1>Nvwa Vine 🌿</h1>

Makes accessing AI simpler and more efficient

[简体中文](./README.zh-CN.md) · English

</div>

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/work.nvwa/nvwa-vine-bom/badge.png)](https://search.maven.org/artifact/work.nvwa/nvwa-vine-bom/)

- ⚡️ Simple and easy to use, efficient
- ⚙️️ Code-driven, easy to manage
- 💰 Tiered model, cost-saving
- 👁 Observability, easy to debug


## Why We Chose the Name `Vine`
this project is part of the `Nvwa` project, representing `Nuwa's vine`.

In ancient Chinese mythology, Nvwa is the goddess of creation. It is said that she used the mud from the Yellow River to craft humans in her image, breathing life into them with her divine powers.

However, the vast land of world the process slow, and `Nvwa` grew exhausted. She then pulled a dried vine from a cliff, dipped it in the mud, and flung it across the ground. Wherever the mud splattered, humans sprang up. Before long, the earth was filled with human beings.

Just as the vine greatly boosted `Nvwa`’s efficiency in creating humans, the `Vine` project aims to help developers build AI interaction applications more efficiently.

## Why Use Vine
When building AI interaction applications, we often need to handle user input, generate corresponding outputs, and process the results.

For simple tasks, we can use prompt templates to generate the needed prompts. However, in more complex scenarios, we may need to build detailed prompts based on inputs, context, conversations, and information retrieved through RAG (Retrieval-Augmented Generation). Additionally, we might need complex data structures to capture the AI's output.

The more complex the business, the more varied the user input scenarios become. As the business grows, we need to continually maintain the prompts and their handling logic.

For example, if we adjust the input or output structure of a prompt, we not only need to update the related code but also the prompt that describes the structure and any few-shot data tied to it.

Since prompts are often separate from the code, it's easy for them to become out of sync. Updating prompts can lead to errors, and managing complex logic becomes harder, just like when documentation doesn't match the code. This is where the need for an AI prompt framework arises.

With over a decade of architectural experience, I firmly believe that code is the best form of documentation. Similarly, when tasks are broken down into atomic units, code can also serve as the best prompt for structured AI interaction scenarios.

Vine provides a code-driven approach that helps generate prompts and handle user input through code, significantly reducing discrepancies between prompts and code. This also simplifies the process of building prompts.

With as little extra code as possible, Vine helps you build AI interactions faster and manage prompt engineering more effectively.

## Quick Start

english:
Add dependencies, Vine can be found through the [Maven Central Repository](https://central.sonatype.com/artifact/work.nvwa/nvwa-vine-bom).

Of course, you can also clone the code and build it yourself.


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

Refer to the usage of [Spring AI](https://docs.spring.io/spring-ai/reference/getting-started.html) to complete the relevant configuration of Chat Model.

For example, using OpenAI, configure it through `application.yml`:

```yaml
spring:
  ai:
    openai:
      apiKey: <your-api-key>
```

Create an interface to access AI, and add the `@VineService` annotation. The method parameter is the variable part of the user input, and the return value is the structured output of AI.

If the business scenario or return structure is more complex, you can use the corresponding annotation to add a description.


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

In the place where you need to call AI, inject the interface and call the corresponding method.

```java

@Component
public class ChatServiceTest {
    private final SqlAnalyzeService sqlAnalyzeService;

    public ChatServiceTest(SqlAnalyzeService sqlAnalyzeService) {
        this.sqlAnalyzeService = sqlAnalyzeService;
    }

    public void businessMethod() {
        // needAnalyzeSql is the variable part of user input
        // example: "select b,c,a from test_table where a = 1 and b like 'Zhang%' and c <= 3 order by created_time desc"
        QuerySqlMetadata sqlMetadata = sqlAnalyzeService.analyzeSql(needAnalyzeSql);
        // use sqlMetadata do something...
    }
}
```

## How It Works
`Vine`’s concept is straightforward. It helps structure prompts and generate them through code, handling user input while avoiding the need to manually write prompts. This reduces issues where prompts and code may not match up.

TBD

## Advanced Features
### Prompt Configuration
TBD

### Graded Models & Model Pool
TBD

### Serialization Type
TBD

### Observability
TBD (not yet merged)

## Java Version Compatibility
`Vine` is built on `Spring AI`, which uses `Spring Framework` 6 and `Spring Boot` 3, so it requires `Java` 17 or higher to run.

`Vine` also supports Kotlin. `Vine` is a part of The `Nvwa` project, is written entirely in `Kotlin`.
However, since `Kotlin` has a smaller user base, we rewrote Vine in `Java` when separating it from `Nvwa`.

## License
 `Vine` is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE) for the full license text.
