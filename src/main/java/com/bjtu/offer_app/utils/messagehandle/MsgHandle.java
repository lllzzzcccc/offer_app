package com.bjtu.offer_app.utils.messagehandle;

import com.bjtu.offer_app.entity.BaseMessage;
import com.bjtu.offer_app.entity.Offer;
import com.bjtu.offer_app.entity.TextMessage;
import com.bjtu.offer_app.utils.code.MessageCode;
import lombok.Data;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
public class MsgHandle {

    public static final String BASE_URL = "http://localhost:80/offer/";
    public static final String WELCOME = "欢迎关注本公众号，本公众号为您提供最新的招聘信息，祝您找到满意的工作！\n";
    public static final String HELP =
            "本系统支持offer信息的增删改查功能，您可以输入以下命令进行操作：\n"+
            "1. “增 (公司名称) (职位名称) (薪水数额)”以增加一份offer\n"+
            "2. “删 (offer_id)”以删除一份offer\n"+
            "3. “查 (offer_id)”以获取一份offer\n"+
            "4. “全”以获取所有offer信息\n"+
            "5. “找 (公司名称)”以部分检索offer信息\n"+
            "6. “改 (offer_id) [公司=(公司名称)] [职位=(职位名称)] [薪水=(薪水数额)]”\n"+
            "7. “换 (offer_id) (公司名称) (职位名称) (薪水数额)”\n"+
            "8. “帮助”以获取帮助信息\n";

    /**
     * 发送方账号（一个openId）
     */
    private String FromUserName;

    /**
     * 开发者微信号
     */
    private String ToUserName;

    /**
     * 消息创建时间
     */
    private long CreateTime;

    /**
     * 消息类型：
     * 文本：text
     * 图片：image
     * 语音：voice
     * 视频：video
     * 小视频：shortvideo
     * 地理位置：location
     * 链接：link
     */
    private String MsgType;

    /**
     * 消息id，64位整数
     */
    private long MsgId;

    public String processMessage(Map<String, String> map) throws InstantiationException, IllegalAccessException{
        //首先对自己的私有属性进行赋值，接着创建基类实体对象，
        this.FromUserName = map.get("ToUserName");  //!!!!!这里是调换的
        //特别说明这里，看上面两个图中，我们会发现关于ToUserName的说明是收到openId，所以这里是调换的！
        this.ToUserName = map.get("FromUserName");
        this.MsgType = map.get("MsgType");
        this.CreateTime = Long.valueOf(map.get("CreateTime"));
        this.MsgId = Long.valueOf(map.get("MsgId"));
        BaseMessage baseMessage = null;
        baseMessage = MsgHelpClass.setAttribute(this, TextMessage.class);
        TextMessage textMessage = (TextMessage) baseMessage;

//        初始化RestTemplate
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        if (this.MsgType.equals(MessageCode.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            String content = map.get("Content");
            String[] words = content.split(" ");
            if(content.startsWith("增") && words.length == 4){

                Map<String,Object> requestBody = new HashMap<>();
                requestBody.put("enterprise",words[1]);
                requestBody.put("job",words[2]);
                requestBody.put("salary",words[3]);
                String data = restTemplate.postForObject(BASE_URL, requestBody, String.class);
                return ParseXml.textMessageToXml(textMessage.setContent(data));
            }else if(content.startsWith("删") && words.length == 2){
                restTemplate.delete(BASE_URL+words[1]);
                return ParseXml.textMessageToXml(textMessage.setContent("删除成功！"));
            }else if(content.startsWith("查") && words.length == 2){
                Offer data = restTemplate.getForObject(BASE_URL+words[1], Offer.class);
                StringBuilder sb = new StringBuilder();
                sb.append("offer_id：").append(data.getOfferId()).append("\n")
                        .append("公司名称：").append(data.getEnterprise()).append("\n")
                        .append("职位名称：").append(data.getJob()).append("\n")
                        .append("薪水：").append(data.getSalary()).append("\n");
                return ParseXml.textMessageToXml(textMessage.setContent(sb.toString()));
            }else if(content.startsWith("全") && words.length == 1){
                Offer[] data = restTemplate.getForObject(BASE_URL+"list", Offer[].class);
                StringBuilder sb = new StringBuilder();
                if(data == null || data.length == 0){
                    return ParseXml.textMessageToXml(textMessage.setContent("暂无offer信息！"));
                }
                for(Offer offer : data){
                    sb.append("offer_id：").append(offer.getOfferId()).append("\n")
                            .append("公司名称：").append(offer.getEnterprise()).append("\n")
                            .append("职位名称：").append(offer.getJob()).append("\n")
                            .append("薪水：").append(offer.getSalary()).append("\n\n");
                }
                return ParseXml.textMessageToXml(textMessage.setContent(sb.toString()));
            }else if(content.startsWith("找") && words.length == 2){
                Offer[] data = restTemplate.getForObject(BASE_URL+"find/"+words[1], Offer[].class);
                StringBuilder sb = new StringBuilder();
                if(data == null || data.length == 0){
                    return ParseXml.textMessageToXml(textMessage.setContent("未找到相关信息！"));
                }
                for(Offer offer : data){
                    sb.append("offer_id：").append(offer.getOfferId()).append("\n")
                            .append("公司名称：").append(offer.getEnterprise()).append("\n")
                            .append("职位名称：").append(offer.getJob()).append("\n")
                            .append("薪水：").append(offer.getSalary()).append("\n");
                }
                return ParseXml.textMessageToXml(textMessage.setContent(sb.toString()));
            }else if(content.startsWith("改") && words.length <= 5 && words.length >= 2){
                Map<String,Object> requestBody = new HashMap<>();
                requestBody.put("offerId",words[1]);
                for(int i = 2; i < words.length; i++){
                    String[] temp = words[i].split("=");
                    requestBody.put(temp[0],temp[1]);
                }
                restTemplate.put(BASE_URL+"update", requestBody, String.class);
                return ParseXml.textMessageToXml(textMessage.setContent("修改成功！"));
            }else if(content.startsWith("换") && words.length == 5){
                Map<String,Object> requestBody = new HashMap<>();
                requestBody.put("offerId",words[1]);
                requestBody.put("enterprise",words[2]);
                requestBody.put("job",words[3]);
                requestBody.put("salary",words[4]);
                restTemplate.put(BASE_URL+"replace", requestBody, String.class);
                return ParseXml.textMessageToXml(textMessage.setContent("替换成功！"));
            }else if(content.startsWith("帮助") && words.length == 1){
                return ParseXml.textMessageToXml(textMessage.setContent(HELP));
            }else {
                return ParseXml.textMessageToXml(textMessage.setContent("输入有误，请重新输入！"));
            }
        }
        return "";
    }

    public String processEvent(Map<String, String> map) throws InstantiationException, IllegalAccessException {
        this.FromUserName = map.get("ToUserName");
        this.ToUserName = map.get("FromUserName");
        this.MsgType = MessageCode.RESP_MESSAGE_TYPE_TEXT;
        this.CreateTime = Long.valueOf(map.get("CreateTime"));
        String event = map.get("Event");
        BaseMessage baseMessage = null;
        if (event.equals(MessageCode.EVENT_TYPE_SUBSCRIBE)) {
            baseMessage = MsgHelpClass.setAttribute(this, TextMessage.class);
            TextMessage textMessage = (TextMessage) baseMessage;
            textMessage.setContent(WELCOME+HELP);
            return ParseXml.textMessageToXml(textMessage);
        }
        return "";
    }


}
