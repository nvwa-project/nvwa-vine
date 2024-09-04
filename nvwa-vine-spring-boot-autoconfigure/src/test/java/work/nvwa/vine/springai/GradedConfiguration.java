package work.nvwa.vine.springai;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import work.nvwa.vine.autoconfigure.VineConfiguration;
import work.nvwa.vine.config.EnableVine;

@EnableVine
@SpringBootConfiguration
@Import(VineConfiguration.class)
public class GradedConfiguration {
}
