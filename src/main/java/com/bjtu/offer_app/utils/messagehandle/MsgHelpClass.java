package com.bjtu.offer_app.utils.messagehandle;

import com.bjtu.offer_app.entity.BaseMessage;

import java.util.Date;

public class MsgHelpClass {
    //其实这里多加一个这个方法，而不是在processmessage里面直接进行转型，是因为会报转型错误的异常，用instanceof这里不太管用。
    //方法讲解：
    //规定第一个参数必须为 MsgHandle对象，用他的私有属性给实体基类进行赋值
    //规定第二个参数必须为继承自BaseMessage基类的子类，用来向下转型
    public static <E extends BaseMessage>E setAttribute(MsgHandle msgHandle, Class<E> eClass) throws IllegalAccessException, InstantiationException {
        //newInstance 获取对象，相当于new 对象
        BaseMessage baseMessage = eClass.newInstance();
        baseMessage.setCreateTime(new Date().getTime());
        baseMessage.setFromUserName(msgHandle.getFromUserName());
        baseMessage.setMsgId(msgHandle.getMsgId());
        baseMessage.setToUserName(msgHandle.getToUserName());
        baseMessage.setMsgType(msgHandle.getMsgType());

        return (E) baseMessage;
    }
}
