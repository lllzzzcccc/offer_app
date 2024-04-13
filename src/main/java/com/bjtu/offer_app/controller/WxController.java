package com.bjtu.offer_app.controller;

import com.bjtu.offer_app.entity.BaseMessage;
import com.bjtu.offer_app.entity.TextMessage;
import com.bjtu.offer_app.utils.SignUtil;
import com.bjtu.offer_app.utils.code.MessageCode;
import com.bjtu.offer_app.utils.messagehandle.MsgHandle;
import com.bjtu.offer_app.utils.messagehandle.MsgHelpClass;
import com.bjtu.offer_app.utils.messagehandle.ParseXml;
import com.bjtu.offer_app.utils.reresult.ResultRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/springSecurity/test")
public class WxController {

    @GetMapping("/weixin")
    public String weixin(HttpServletRequest request, HttpServletResponse response){

        String echostr = null;
        //token验证代码段
        try{
            //这里对应的文档里面的几个参数，如果不清楚，请查看文档
            if (StringUtils.isNotBlank(request.getParameter("signature"))) {
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                echostr = request.getParameter("echostr");
                if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                    response.getOutputStream().println(echostr);
                    return echostr;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return echostr;
    }

    @PostMapping("/weixin")
    public void message(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, String> paramMap = ParseXml.parseXml(request);
            String msgType = paramMap.get("MsgType");
            if (MessageCode.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
                //事件消息
                //event事件类型
//                String event = paramMap.get("Event");
//                if (MessageCode.EVENT_TYPE_SUBSCRIBE.equals(event)) {
//                    System.out.println("关注事件");
//                    //关注事件
//                    //回复欢迎消息
//                    String content = "欢迎关注";
//                    BaseMessage baseMessage = MsgHelpClass.setAttribute(this,TextMessage.class);
//                    TextMessage textMessage = new TextMessage();
//                    textMessage.setContent(content);
//                    ResultRes.response(ParseXml.textMessageToXml(textMessage),response);
//                }
                MsgHandle msgHandle = new MsgHandle();
                ResultRes.response(msgHandle.processEvent(paramMap),response);
            } else {
                MsgHandle msgHandle = new MsgHandle();
                ResultRes.response(msgHandle.processMessage(paramMap),response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
