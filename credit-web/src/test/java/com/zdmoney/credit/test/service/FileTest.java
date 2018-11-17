package com.zdmoney.credit.test.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileTest {

	public static void main(String[] args) throws Exception {
		a();
//		b();
	}

	public static void a() {
		String fileContent = "";
		try {
			File f = new File("c:\\bb.txt");
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), "GB2312");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line + "\n";
				}
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fileContent);
	}

	public static void b() throws Exception {
		String fileContent = "aaaaaPQ10152900H1400伍少云    ";
		File f = new File("c:\\bb.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "GB2312");
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(fileContent);
		writer.close();
	}

}
