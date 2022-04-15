import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;


/**
 * @Author vansn
 * @Date 2022/1/28 上午10:50
 * @Version 1.0
 * @Description
 */
@SpringBootTest
public class SpringBootTest1 {

    private static Environment env;

    @Autowired
    public void init(Environment env){
        this.env = env;

    }

    @Test
    public void contextLoads(){
        System.out.println(SpringBootTest1.env.getProperty("user.name"));
    }
}
