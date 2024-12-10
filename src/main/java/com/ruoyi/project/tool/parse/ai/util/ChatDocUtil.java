package com.ruoyi.project.tool.parse.ai.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.project.tool.parse.ai.dto.UploadResp;
import com.ruoyi.project.tool.parse.ai.dto.chat.ChatMessage;
import com.ruoyi.project.tool.parse.ai.dto.chat.ChatRequest;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Test
 *
 * @author ydwang16
 * @version 2023/09/06 11:45
 **/
public class ChatDocUtil {
    public UploadResp upload(String filePath, String url, String appId, String secret) {
        File file = new File(filePath);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));
        builder.addFormDataPart("fileType", "wiki");
        RequestBody body = builder.build();
        long ts = System.currentTimeMillis() / 1000;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("appId", appId)
                .addHeader("timestamp", String.valueOf(ts))
                .addHeader("signature", ApiAuthUtil.getSignature(appId, secret, ts))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (Objects.equals(response.code(), 200)) {
                String respBody = response.body().string();
                return JSONUtil.toBean(respBody, UploadResp.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String chat(String chatUrl, String fileId, String question, String appId, String secret) {
        ChatMessage message = new ChatMessage();
        message.setRole("user");
        message.setContent(question);
        // 请求内容
        ChatRequest request = ChatRequest.builder()
                .fileIds(Collections.singletonList(fileId))
                .topN(3)
                .messages(Collections.singletonList(message))
                .build();

        // 构造url鉴权
        long ts = System.currentTimeMillis() / 1000;
        String signature = ApiAuthUtil.getSignature(appId, secret, ts);
        String requestUrl = chatUrl + "?" + "appId=" + appId + "&timestamp=" + ts + "&signature=" + signature;
        // ws
        Request wsRequest = (new Request.Builder()).url(requestUrl).build();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        WebSocketResponse response = new WebSocketResponse();
        WebSocket webSocket = okHttpClient.newWebSocket(wsRequest, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                webSocket.close(1002, "websocket finish");
                okHttpClient.connectionPool().evictAll();
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                webSocket.close(1001, "websocket finish");
                okHttpClient.connectionPool().evictAll();
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                JSONObject jsonObject = JSONUtil.parseObj(text);
                String content = jsonObject.getStr("content");
                if (StrUtil.isNotEmpty(content)) {
                    response.buffer.append(content);
                }
                if (Objects.equals(jsonObject.getInt("status"), 2)) {
                    webSocket.close(1000, "websocket finish");
                    okHttpClient.connectionPool().evictAll();
                    response.setComplete(true);
                }
            }
        });
        webSocket.send(JSONUtil.toJsonStr(request));
        return response.response();
    }

    private static class WebSocketResponse{
        @Setter
        @Getter
        public StringBuilder buffer = new StringBuilder();
        @Setter
        private volatile boolean complete = false;

        public String response(){
            while (!complete){
                ThreadUtil.sleep(300);
            }
            return buffer.toString();
        }
    }

}