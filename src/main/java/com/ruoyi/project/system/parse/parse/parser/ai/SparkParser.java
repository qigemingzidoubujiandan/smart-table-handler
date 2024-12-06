package com.ruoyi.project.system.parse.parse.parser.ai;


import com.ruoyi.project.system.parse.parse.ai.dto.UploadResp;
import com.ruoyi.project.system.parse.parse.ai.util.ChatDocUtil;
import com.ruoyi.project.system.parse.parse.parser.IParser;

public class SparkParser implements IParser<AIRequest, AIResponse> {
    private static final String uploadUrl = "https://chatdoc.xfyun.cn/openapi/fileUpload";
    private static final String fileStatusUrl = "https://chatdoc.xfyun.cn/openapi/fileStatus";
    private static final String chatUrl = "https://chatdoc.xfyun.cn/openapi/chat";
    private static final String appId = "ef46e728";
    private static final String secret = "MTQ4MjViZTYwOWFmZjQ1ZTAyZmNhMWRk";

    @Override
    public AIResponse parse(AIRequest aiRequest) {
        ChatDocUtil chatDocUtil = new ChatDocUtil();
        // 1、上传
        UploadResp uploadResp = chatDocUtil .upload(aiRequest.getFile().getAbsolutePath(), uploadUrl, appId, secret);
        System.out.println("请求sid=" + uploadResp.getSid());
        System.out.println("文件id=" + uploadResp.getData().getFileId());

        // 2、问答，上传文件状态为vectored时才可以问答，文件状态可以调用【文档状态查询】接口查询
        String fileId = uploadResp.getData().getFileId();
        String question = aiRequest.getQuestion();
        String ret = chatDocUtil.chat(chatUrl, fileId, question, appId, secret);
        System.out.println("-----------------------------");
        System.out.println(ret);
        AIResponse response = new AIResponse();
        response.setFileId(fileId);
        response.setResp(ret);
        return  response;
    }
}
