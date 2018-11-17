package com.zdmoney.credit.test.service;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
public class ComOrganizationTest {
	@Autowired
	IComOrganizationDao comOrganizationDaoImpl;

	@Test
	public void queryPart() {
		Map map = comOrganizationDaoImpl.queryPartOrgName(15594700L);
		String areaInfo = "";
		String branchInfo = "";
		String groupInfo = "";
		if (map != null) {
			String vLevel = Strings.parseString(map.get("V_LEVEL"));

			String name1 = Strings.parseString(map.get("NAME1"));
			String name2 = Strings.parseString(map.get("NAME2"));
			String name3 = Strings.parseString(map.get("NAME3"));
			String name4 = Strings.parseString(map.get("NAME4"));
			String name5 = Strings.parseString(map.get("NAME5"));
			String name6 = Strings.parseString(map.get("NAME6"));
			if (ComOrganizationEnum.Level.V105.getName().equals(vLevel)) {
				groupInfo = name6;
				branchInfo = name5;
				areaInfo = name2 + "/" + name3;
			} else if (ComOrganizationEnum.Level.V104.getName().equals(vLevel)) {
				branchInfo = name6;
				areaInfo = name3 + "/" + name4;
			} else if (ComOrganizationEnum.Level.V103.getName().equals(vLevel)) {
				areaInfo = name4 + "/" + name5;
			} else if (ComOrganizationEnum.Level.V102.getName().equals(vLevel)) {
				areaInfo = name5 + "/" + name6;
			} else if (ComOrganizationEnum.Level.V101.getName().equals(vLevel)) {
				areaInfo = name6;
			}
		}
		System.out.println(map);
		System.out.println("areaInfo:" + areaInfo);
		System.out.println("branchInfo:" + branchInfo);
		System.out.println("groupInfo:" + groupInfo);

	}
}
