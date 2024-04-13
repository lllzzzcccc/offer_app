package com.bjtu.offer_app.utils.messagehandle;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.bjtu.offer_app.entity.BaseMessage;
import com.bjtu.offer_app.entity.TextMessage;
import org.dom4j.Document;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXml {

    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        return map;
    }

    private static <T extends BaseMessage> String XmlHandleFun(T object){

        //Document对象，后续用他生成xml结构，并调用他的方法进行string类型数据返回
        Document dou = null;
        //用来判断下文的if是否还继续判断，当然这里这个判断是重复的，大家可以在看懂此代码块之后，自己决定是否删除
        boolean isif = true;
        try {
            //开始创建dom结构
            dou = DocumentHelper.createDocument();
            //dou.addElement  创建唯一的全局父节点，根据官方文档的格式，返回的xml'格式基本只有最多3级
            Element root = dou.addElement("xml");
            //root.addElement 在root节点下，创建一个节点，相当于二级节点<ToUserName></ToUserName>
            //attText 为添加二级节点的内容，<ToUserName>内容</ToUserName>
            //补充知识：如果要给此行添加xml属性，使用如下代码-> root.addAttribute("id", "属性");
            //值为<xml id="属性"></xml>
            //获取对象的值，进行字符串拼接
            Element emp = root.addElement("ToUserName").addText("<![CDATA[" + object.getToUserName() + "]]>");
            Element emp1 = root.addElement("FromUserName").addText("<![CDATA[" + object.getFromUserName() + "]]>");
            Element emp2 = root.addElement("CreateTime").addText(String.valueOf(object.getCreateTime()));
            Element emp3 = root.addElement("MsgType").addText("<![CDATA[" + object.getMsgType() + "]]>");
            //判断传入的对象是否是它的实例
            //是的话进行转型，并添加属于自己类的特有的属性！
            if(object instanceof TextMessage && isif){
                TextMessage textMessage = (TextMessage) object;
                Element emp4 = root.addElement("Content").addText("<![CDATA[" + textMessage.getContent() + "]]>");
                isif = false;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        //生成的xml是附带<?xml version="1.0" encoding="UTF-8"?>此行的，我还并没有测试带上返回给微信服务器是否可行，当前没被注释的是去除此行的，如果使用注释的一行则是直接返回生成的，带上此头部的
        int count = "encoding=\"UTF-8\"?".length();
        String result = dou.asXML();
        result = result.substring(result.indexOf("encoding=\"UTF-8\"?") + count + 1);
        return result.trim();
//        return  dou.asXML();
    }


    public static String textMessageToXml(TextMessage textMessage) {
        //真正用来处理的方法，也在本类中
        return XmlHandleFun(textMessage);
    }

}
