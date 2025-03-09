package com.hometask.dkp.hsbctransactionmanagement.StressTest;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StressTest {
    private static final int NUMBER_OF_THREADS = 200; // 并发线程数
    private static final int TRANSACTIONS_PER_THREAD = 100; // 每个线程执行的交易次数
    private static final AtomicInteger successfulTransactions = new AtomicInteger(0); // 成功交易计数

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        long startTime = System.currentTimeMillis();
        Random random = new Random();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executor.execute(() -> {
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    for (int j = 0; j < TRANSACTIONS_PER_THREAD; j++) {
                        // 调用转账接口
                        boolean success = createTransactionTest(httpClient, "stressTest" + random.nextInt(), 1001L, 2001L, BigDecimal.valueOf(100.0));
                        if (success) {
                            successfulTransactions.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 等待所有任务完成
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total successful transactions: " + successfulTransactions.get());
        System.out.println("Total time: " + (endTime - startTime) + " ms");
    }

    private static boolean createTransactionTest(CloseableHttpClient httpClient, String transactionId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        try {
            // 创建 POST 请求
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/transaction");
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体
//            "{\"transactionNo\":\"transactionIdTest\",\"amount\":220.0,\"description\":\"description\",\"sourceAccountId\":1001,\"targetAccountId\":2001}";
            String jsonBody = String.format("{\"transactionId\": \"%s\", \"sourceAccountId\": \"%d\", \"targetAccountId\": \"%d\", \"amount\": %.2f}",transactionId, sourceAccountId, targetAccountId, amount);
            httpPost.setEntity(new StringEntity(jsonBody));

            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
//                System.out.println("Response: " + responseBody);

                // 解析响应，判断是否成功
                return responseBody.contains("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
