package work.nvwa.vine.springai.chat;

import org.springframework.boot.test.context.SpringBootTest;
import work.nvwa.vine.springai.GradedConfiguration;


@SpringBootTest(classes = GradedConfiguration.class, properties = "spring.profiles.active=graded")
public abstract class AbstractGradedTest {
}
