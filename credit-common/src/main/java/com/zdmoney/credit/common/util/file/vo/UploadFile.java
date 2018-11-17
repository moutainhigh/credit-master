package com.zdmoney.credit.common.util.file.vo;

import org.springframework.web.multipart.MultipartFile;

/***
 * 上传文件封闭
 * @author Ivan
 *
 */
public class UploadFile {
	/** 文件对象 **/
	private MultipartFile file;
	/** 校验文件最大Size **/
	private long fileMaxSize = 0;
	/** 检验文件类型 **/
	private String[] fileType;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public long getFileMaxSize() {
		return fileMaxSize;
	}
	public void setFileMaxSize(long fileMaxSize) {
		this.fileMaxSize = fileMaxSize;
	}
	public String[] getFileType() {
		return fileType;
	}
	public void setFileType(String[] fileType) {
		this.fileType = fileType;
	}
	
	
	
}
