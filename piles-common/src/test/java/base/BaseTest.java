package base;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application.xml")
public class BaseTest {
    @Test
    public void test() {
        int i = 1;
        Assert.assertEquals(i,1);
    }
}
