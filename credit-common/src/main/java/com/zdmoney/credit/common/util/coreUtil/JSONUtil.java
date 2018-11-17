package com.zdmoney.credit.common.util.coreUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by 石俊 on 15-3-19.
 */
public class JSONUtil {

    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;
    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
    }

    /**
     * 默认的处理时间
     *
     * @param jsonText
     * @return
     */
    public static String toJSON(Object jsonText) {
        return JSON.toJSONString(jsonText, mapping);
    }

    /**
     * 自定义时间格式
     *
     * @param dateFormat
     * @param jsonText
     * @return
     */
    public static String toJSON(String dateFormat, String jsonText) {
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        return JSON.toJSONString(jsonText, mapping);
    }

    /**
     * 转化对应VO
     *
     * @param clazz
     * @param jsonText
     * @return
     */
    public static <T> T toJavaObject(String jsonText, Class<T> clazz) {
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        return JSONObject.parseObject(jsonText, clazz);
    }

}
