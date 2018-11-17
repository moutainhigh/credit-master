package com.zdmoney.credit.common.exception;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;

/**
 * 平台异常
 * 
 * @author Ivan
 *
 */
public class PlatformException extends RuntimeException {

	private static final long serialVersionUID = 7946023196149777499L;

	private static Log logger = LogFactory.getLog(PlatformException.class);

	protected ResponseEnum responseCode;

	private LogLevel logLevel = LogLevel.ERROR;

	private Object[] arguments;

	private String errorMsg;

	private Throwable cause;

	public ResponseEnum getResponseCode() {
		return responseCode;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public PlatformException(String message) {
		super(message);
		errorMsg = message;
	}

	public PlatformException(ResponseEnum responseCode, Object... arguments) {
		super();
		this.responseCode = responseCode;
		this.arguments = arguments;
	}

	// public PlatformException(ResponseEnum responseCode,String errorMsg,
	// Object... arguments) {
	// super();
	// this.responseCode = responseCode;
	// this.errorMsg = errorMsg;
	// this.arguments = arguments;
	// }

	public PlatformException(ResponseEnum responseCode, Throwable cause) {
		super(cause);
		this.responseCode = responseCode;
		this.cause = cause;
	}

	public PlatformException(String message, Throwable cause) {
		super(message, cause);
		this.cause = cause;
	}

	@Override
	public String toString() {
		if (cause != null) {
			cause.printStackTrace();
		}
		return getMessage();
	}

	@Override
	public String getMessage() {
		String returnMsg = "";
		if (Strings.isNotEmpty(errorMsg)) {
			returnMsg = errorMsg;
		} else {
			if (responseCode != null) {
				returnMsg = responseCode.getDesc();
			}
		}
		if (Strings.isEmpty(returnMsg)) {
			returnMsg = "not message";
		}
		return MessageFormat.format(returnMsg, arguments);
	}

	/**
	 * 将异常转换成ResponseInfo对象
	 * 
	 * @return
	 */
	public ResponseInfo toResponseInfo() {
		ResponseEnum code = this.getResponseCode();
		if (code != null) {
			return new ResponseInfo(code, this.arguments);
		} else {
			return new ResponseInfo(ResponseEnum.FULL_MSG,
					new Object[]{this.getMessage()});
		}
	}

	/**
	 * 将异常转换成ResponseInfo对象
	 * 
	 * @return
	 */
	public <T extends Object> AttachmentResponseInfo<T> toAttachmentResponseInfo() {
		ResponseEnum code = this.getResponseCode();
		if (code != null) {
			return new AttachmentResponseInfo<T>(code, this.arguments);
		} else {
			return new AttachmentResponseInfo<T>(ResponseEnum.FULL_MSG,
					new Object[]{this.getMessage()});
		}
	}

	/**
	 * 设置异常打印的日志级别
	 * 
	 * @param logLevel
	 * @return
	 */
	public PlatformException applyLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	/**
	 * 打印异常信息
	 */
	public void printStackTraceExt() {
		printStackTraceExt(logger);
	}

	/**
	 * 打印异常信息
	 */
	public void printStackTraceExt(Log logger) {
		switch (this.logLevel) {
			case DEBUG :
				logger.debug(this.getMessage());
				break;
			case WARN :
				logger.warn(this.getMessage());
				break;
			case INFO :
				logger.info(this.getMessage());
				break;
			case ERROR :
				logger.error(this, this);
				break;
			default :
				break;
		}

	}

}