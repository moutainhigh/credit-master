package com.zdmoney.credit.core;

import java.util.Map;
import java.util.Set;

import com.zdmoney.credit.common.util.DateConvertUtils;
import com.zdmoney.credit.core.FieldNameMapper.FieldNameType;

public class FieldValueConvertor {
	
	Set<String> needConvertDateFieldNames ;
    Map<String,String> needConvertId2InstanceFieldNames;

    private static FieldValueConvertor instance = new FieldValueConvertor();
    
    public static FieldValueConvertor getInstance() {
    	return instance;
    }
    
    public void initDateFieldNames(Set<String> needConvertDateFieldNames, Map<String, String> needConvertId2InstanceFieldNames){
        this.needConvertDateFieldNames = needConvertDateFieldNames;
        this.needConvertId2InstanceFieldNames = needConvertId2InstanceFieldNames;
    }
    
	/**
     * 处理枚举code<->text的转换
     * 处理date的String->Date转换（format:yyyy-MM-dd hhmmss.SSS）
     * 处理实体id -> 实体实例对象的转换
     *
     * @param source
     * @param fieldName
     * @param fieldNameType
     * @return
     */
    public Object convert(Object source, String fieldName, FieldNameType fieldNameType){
        if(isNeedConvert(fieldName,fieldNameType) == false)
            return source;

        Object result ;
        result = EnumConvertor.autoConvert((String)source,  fieldName, fieldNameType);
        result = autoConvertToDate((String)result, fieldName, fieldNameType);
        //id->instance的转换在这里处理
        
        /*if(result instanceof Date){

        }else{
            result = autoConvertIdToInstance((String) result, fieldName, fieldNameType);
        }*/

        // 字符串拼接的转换不在这里处理？恐怕处理不了，需要在外层调用处定制处理逻辑。
        return result;
    }
    
    private Object convertStringToDate(String source, String fieldName, FieldNameType fieldNameType){
        if(isNeedConvertStringToDate(fieldName) == false)
            return source;
        return DateConvertUtils.parse(source);
    }
    
    private Object autoConvertToDate(String source, String fieldName, FieldNameType fieldNameType){
        if (fieldNameType.equals(FieldNameType.aps4json)) {
            return convertStringToDate(source, fieldName, fieldNameType);
        } else {
            return source;
        }
    }
    
    private boolean isNeedConvertStringToDate(String fieldName){
        return needConvertDateFieldNames.contains(fieldName);
    }
    
    private boolean isNeedConvert(String fieldName, FieldNameType fieldNameType){
        if(fieldNameType.equals(FieldNameType.aps4json)) {
            return EnumConvertor.isNeedConvert(fieldName, FieldNameType.aps4json) || needConvertDateFieldNames.contains(fieldName) || needConvertId2InstanceFieldNames.keySet().contains(fieldName);
        } else {
        	return false;
            //return EnumConvertor.isNeedConvert(fieldName, FieldNameType.fieldNameType); //批量导出给催收，java只负责转换枚举
        }
    }
}
