package org.example.backend.XunFei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 讯飞星火大模型API调用类
 */
public class BigModel extends WebSocketListener {
    // 使用最新的Spark 4.0 Ultra版本
    public static final String hostUrl = "wss://spark-api.xf-yun.com/v3.5/chat";
    public static final String domain = "generalv3.5";
    public static final String appid = "8bc1f56b";
    public static final String apiSecret = "Yzg0ZmUxNTdhODE4YjUwZDk5N2FmMzg2";
    public static final String apiKey = "a289832fdd24dd7b8119fea2a91e43ca";

    private List<RoleContent> historyList = new ArrayList<>(); // 对话历史存储集合
    private String totalAnswer = ""; // 大模型的答案汇总
    private String newQuestion = ""; // 新的问题
    private static final Gson gson = new Gson();
    private CompletableFuture<String> responseFuture;

    // 个性化参数
    private final String userId;
    private Boolean wsCloseFlag;
    private CountDownLatch latch;
    private OkHttpClient client;

    // 构造函数
    public BigModel(String userId) {
        this.userId = userId;
        this.wsCloseFlag = false;
        this.client = new OkHttpClient.Builder().build();
    }

    /**
     * 发送问题到大模型并获取回答
     * @param question 用户问题
     * @return 大模型的回答
     */
    public String chat(String question) throws Exception {
        newQuestion = question;
        totalAnswer = "";
        wsCloseFlag = false;
        latch = new CountDownLatch(1);
        responseFuture = new CompletableFuture<>();
        
        try {
            // 构建鉴权url
            String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();
            
            // 创建WebSocket连接
            WebSocket webSocket = client.newWebSocket(request, this);
            
            // 等待响应完成
            latch.await();
            return totalAnswer;
            
        } catch (Exception e) {
            throw new Exception("与大模型通信出错: " + e.getMessage(), e);
        }
    }

    /**
     * 异步发送问题到大模型
     * @param question 用户问题
     * @return CompletableFuture包含大模型的回答
     */
    public CompletableFuture<String> chatAsync(String question) {
        newQuestion = question;
            totalAnswer = "";
        wsCloseFlag = false;
            latch = new CountDownLatch(1);
        responseFuture = new CompletableFuture<>();
            
            try {
                // 构建鉴权url
                String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
                String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
                Request request = new Request.Builder().url(url).build();
                
                // 创建WebSocket连接
            WebSocket webSocket = client.newWebSocket(request, this);
            } catch (Exception e) {
            responseFuture.completeExceptionally(new Exception("与大模型通信出错: " + e.getMessage(), e));
            }
        
        return responseFuture;
    }

    /**
     * 清除历史对话记录
     */
    public void clearHistory() {
        historyList.clear();
    }
    
    /**
     * 检查是否可以添加历史记录并进行管理
     */
    private boolean canAddHistory() {
        int historyLength = historyList.stream()
                .mapToInt(roleContent -> roleContent.content.length())
                .sum();
        
        if (historyLength > 12000) {
            historyList.subList(0, 5).clear();
            return false;
        }
        return true;
    }

    // 发送消息的线程类
    class MyThread extends Thread {
        private final okhttp3.WebSocket webSocket;

        public MyThread(okhttp3.WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        public void run() {
            try {
                JSONObject requestJson = new JSONObject();

                // header参数
                JSONObject header = new JSONObject();
                header.put("app_id", appid);
                header.put("uid", UUID.randomUUID().toString().substring(0, 10));

                // parameter参数
                JSONObject parameter = new JSONObject();
                JSONObject chat = new JSONObject();
                chat.put("domain", domain);
                chat.put("temperature", 0.5);
                chat.put("max_tokens", 4096);
                parameter.put("chat", chat);

                // payload参数
                JSONObject payload = new JSONObject();
                JSONObject message = new JSONObject();
                JSONArray text = new JSONArray();

                // 添加历史对话
                if (!historyList.isEmpty()) {
                    text.addAll(historyList.stream()
                            .map(JSON::toJSON)
                            .toList());
                }

                // 添加新问题
                RoleContent roleContent = new RoleContent();
                roleContent.role = "user";
                roleContent.content = newQuestion;
                text.add(JSON.toJSON(roleContent));
                historyList.add(roleContent);

                message.put("text", text);
                payload.put("message", message);

                requestJson.put("header", header);
                requestJson.put("parameter", parameter);
                requestJson.put("payload", payload);

                webSocket.send(requestJson.toString());

                while (!wsCloseFlag) {
                    Thread.sleep(200);
                }
                webSocket.close(1000, "");
            } catch (Exception e) {
                responseFuture.completeExceptionally(e);
            }
        }
    }

    @Override
    public void onOpen(okhttp3.WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        MyThread myThread = new MyThread(webSocket);
        myThread.start();
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, String text) {
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
        if (myJsonParse.header.code != 0) {
            responseFuture.completeExceptionally(
                new RuntimeException("大模型返回错误，错误码: " + myJsonParse.header.code + 
                                    ", sid: " + myJsonParse.header.sid));
            webSocket.close(1000, "");
            latch.countDown();
            return;
        }

        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            totalAnswer += temp.content;
        }

        if (myJsonParse.header.status == 2) {
            RoleContent roleContent = new RoleContent();
            roleContent.setRole("assistant");
            roleContent.setContent(totalAnswer);
            
            if (!canAddHistory()) {
                historyList.remove(0);
            }
            historyList.add(roleContent);
            
            wsCloseFlag = true;
            responseFuture.complete(totalAnswer);
            latch.countDown();
        }
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        String errorMsg;
        try {
            if (response != null) {
                int code = response.code();
                String body = "";
                if (response.body() != null) {
                    body = response.body().string();
                }
                errorMsg = "连接失败，响应码: " + code + ", 响应内容: " + body;
            } else {
                errorMsg = "连接失败: " + t.getMessage();
            }
        } catch (IOException e) {
            errorMsg = "读取响应失败: " + e.getMessage();
        }
        
        responseFuture.completeExceptionally(new RuntimeException(errorMsg));
        latch.countDown();
    }

    /**
     * 鉴权方法
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl.replace("wss://", "https://"));
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);

        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrl.toString();
    }

    // 返回的json结果拆解
    static class JsonParse {
        Header header;
        Payload payload;
    }

    static class Header {
        int code;
        int status;
        String sid;
    }

    static class Payload {
        Choices choices;
    }

    static class Choices {
        List<Text> text;
    }

    static class Text {
        String role;
        String content;
    }

    public static class RoleContent {
        String role;
        String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}


