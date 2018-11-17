package com.zdmoney.credit.common.util.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.file.vo.UploadFile;

/***
 * 校验上传文件
 * @author Ivan
 *
 */
public class UploadFileUtil {
	
	protected static Log logger = LogFactory.getLog(UploadFileUtil.class);
	
	/** 文件最大Size **/
	public final static long FILE_SIZE_DEFAULT_MAX = 1024 * 1024 * 5;
	
	/** Excel文件类型  **/
	public final static String [] FILE_TYPE_EXCEL = new String[]{"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
		,"application/vnd.ms-excel","application/.xls","application/octet-stream","application/xls"};

	/**
	 * 检验上传（空、大小、格式）
	 * @param file 文件
	 */
	public static void valid(UploadFile file) {
		Assert.notNull(file, ResponseEnum.FILE_EMPTY_FILE);
		MultipartFile multipartFile = file.getFile();
		Assert.notNull(multipartFile, ResponseEnum.FILE_EMPTY_FILE);
		long fileSize = file.getFileMaxSize() <= 0 ? FILE_SIZE_DEFAULT_MAX : file.getFileMaxSize();
		String [] fileType = file.getFileType();
		Assert.notArrayEmpty(fileType,"文件类型");
		if (multipartFile.isEmpty() || multipartFile.getSize() <= 0) {
			/** 空文件 **/
			throw new PlatformException(ResponseEnum.FILE_EMPTY_FILE,"");
		} else if (multipartFile.getSize() > fileSize) {
			/** 文件过大 **/
			throw new PlatformException(ResponseEnum.FILE_SIZE_OUT_RANGE,(fileSize / 1024 / 1024) + "MB");
		} else if (!Strings.arrayContains(fileType, multipartFile.getContentType())) {
			/** 文件类型错误 **/
			logger.error("Not Match ContentType : " + multipartFile.getContentType());
			throw new PlatformException(ResponseEnum.FILE_ERROR_TYPE);
		}
	}
	
}
