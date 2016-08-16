package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.easyproxy.constants.Const;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class XmlUtil {

    private static SAXReader reader = new SAXReader();
    private Document document = null;
    private Element root = null;
    private JSONObject object = new JSONObject();

    public XmlUtil(InputStream is) {
        try {
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XmlUtil(String xml_path) {
        InputStream is = XmlUtil.class.getResourceAsStream(xml_path);
        try {
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Iterator<Element> elementIterator = root.elementIterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            switch (element.getName()) {
                case Const.PROXY_PASS:
                    JSONArray array = new JSONArray();
                    Iterator<Element> eleIt = element.elementIterator();
                    while (eleIt.hasNext()) {
                        Map<String, String> kv = new HashMap<String, String>();
                        Element ele = eleIt.next();
                        kv.put(Const.PORT, ele.attributeValue(Const.PORT));
                        kv.put(Const.HOST, ele.attributeValue(Const.HOST));
                        kv.put(Const.WEIGHT, ele.attributeValue(Const.WEIGHT));
                        array.add(kv);
                    }
                    object.put(Const.PROXY_PASS, array);
                    break;
                default:
                    Iterator<Attribute> attrIterator = element.attributeIterator();
                    while (attrIterator.hasNext()) {
                        Attribute attr = attrIterator.next();
                        object.put(attr.getName(), attr.getValue());
                    }
                    break;
            }
        }
    }

    public String xml2Json() {
        return object.toJSONString();
    }

    public void listAll() {
        System.out.println(object.toJSONString());
    }
}