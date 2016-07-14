import com.loacg.httpclient.utils.HttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: httpClient
 * Author: Sendya <18x@loacg.com>
 * Time: 7/14/2016 9:13 AM
 */
public class Test {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        /*
        String ret = null;
        ret = HttpClient.get("http://static.zueki.com/api.php");

        System.out.println(ret);

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", "sendya");
            params.put("locked", "1");
            ret = HttpClient.post("http://static.zueki.com/api.php", params);
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        try {

            int count = 10;
            ExecutorService executors = Executors.newFixedThreadPool(count);
            CountDownLatch countDownLatch = new CountDownLatch(count);

            for (int i = 0; i < count; i++) {
                executors.execute(new GetRunnable("http://static.zueki.com/api.php", countDownLatch));
            }
            countDownLatch.await();
            executors.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程" + Thread.currentThread().getName() + " 执行完毕, 所有线程已完成");
        }

        long end = System.currentTimeMillis();
        System.out.println("consume -> " + (end - start));
    }

    static class GetRunnable implements Runnable {
        private CountDownLatch countDownLatch;
        private String url;

        public GetRunnable(String url, CountDownLatch countDownLatch) {
            this.url = url;
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", UUID.randomUUID().toString().substring(1,10));
                params.put("locked", "1");
                try {
                    System.out.println(HttpClient.post(url, params));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + " 执行完毕.");
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
