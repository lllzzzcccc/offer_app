package com.bjtu.offer_app.controller;

import com.bjtu.offer_app.utils.SignUtil;
import com.bjtu.offer_app.utils.messagehandle.ParseXml;
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
