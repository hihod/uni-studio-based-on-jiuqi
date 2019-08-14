package com.jiuqi.etl.rpa.toolkits.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 通用模板
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Template {
	private TemplateInfo info;
	private byte[] data;

	public TemplateInfo getInfo() {
		return info;
	}

	public byte[] getData() {
		return data;
	}
	
	public void load(byte[] bytes) throws Exception {
		byte[] etlData = new byte[0];
		byte[] manifestData = new byte[0];
		
		//输入字节流
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ZipInputStream zip = new ZipInputStream(bis);

			//遍历压缩包
			ZipEntry entry = zip.getNextEntry();
			while(entry != null ) {
				String entryName = entry.getName().toLowerCase();
				if (entryName.equalsIgnoreCase("manifest.mf"))
					manifestData = readStreamToBytes(zip);
				else if (entryName.endsWith(".etl"))
					etlData = readStreamToBytes(zip);
				
				entry = zip.getNextEntry();
			}
			
			//压缩包内包含的文件
			if (manifestData.length == 0)
				throw new Exception("上传包缺少描述文件“manifest.mf”");
			if (etlData.length == 0)
				throw new Exception("上传包缺少流程文件“main.etl”");
		} finally {
			bis.close();
		}
		
		//流程包数据
		this.data = etlData;
		
		//模板信息
		ByteArrayInputStream mfbis = new ByteArrayInputStream(manifestData);
		try {
			Properties properties = new Properties();
			properties.load(new InputStreamReader(mfbis, "UTF-8"));
			
			TemplateInfo info = new TemplateInfo();
			info.load(properties);
			this.info = info;
		} finally {
			mfbis.close();
		}
	}
	
	private static byte[] readStreamToBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		int count = 0;
		byte[] buffer = new byte[1024 * 8];
		while ((count = is.read(buffer)) != -1)
			bos.write(buffer, 0, count);
		return bos.toByteArray();
	}

}
