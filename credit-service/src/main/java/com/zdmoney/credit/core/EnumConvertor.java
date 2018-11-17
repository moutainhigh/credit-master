package com.zdmoney.credit.core;

import org.apache.commons.collections.MapUtils;

import com.zdmoney.credit.core.FieldNameMapper.FieldNameType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 *  枚举转换，目前识别的调用场景包括
 *  征审-->账务，实时推送接口和批量文件接口，codeToText
 *  账务-->催收，批量文件接口，textToCode
 *
 * Created by 00217484 on 2015/3/27.
 */
public class EnumConvertor {

    static Map emumMapCodeToText ; // 两层map，fieldName，code，title; 要不要2个方向各一个map；field_name以dict_name为准
    static Map emumMapTextToCode ; // 两层map，fieldName，code，title; 要不要2个方向各一个map

    static Set<String> needConvertFieldNames ;

    public static void init(Map emumMapCodeToText){
        EnumConvertor.emumMapCodeToText = emumMapCodeToText;
//        EnumConvertor.emumMapTextToCode = MapUtils.invertMap(EnumConvertor.emumMapCodeToText);

        emumMapTextToCode = new HashMap();
        for(Object o : emumMapCodeToText.entrySet()){
            Map.Entry<String, Map> entry = (Map.Entry<String, Map>)o;
            emumMapTextToCode.put(entry.getKey(), MapUtils.invertMap(entry.getValue()));
        }

        EnumConvertor.needConvertFieldNames = EnumConvertor.emumMapCodeToText.keySet();
    }

    // 如果不存在code、title的映射关系，这里的处理策略是报错还是忽视？ --暂时选择忽视

    public static void convertToText(Map m, FieldNameType fieldNameType) {
        for(Object o : m.entrySet()){
            Map.Entry e = (Map.Entry) o;
            String fieldName = (String) e.getKey();
            String dictSFieldName = convertFieldName(fieldName,fieldNameType);
            if(isNeedConvert(dictSFieldName)) {
                String code = (String) e.getValue();
                String result = convertToText(fieldName, code, fieldNameType);
                m.put(fieldName, result);
            }
        }
    }

    public static void convertToCode(Map m, FieldNameType fieldNameType)throws Exception{
        for(Object o : m.entrySet()){
            Map.Entry e = (Map.Entry) o;
            String fieldName = (String) e.getKey();
            String dictSFieldName = convertFieldName(fieldName,fieldNameType);
            if(isNeedConvert(dictSFieldName)) {
                String text = (String) e.getValue();
                String result = convertToCode(fieldName, text, fieldNameType);
                m.put(fieldName, result);
            }
        }
    }
    public static boolean isNeedConvert(String dictSFieldName){
        return needConvertFieldNames.contains(dictSFieldName);
    }

    public static boolean isNeedConvert(String fieldName, FieldNameType fieldNameType){
        String dictSFieldName = convertFieldName(fieldName,fieldNameType);
        return needConvertFieldNames.contains(dictSFieldName);
    }

    public static String autoConvert(String source, String fieldName, FieldNameType fieldNameType) {

        String result;
        if (fieldNameType.equals(FieldNameType.core4java) || fieldNameType.equals(FieldNameType.core4json)) {
            result = EnumConvertor.convertToCode(fieldName, source, fieldNameType);
        } else {
            result = EnumConvertor.convertToText(fieldName, source, fieldNameType);
        }
        return result;
    }

    public static String convertToText(String fieldName, String code, FieldNameType fieldNameType){

        if(isNeedConvert(fieldName,fieldNameType) == false)
            return code;

        fieldName = convertFieldName(fieldName,fieldNameType);
        Map codeToTextMap =  ((Map)emumMapCodeToText.get(fieldName));
        String text = null;
        if(codeToTextMap!=null){
            text = (String)codeToTextMap.get(code);
        }
        return text;
    }

    public static String convertToCode(String fieldName, String text, FieldNameType fieldNameType){

        if(isNeedConvert(fieldName,fieldNameType) == false)
            return text;

        fieldName = convertFieldName(fieldName,fieldNameType);
        Map textToCodeMap =  ((Map)emumMapTextToCode.get(fieldName));
        String code = null;
        if(textToCodeMap!=null){
            code = (String)textToCodeMap.get(text);
        }
        return code;
    }

    private static String convertFieldName(String source, FieldNameType fieldNameType){
        return FieldNameMapper.getInstance().convertFieldName(source,fieldNameType,FieldNameType.dict4sql);
    }


//    private static Map getReverseMap(Map m) {
//        try {
//            Map reverseMap = m.getClass().newInstance();
//            Iterator it = m.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pairs = (Map.Entry) it.next();
//                reverseMap.put(pairs.getValue(), pairs.getKey());
//            }
//
//            return reverseMap;
//        } catch (Exception e) {
//        }
//        return null;
//    }

    public static void main(String[] args){

    }

    private static void test(){

    }

}
