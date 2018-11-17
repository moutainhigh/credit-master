package com.zdmoney.credit.system.service.pub;



/**
 * 产生各类流水号
 * 
 */
public interface ISerialNoService {
	
	/**
	 * 交易流水号默认长度
	 */
	public static final int TRX_SERIAL_NO_LENGTH = 26;
	
	
	/**
	 * 生成交易流水号
	 * 规则：T+yyyyMMddHHmmssSSS + 1~2位预留 （看ip）+ 2~3位ip+ 4位自增数
	 * @return
	 */
	public String generateTrxSerialNo();
	
	
}
