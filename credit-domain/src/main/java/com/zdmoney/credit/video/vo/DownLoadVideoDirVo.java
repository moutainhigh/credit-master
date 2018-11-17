package com.zdmoney.credit.video.vo;

import java.util.List;

/**
 * 下载影像文件Vo
 * 
 * @author Ivan
 *
 */
public class DownLoadVideoDirVo {
	/** 完整的目录路径 如：/丁一422421197006260045/合同 **/
	private String fullPath;
	/** 包含所有文件集合 **/
	private List<DownLoadVideoFileVo> fileList;

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public List<DownLoadVideoFileVo> getFileList() {
		return fileList;
	}

	public void setFileList(List<DownLoadVideoFileVo> fileList) {
		this.fileList = fileList;
	}

}
