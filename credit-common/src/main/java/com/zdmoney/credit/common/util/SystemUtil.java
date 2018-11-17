package com.zdmoney.credit.common.util;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class SystemUtil {

	// 获得系统属性集
	public static Properties props = System.getProperties();
	// 操作系统名称
	public static String OS_NAME = getPropertery("os.name");
	// 行分页符
	public static String OS_LINE_SEPARATOR = getPropertery("line.separator");
	// 文件分隔符号
	public static String OS_FILE_SEPARATOR = getPropertery("file.separator");
	
	private final static char[] HEX = "0123456789ABCDEF".toCharArray();
	
	private static Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	/**
	 * 
	 * 根据系统的类型获取本服务器的ip地址
	 * 
	 * InetAddress inet = InetAddress.getLocalHost(); 但是上述代码在Linux下返回127.0.0.1。
	 * 主要是在linux下返回的是/etc/hosts中配置的localhost的ip地址，
	 * 而不是网卡的绑定地址。后来改用网卡的绑定地址，可以取到本机的ip地址：）：
	 * 
	 * @throws java.net.UnknownHostException
	 */
	public static String getSystemLocalIp() throws UnknownHostException {
		String inet = null;
		String osname = getSystemOSName();
		try {
			// 针对window系统
			if (osname.equalsIgnoreCase("Windows XP") || osname.equalsIgnoreCase("Windows 7")) {
				inet = getWinLocalIp();
				// 针对linux系统
			} else if (osname.equalsIgnoreCase("Linux")) {
				inet = getUnixLocalIp();
			}
			if (null == inet) {
				throw new UnknownHostException("主机的ip地址未知");
			}
		} catch (SocketException e) {
			throw new UnknownHostException("获取本机ip错误" + e.getMessage());
		}
		return inet;
	}

	/**
	 * 获取FTP的配置操作系统
	 * 
	 * @return
	 */
	public static String getSystemOSName() {
		// 获得系统属性集
		Properties props = System.getProperties();
		// 操作系统名称
		String osname = props.getProperty("os.name");

		return osname;
	}

	/**
	 * 获取属性的值
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertery(String propertyName) {
		return props.getProperty(propertyName);
	}

	/**
	 * 获取window 本地ip地址
	 * 
	 * @return
	 * @throws java.net.UnknownHostException
	 */
	private static String getWinLocalIp() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		return inet.getHostAddress();
	}

	/**
	 * 
	 * 可能多多个ip地址只获取一个ip地址 获取Linux 本地IP地址
	 * 
	 * @return
	 * @throws java.net.SocketException
	 * @throws java.net.UnknownHostException
	 */
	private static String getUnixLocalIp() throws SocketException, UnknownHostException {
		String ip = "";
		for (Enumeration<NetworkInterface> i = NetworkInterface.getNetworkInterfaces(); i.hasMoreElements();) {
			NetworkInterface ni = i.nextElement();
			Enumeration<InetAddress> j = ni.getInetAddresses();
			while (j.hasMoreElements()) {
				String temIP = j.nextElement().toString();
				temIP = temIP.substring(1, temIP.length());
				Matcher matcher = pattern.matcher(temIP);
				if (matcher.matches()) {
					if (!"127".equals(temIP.substring(0, 3))) {
						ip = temIP;
					}
				}
			}
		}
		return ip;
	}

	@SuppressWarnings("unused")
	private static String toMacString(byte[] bys) {
		if (bys == null) {
			return null;
		}
		char[] chs = new char[bys.length * 3 - 1];
		for (int i = 0, k = 0; i < bys.length; i++) {
			if (i > 0) {
				chs[k++] = '-';
			}
			chs[k++] = HEX[(bys[i] >> 4) & 0xf];
			chs[k++] = HEX[bys[i] & 0xf];
		}
		return new String(chs);
	}

	/**
	 * 
	 * 获取当前运行程序的内存信息
	 * 
	 * @return
	 */
	public static final String getRAMinfo() {
		Runtime rt = Runtime.getRuntime();
		return "RAM: " + rt.totalMemory() + " bytes total, " + rt.freeMemory() + " bytes free.";
	}
	
	/**
	 * 获取请求的ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
