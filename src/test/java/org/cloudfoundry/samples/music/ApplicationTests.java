package org.cloudfoundry.samples.music;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("in-memory")
public class ApplicationTests {

    @Test
    public void contextLoads() {
    }

}