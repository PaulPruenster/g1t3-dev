
package at.qe.skeleton.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringBootTest
@WebAppConfiguration
@Sql("classpath:sql/UserxRepositoryTest.sql")
class UserxRepositoryTest {
    // TODO
}
