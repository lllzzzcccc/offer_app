package com.bjtu.offer_app.utils.reresult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class ResultRes {
    public static void response(String data, HttpServletResponse response)  {
        //用此方法进行xml文件的转义和format效果类似，因为上文的代码中，返回的string数据里面的，< >符合已经被转义无法正常传输给微信服务器识别
        data = StringEscapeUtils.unescapeXml(data);
        //如果数据为空，直接返回
        if(StringUtils.isEmpty(data)){
            return;
        }
        try {
            //进行编码规定，返回数据！
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(data);
            printWriter.close();
        }catch (IOException io){
            io.printStackTrace();
        }

    }
    /**
     * @Title: format
     * @Description: 格式化输出xml字符串
     * @param str
     * @return String
     * @throws Exception
     * 这里的代码是网上的，本来要用的，但是也最后没用，是用来解决xml里面的<>符合传输的时候被转义的问题
     */
    public static String format(String str) throws Exception {
        SAXReader reader = new SAXReader();
        // 创建一个串的字符输入流
        StringReader in = new StringReader(str);
        Document doc = reader.read(in);
        // 创建输出格式
        OutputFormat formater = OutputFormat.createPrettyPrint();
        // 设置xml的输出编码
        formater.setEncoding("utf-8");
        // 创建输出(目标)
        StringWriter out = new StringWriter();
        // 创建输出流
        XMLWriter writer = new XMLWriter(out, formater);
        // 输出格式化的串到目标中，执行后。格式化后的串保存在out中。
        writer.write(doc);
        writer.close();
        // 返回格式化后的结果
        return out.toString();
    }
}
