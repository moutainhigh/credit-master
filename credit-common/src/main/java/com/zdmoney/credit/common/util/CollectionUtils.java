package com.zdmoney.credit.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合工具包
 * @author Ivan
 *
 */
public class CollectionUtils {
	
	/***
	 * 判断是否空集合
	 * @param coll
	 * @return
	 */
	public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }
	
	/***
	 * 判断是否空集合
	 * @param coll
	 * @return
	 */
	public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }
	
	/**
	 * 将数组转换成集合
	 * 
	 * CollectionUtils.toList(new String[]{"a","b","c","d","e"}) 		= ArrayList : ["a", "b", "c", "d", "e"]
	 * 
	 * @param arrays 数据对象
	 * @return
	 */
	@SuppressWarnings(value={"unchecked"})
	public static <T> List<T> toList(T... arrays) {
		List<T> list = new ArrayList<T>(arrays.length);
		for (T t : arrays) {
			list.add(t);
		}
		return list;
	}
	
	/**
	 * 
	 * 将数组转换成Map
	 * 
	 * CollectionUtils.toMap(new String[] {"a","b","c","d"}) 		= HashMap : {a=b, c=d}
	 * 
	 * @param arrays
	 * @return
	 */
	@SuppressWarnings(value={"rawtypes","unchecked"})
	public static Map toMap(Object... arrays) {
		Map maps = new HashMap();
		if (arrays.length % 2 != 0)
			throw new RuntimeException("arrays 长度 必须为偶数");
		for (int i = 0; i < arrays.length; i++) {
			maps.put(arrays[i], arrays[++i]);
		}
		return maps;
	}
	
	/**
	 * 将数组转换成枚举集合
	 * 
	 * CollectionUtils.toSet(new String[]{"a","b","c","d","e"}) 		= HashSet : ["a", "b", "c", "d", "e"]
	 * 
	 * @param arrays
	 * @return
	 */
	@SuppressWarnings(value={"unchecked"})
	public static <T> Set<T> toSet(T... arrays) {
		Set<T> sets = new HashSet<T>(arrays.length);
		for (T t : arrays) {
			sets.add(t);
		}
		return sets;
	}

}
