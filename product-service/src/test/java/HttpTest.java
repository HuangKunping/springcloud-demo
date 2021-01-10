import com.spring.cloud.common.util.ThreadUtil;
import com.spring.cloud.common.vo.ResultMessage;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService threadPool = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 3000; i++) {
            String url = "http://localhost:8001/cr/sleep/1000";
            threadPool.submit(() -> {
                ResultMessage result = restTemplate.getForObject(url, ResultMessage.class);
                System.out.println(result);
            });
        }
        ThreadUtil.sleep(10000);
        threadPool.shutdown();
    }
}
