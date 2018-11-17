package com.zdmoney.credit.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举工具类
 * @author 00236633
 *
 */
public class EnumUtil {
    
    /**
     * 将枚举类转换为map
     * @param clazz
     * @param keyName
     * @param valueName
     * @return map
     */
    public static List<Map<String,Object>> enumConvertToMap(Class<?> clazz,String keyName,String valueName){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        try {
            Enum[] enums = (Enum[])clazz.getMethod("values").invoke(null, null);
            for(Enum enumTemp : enums){
                String mapKey = "";
                String mapValue = null;
                if(keyName!=null&&!keyName.equals("")){
                    String keyMethodName ="get"+keyName.substring(0, 1).toUpperCase()+keyName.substring(1);
                    mapKey=enumTemp.getClass().getDeclaredMethod(keyMethodName).invoke(enumTemp)+"";
                }else{
                    mapKey=enumTemp.ordinal()+"";
                }
                String valueMethodName = "get"+valueName.substring(0, 1).toUpperCase()+valueName.substring(1);
                mapValue =enumTemp.getClass().getDeclaredMethod(valueMethodName).invoke(enumTemp)+"";
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(mapKey, mapValue);
                list.add(map);
            }
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 将枚举实例转换为map
     * @param clazz
     * @param keyName
     * @param valueName
     * @return map
     */
    public static Map<String,Object> enumInstanceConvertToMap(Enum enumTemp,String keyName,String valueName){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            String mapKey = "";
            String mapValue = null;
            if(keyName!=null&&!keyName.equals("") ){
                String keyMethodName ="get"+keyName.substring(0, 1).toUpperCase()+keyName.substring(1);
                mapKey=enumTemp.getClass().getDeclaredMethod(keyMethodName).invoke(enumTemp)+"";
            }else{
                mapKey=enumTemp.ordinal()+"";
            }
            String valueMethodName = "get"+valueName.substring(0, 1).toUpperCase()+valueName.substring(1);
            mapValue =enumTemp.getClass().getDeclaredMethod(valueMethodName).invoke(enumTemp)+"";
            map.put(mapKey, mapValue);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * 根据key获取枚举的值
     * @param clazz
     * @param keyValue
     * @param keyName
     * @param valueName
     * @return
     */
    public static String getEnumValue(Class<?> clazz,String keyValue, String keyName,String valueName){
        try {
            Enum[] enums = (Enum[])clazz.getMethod("values").invoke(null, null);
            for(Enum enumTemp :enums ){
                String keyValueTamp ;
                if(keyName!=null&&!keyName.equals("")){
                    String keyMethodName ="get"+keyName.substring(0, 1).toUpperCase()+keyName.substring(1);
                    keyValueTamp=enumTemp.getClass().getDeclaredMethod(keyMethodName).invoke(enumTemp)+"";
                }else{
                    keyValueTamp=enumTemp.ordinal()+"";
                }
                
                if(keyValueTamp.equals(keyValue)){
                    String valueMethodName = "get"+valueName.substring(0, 1).toUpperCase()+valueName.substring(1);
                    return enumTemp.getClass().getDeclaredMethod(valueMethodName).invoke(enumTemp)+"";
                }
            }
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 根据key获取枚举的值
     * @param clazz
     * @param keyValue
     * @param keyName
     * @return
     */
    public static <T extends Enum> T getEnum(Class<T> clazz,String keyValue, String keyName){
        try {
            T[] enums = (T[])clazz.getMethod("values").invoke(null, null);
            for(T enumTemp :enums ){
                String keyValueTamp ;
                if(keyName!=null&&!keyName.equals("")){
                    String keyMethodName ="get"+keyName.substring(0, 1).toUpperCase()+keyName.substring(1);
                    keyValueTamp=enumTemp.getClass().getDeclaredMethod(keyMethodName).invoke(enumTemp)+"";
                }else{
                    keyValueTamp=enumTemp.ordinal()+"";
                }
                
                if(keyValueTamp.equals(keyValue)){
                    return enumTemp;
                }
            }
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 枚举类型转换为List集合
     * @param clazz
     * @param key
     * @return
     */
    public static List<Object> enumConvertToMap(Class<?> clazz, String key) {
        List<Object> valueList = new ArrayList<Object>();
        if (null == key || "".equals(key)) {
            key = "value";
        }
        try {
            Enum[] enums = (Enum[]) clazz.getMethod("values").invoke(null,null);
            for (Enum enumTemp : enums) {
                Field field = enumTemp.getClass().getDeclaredField(key);
                field.setAccessible(true);
                valueList.add(field.get(enumTemp));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return valueList;
    }
}
