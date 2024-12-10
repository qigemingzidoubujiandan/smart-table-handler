package com.ruoyi.project.tool.parse.ai;


import com.ruoyi.project.tool.parse.ai.dto.UploadResp;
import com.ruoyi.project.tool.parse.ai.util.ChatDocUtil;

/**
 * Test
 * 详细接口文档请查看 <a href="https://chatdoc.xfyun.cn/docs">文档知识库API</a>
 *
 * @author ydwang16
 * @version 2023/09/06 13:59
 **/
public class Main {
    private static final String uploadUrl = "https://chatdoc.xfyun.cn/openapi/fileUpload";
    private static final String fileStatusUrl = "https://chatdoc.xfyun.cn/openapi/fileStatus";
    private static final String chatUrl = "wss://chatdoc.xfyun.cn/openapi/chat";
    private static final String appId = "3a36df7f";
    private static final String secret = "YWUyYWE1MGM3OWFmMjdmMzVjNDU3ZGMy";

    public static void main() {
        ChatDocUtil chatDocUtil = new ChatDocUtil();
        // 1、上传
//        UploadResp uploadResp = chatDocUtil .upload(Main.class.getResource("/").getPath() + "test.txt", uploadUrl, appId, secret);
        UploadResp uploadResp = chatDocUtil .upload("C:\\Users\\chenl\\Desktop\\华安分红.pdf", uploadUrl, appId, secret);
        System.out.println("请求sid=" + uploadResp.getSid());
        System.out.println("文件id=" + uploadResp.getData().getFileId());

        // 2、问答，上传文件状态为vectored时才可以问答，文件状态可以调用【文档状态查询】接口查询
        String fileId = "7c572debdf84405eb39f54bd4fbe76e1";
        String question = "华安年年红债券 A的分红情况，以json形式返回";
        chatDocUtil.chat(chatUrl, fileId, question, appId, secret);
    }
}