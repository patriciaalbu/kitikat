package unit.com.ciutz.kitikat.configuration;

import com.ciutz.kitikat.configuration.SpringFoxConfig;
import org.junit.Assert;
import org.junit.Test;
import springfox.documentation.spring.web.plugins.Docket;

public class SpringFoxConfigUnitTest {
    SpringFoxConfig target = new SpringFoxConfig();

    @Test
    public void apiReturnsNoNullValueTest () {
        Docket actual = target.api();
        Assert.assertNotNull("Docket should not be null", actual);
    }
}
