package com.zdmoney.credit.video.vo;

import java.io.InputStream;
import java.io.OutputStream;

import com.zdmoney.credit.common.util.FTPUtil;

/**
 * 目录文件Vo
 * 
 * @author Ivan
 *
 */
public class DownLoadVideoFileVo {
	/** 文件流 **/
	private InputStream inputStream;
	/** 文件流 **/
	private OutputStream outputStream;
	/** Ftp客户端 **/
	private FTPUtil ftpUtil;
	/** 文件名 **/
	private String fileName;
	/** 完整路径 **/
	private String fullFileName;
	/** 文件编号 **/
	private Long fileId;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public FTPUtil getFtpUtil() {
		return ftpUtil;
	}

	public void setFtpUtil(FTPUtil ftpUtil) {
		this.ftpUtil = ftpUtil;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 关闭流
	 * 
	 * @throws Exception
	 */
	public void disconnect() throws Exception {
		if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

}
