package unit.com.ciutz.kitikat.controller;

import com.ciutz.kitikat.controller.ResourcesController;
import org.junit.Assert;
import org.junit.Test;

public class ResourcesControllerUnitTest {

    ResourcesController target = new ResourcesController();

    @Test
    public void indexTest() {
        String actual = target.index();

        Assert.assertEquals("index not the same", "index", actual);
    }
}
