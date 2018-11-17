package com.zdmoney.credit.common.vo;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Strings;

/**
 * 
 * 响应数据封装(带附件)
 * 
 * @author Ivan
 *
 */
public class AttachmentResponseInfo<E> extends ResponseInfo {
    private static final long serialVersionUID = -6276507727857308586L;

    public AttachmentResponseInfo() {
	super();
    }
    
    public AttachmentResponseInfo(ResponseEnum responseEnum,Object... arguments) {
	super(responseEnum.getCode(),Strings.format(responseEnum.getDesc(),arguments));
    }

    public AttachmentResponseInfo(String resCode, String resMsg) {
	super(resCode, resMsg);
    }

    public AttachmentResponseInfo(String resCode, String resMsg, String flowId) {
	super(resCode, resMsg, flowId);
    }

    /** 附件 **/
    private E attachment;

    public E getAttachment() {
	return attachment;
    }

    public void setAttachment(E attachment) {
	this.attachment = attachment;
    }

}
