import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        plugin={"pretty","io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm",
                "json:target/cucumber.json",
                "html:test-output"
        },
        features = "src/test/resources/features",
        glue = {"BaseSteps", "Hooks"},
        tags = "@TEST"
)

public class RunnerTest {
}
