package com.zdmoney.credit.system.service;



import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Date;





import org.springframework.stereotype.Service;

import com.zdmoney.credit.system.service.pub.ISerialNoService;


/**
 * 流水号生成
 * @author 00232949
 *
 */
@Service
public class SerialNoService  implements ISerialNoService {
    
	public static final int SEQ_MAX_LENGTH = 10000;
	
	private int index = 1;
	
	private static byte ipSuffix;//本服务器ip后3位
	
	static {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			byte[] bytes = inet.getAddress();
			ipSuffix = bytes[bytes.length - 1];
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public synchronized long create() {
		long seril = ipSuffix * 10000 + (index ++);
		if(index == SEQ_MAX_LENGTH) index = 1;
		return seril;
	}
	
	/* (non-Javadoc)
	 * @see net.easipay.pepp.peps.service.sys.ISerialNoService#genTrxSerialNo()
	 */
	public String generateTrxSerialNo() {
		String serialNo = String.valueOf(create());
		String trxSerialNo = MessageFormat.format("{0}{1,time,yyyyMMddHHmmssSSS}{2,number,00000000}", "T", new Date(), Long.valueOf(serialNo));
		return trxSerialNo;
	}

	public static void main(String[] args) {
		SerialNoService serialNoService = new SerialNoService();
		for(int i=0;i<101;i++){
			System.out.println(serialNoService.generateTrxSerialNo());
			
			
			
//			String serialNo = String.valueOf(serialNoService.create()).substring(16);
//			System.out.println(serialNo);
//			String trxSerialNo = MessageFormat.format("{0}{1,time,yyyyMMddHHmmssSSS}{2,number,00000000}", "T", new Date(), Long.valueOf(serialNo));
//			System.out.println(trxSerialNo);
		}
		
		
	}
    
}
