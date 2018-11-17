package com.zdmoney.credit.common.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 阿里巴巴 JSON工具类
 * 
 * @author Ivan
 *
 */
public class JsonFileUtils {
	
	/**
	 * 将JSONObject 写入文件
	 * @param t 
	 * @param file
	 * @throws IOException
	 */
	public static <T> void writeJsonToFile(T t, File file) throws IOException {
		String jsonStr = JSONObject.toJSONString(t,
				SerializerFeature.PrettyFormat);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file)));
		bw.write(jsonStr);
		bw.close();
	}
	
	/**
	 * 将JSONObject 写入文件
	 * @param t
	 * @param filename
	 * @throws IOException
	 */
	public static <T> void writeJsonToFile(T t, String filename)
			throws IOException {
		writeJsonToFile(t, new File(filename));
	}
	
	/**
	 * 从文件中读取内容，转换成指定类实例
	 * @param cls
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJsonFromFile(Class<T> cls, File file)
			throws IOException {
		StringBuilder strBuilder = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = null;
		while ((line = br.readLine()) != null) {
			strBuilder.append(line);
		}
		br.close();
		return JSONObject.parseObject(strBuilder.toString(), cls);
	}
	
	/**
	 * 从文件中读取内容，转换成指定类实例
	 * @param cls
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJsonFromFile(Class<T> cls, String filename)
			throws IOException {
		return readJsonFromFile(cls, new File(filename));
	}
	
	/**
	 * 从文件中读取内容，转换成指定类实例
	 * @param typeReference 转换成指定类型格式 如new TypeReference<List<String>>(){} 或 new TypeReference<List<Map<String,Object>>>(){} 调用JSON.parseObject
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJsonFromFile(TypeReference<T> typeReference,
			File file) throws IOException {
		StringBuilder strBuilder = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = null;
		while ((line = br.readLine()) != null) {
			strBuilder.append(line);
		}
		br.close();
		return JSONObject.parseObject(strBuilder.toString(), typeReference);
	}
	
	/**
	 * 从文件中读取内容，转换成指定类实例
	 * @param typeReference 转换成指定类型格式 如new TypeReference<List<String>>(){} 或 new TypeReference<List<Map<String,Object>>>(){} 调用JSON.parseObject
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJsonFromFile(TypeReference<T> typeReference,
			String filename) throws IOException {
		return readJsonFromFile(typeReference, new File(filename));
	}
	
}
