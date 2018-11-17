package com.zdmoney.credit.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import com.zdmoney.credit.system.domain.SysFieldMapper;

public class FieldNameMapper {
	private Map<String,String> coreJ2dictS = new HashMap<String, String>();
    private Map<String,String> apsJ2dictS = new HashMap<String, String>();
    private Map<String,String> ctsS2dictS = new HashMap<String, String>();
    private Map<String,String> apsJson2coreJson = new HashMap<String, String>();
    private Map<String,String> apsJson2dictS = new HashMap<String, String>();
    private Map<String,String> coreJ2apsJ = new HashMap<String, String>();
    private Map<String,String> coreJ2ctsS = new HashMap<String, String>();
    private Map<String,String> apsJ2coreJ = new HashMap<String, String>();
    private Map<String,String> ctsS2coreJ = new HashMap<String, String>();

    //singleton
    private static FieldNameMapper instance = new FieldNameMapper();

    public static FieldNameMapper getInstance() {
        return instance;
    }

    public static enum FieldNameType{core4java, aps4java, cts4sql, dict4sql, aps4json, core4json}

    public String convertFieldName(String source , FieldNameType sType, FieldNameType tType) {
        Map<String,String> m = getMapper(sType,tType);
        String result = m ==null ? source :m.get(source);
        return result == null ? source : result;
    }

    public String convertFieldName2(String source , FieldNameType sType, FieldNameType tType) {
        Map<String,String> m = getMapper(sType,tType);
        if(m==null)
            throw new RuntimeException(String.format("无法找到字段名转换字典 {0} to {1}",sType,tType));
        String result =m.get(source);
        if(result==null)
            throw new RuntimeException(String.format("无法找到字段名，转换字典 {0} to {1}，原始字段名{2}",sType,tType,source));
        return result ;
    }


    @SuppressWarnings("unchecked")
	public void init(Collection<?> fieldNamesCollection){
        clearAllMap();
        for(Object o : fieldNamesCollection) {
            SysFieldMapper names  = (SysFieldMapper)o;
            addPairs(names.getCoreNameJava(), names.getApsNameJava(), names.getCtsNameSql(), names.getDictName(), names.getApsNameJson(), names.getCoreNameJson());
        }
        apsJ2coreJ = MapUtils.invertMap(coreJ2apsJ);
        ctsS2coreJ = MapUtils.invertMap(coreJ2ctsS);
    }

	@SuppressWarnings("unchecked")
	public void initByArray(List<?> fieldNamesList){
        for(Object o : fieldNamesList) {
            String[] names = (String[]) o;
            addPairs(names[0], names[1], names[2], names[3], names[4], names[5]);
        }
        apsJ2coreJ = MapUtils.invertMap(coreJ2apsJ);
        ctsS2coreJ = MapUtils.invertMap(coreJ2ctsS);
    }

    private void clearAllMap(){
        coreJ2dictS.clear();
        apsJ2dictS.clear();
        ctsS2dictS.clear();
        apsJson2dictS.clear();
        apsJson2coreJson.clear();
        coreJ2apsJ.clear();
        coreJ2ctsS.clear();
        apsJ2coreJ.clear();
        ctsS2coreJ.clear();
    }

    private void addPairs(String coreNameJava, String apsNameJava, String ctsNameSql, String dictNameSql, String apsNameJson, String coreNameJson){
        coreJ2dictS.put(coreNameJava,dictNameSql);
        apsJ2dictS.put(apsNameJava,dictNameSql);
        ctsS2dictS.put(ctsNameSql,dictNameSql);
        apsJson2dictS.put(apsNameJson,dictNameSql);
        apsJson2coreJson.put(apsNameJson,coreNameJson);
        coreJ2apsJ.put(coreNameJava,apsNameJava);
        coreJ2ctsS.put(coreNameJava,ctsNameSql);
    }

    public Map<String,String> getMapper( FieldNameType sType, FieldNameType tType){
    	Map<String,String> m = null;
        if(sType.equals(FieldNameType.core4java) && tType.equals(FieldNameType.dict4sql)) {
            m = coreJ2dictS;
        }else if(sType.equals(FieldNameType.aps4java) && tType.equals(FieldNameType.dict4sql)) {
            m = apsJ2dictS;
        }else if(sType.equals(FieldNameType.cts4sql) && tType.equals(FieldNameType.dict4sql)) {
            m = ctsS2dictS;
        }else if(sType.equals(FieldNameType.aps4json) && tType.equals(FieldNameType.dict4sql)) {
            m = apsJson2dictS;
        }else if(sType.equals(FieldNameType.aps4json) && tType.equals(FieldNameType.core4json)) {
            m = apsJson2coreJson;
        }else if(sType.equals(FieldNameType.core4java) && tType.equals(FieldNameType.aps4java)) {
            m = coreJ2apsJ;
        }else if(sType.equals(FieldNameType.core4java) && tType.equals(FieldNameType.cts4sql)) {
            m = coreJ2ctsS;
        }else if(sType.equals(FieldNameType.aps4java) && tType.equals(FieldNameType.core4java) ) {
            m = apsJ2coreJ;
        }else if(sType.equals(FieldNameType.cts4sql) && tType.equals(FieldNameType.core4java) ) {
            m = ctsS2coreJ;
        }

        return m;
    }
}
