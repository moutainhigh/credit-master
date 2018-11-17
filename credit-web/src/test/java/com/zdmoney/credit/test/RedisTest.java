package com.zdmoney.credit.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.redis.RedisClientUtil;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
public class RedisTest {

	@Autowired
	RedisClientUtil redisClientUtil;
	
	public static void main(String []args) {
		String regExp = "^[0-9]{11}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher("a5921979390");

		System.out.println(m.find());// boolean
	}

	@Test
	public void testSet() {
		try {
			for (int i=0;i<10000;i++) {
				redisClientUtil.setValue("aaa" + i, 10, "bbb");
				System.out.println(i);
			}
			 
			 System.out.println(redisClientUtil.getValue("aaa0"));
			 System.out.println(redisClientUtil.getValue("aaa1"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
