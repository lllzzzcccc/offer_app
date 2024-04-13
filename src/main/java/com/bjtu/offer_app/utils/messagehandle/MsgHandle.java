package com.bjtu.offer_app.utils.messagehandle;

import com.bjtu.offer_app.entity.BaseMessage;
import com.bjtu.offer_app.entity.TextMessage;
import com.bjtu.offer_app.utils.code.MessageCode;
import lombok.Data;

import java.util.Map;

@Data
public class MsgHandle {

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
        //目前只支持文字消息回复
        //用枚举获取是什么类型，再进入里面进行具体操作
        if (this.MsgType.equals(MessageCode.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            //这里用到了MsgHelpClass的方法，请看下文的此方法代码
            //参数1：this processMessage对象即可
            //参数2：对应消息处理类即可
            baseMessage = MsgHelpClass.setAttribute(this, TextMessage.class);
            //向下转型，小心，如果报了什么class异常，就是这里的问题。
            TextMessage textMessage = (TextMessage) baseMessage;
            //向文字消息实体类添加私有的属性数据
            textMessage.setContent("这里是测试回复");
            //这里为生成xml数据的类，需要我们提供一个要生成xml数据的实体类，下文放代码
            return ParseXml.textMessageToXml(textMessage);
        }

        return "";
    }

    public String processEvent(Map<String, String> map) throws InstantiationException, IllegalAccessException {
        this.FromUserName = map.get("ToUserName");
        this.ToUserName = map.get("FromUserName");
        this.MsgType = map.get("MsgType");
        this.CreateTime = Long.valueOf(map.get("CreateTime"));
        String event = map.get("Event");
        BaseMessage baseMessage = null;
        if (event.equals(MessageCode.EVENT_TYPE_SUBSCRIBE)) {
            baseMessage = MsgHelpClass.setAttribute(this, TextMessage.class);
            TextMessage textMessage = (TextMessage) baseMessage;
            textMessage.setContent("欢迎关注");
            return ParseXml.textMessageToXml(textMessage);
        }
        return "";
    }


}
