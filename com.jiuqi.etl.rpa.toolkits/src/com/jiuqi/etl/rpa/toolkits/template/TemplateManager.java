package com.jiuqi.etl.rpa.toolkits.template;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.storage.WorkSpaceManager;

/**
 * 通用模板管理
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TemplateManager {
	private static final String TEMPLATE_SUFFIX = ".template";
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private String TEMPLATE_PATH = WorkSpaceManager.getInstance().getLastWorkspace() + FILE_SEPARATOR + "Templates";
	
	/**
	 * 获取分组集合
	 * 
	 * @return 返回分组集合
	 * @throws Exception
	 */
	public List<String> getCategories() throws Exception {
		Set<String> set = new HashSet<String>();
		for (TemplateInfo info: getInfos())
			set.add(info.getCategory());
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 根据分组获取模板信息列表
	 * 
	 * @param category 分组
	 * @return 返回模板信息列表
	 * @throws Exception
	 */
	public List<TemplateInfo> getInfosByCategory(String category) throws Exception {
		List<TemplateInfo> list = new ArrayList<TemplateInfo>();
		for (TemplateInfo info: getInfos()) {
			if (StringUtils.equalsIgnoreCase(info.getCategory(), category))
				list.add(info);
		}
		
		Collections.sort(list, new Comparator<TemplateInfo>() {
			public int compare(TemplateInfo o1, TemplateInfo o2) {
				return o1.getTitle().compareToIgnoreCase(o2.getTitle());
			}
		});
		return list;
			
	}
	
	public List<TemplateInfo> getInfos() throws Exception {
		File directory = new File(TEMPLATE_PATH);
		if (!directory.exists() || !directory.isDirectory())
			return new ArrayList<TemplateInfo>(0);
		
		List<TemplateInfo> infos = new ArrayList<TemplateInfo>();
		for (File file: directory.listFiles()) {
			FileInputStream fis = new FileInputStream(file);
			try {
				byte[] bytes = new byte[0];
				
				//输入流转byte数组
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					int count = 0;
					byte[] buffer = new byte[1024 * 8];
					while ((count = fis.read(buffer)) != -1)
						bos.write(buffer, 0, count);
					
					bytes = bos.toByteArray();
				} finally {
					bos.close();
				}
				
				//从byte数组加载模板数据
				Template template = new Template();
				template.load(bytes);
				infos.add(template.getInfo());
			} finally {
				fis.close();
			}
		}
		
		return infos;
	}
	public List<TemplateInfo> getInfos(String keyValue) throws Exception {
		File directory = new File(TEMPLATE_PATH);
		if (!directory.exists() || !directory.isDirectory())
			return new ArrayList<TemplateInfo>(0);
		//查询内容为空，则查找所有
		if(StringUtils.isEmpty(keyValue)) {
			return getInfos();
		}
		
		List<TemplateInfo> infos = new ArrayList<TemplateInfo>();
		for (File file: directory.listFiles()) {
			FileInputStream fis = new FileInputStream(file);
			try {
				byte[] bytes = new byte[0];
				
				//输入流转byte数组
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					int count = 0;
					byte[] buffer = new byte[1024 * 8];
					while ((count = fis.read(buffer)) != -1)
						bos.write(buffer, 0, count);
					
					bytes = bos.toByteArray();
				} finally {
					bos.close();
				}
				
				//从byte数组加载模板数据
				Template template = new Template();
				template.load(bytes);
				TemplateInfo ti = template.getInfo();
				if(ti.getTitle().toUpperCase().contains(keyValue.toUpperCase())) {
					infos.add(template.getInfo());
				}
				
			} finally {
				fis.close();
			}
		}
		
		return infos;
	}
	
	/**
	 * 判断上传模板是否已存在(通过id)
	 * 
	 * @param bytes 模板字节数组
	 * @return 返回模板是否存在在
	 * @throws Exception
	 */
	public boolean exists(byte[] bytes) throws Exception {
		//加载通用模板
		Template template = new Template();
		template.load(bytes);

		//判断工作空间下是否存在
		File file = new File(getFilePath(template));
		return file.exists();
	}
	
	/**
	 * 添加模板
	 * 
	 * @param bytes 模板字节数组
	 * @return 添加成功返回true，否则false
	 * @throws Exception
	 */
	public boolean add(byte[] bytes) throws Exception {
		//加载通用模板
		Template template = new Template();
		template.load(bytes);
		template.getInfo().setOrder(String.valueOf(System.currentTimeMillis()));
		
		//workspace下存在则删除
		File file = new File(getFilePath(template));
		if (file.exists())
			file.delete();
		
		add(template);
		
		//拷贝到workspace
		/*ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				int count = 0;
				byte[] buffer = new byte[1024 * 8];
				while ((count = bis.read(buffer)) != -1)
					fos.write(buffer, 0, count);
			} finally {
				fos.close();
			}
		} finally {
			bis.close();
		}*/
		
		return true;
	}
	/**
	 * 将模板信息添加到压缩文件
	 * @param template
	 * @throws Exception
	 */
	public void add(Template template) throws Exception{
		String filePath = getFilePath(template);
		FileOutputStream fos = new FileOutputStream(filePath);
		try {
			ZipOutputStream zos = new ZipOutputStream(fos);
			try {
				//添加配置信息
				ZipEntry zip = new ZipEntry("manifest.mf");
				zos.putNextEntry(zip);
				Properties mf = template.getInfo().save();
				mf.store(zos, "");
				zos.flush();
				//添加etl
				ZipEntry zipEtl = new ZipEntry("main.etl");
				zos.putNextEntry(zipEtl);
				zos.write(template.getData());
				zos.flush();
				
			} finally {
				zos.close();
			}
		} finally {
			fos.close();
		}
	}
	
	/**
	 * 移除模板
	 * 
	 * @param id 模板id
	 * @return 移除成功返回true，否则false
	 */
	public boolean remove(String id) {
		String filePath = TEMPLATE_PATH + FILE_SEPARATOR + id + TEMPLATE_SUFFIX;
		
		File file = new File(filePath);
		if (file.exists())
			return file.delete();
			
		return false;
	}
	
	public Template get(String id) throws Exception {
		String filePath = TEMPLATE_PATH + FILE_SEPARATOR + id + TEMPLATE_SUFFIX;
		
		File file = new File(filePath);
		if (!file.exists())
			throw new Exception("未找到指定模板[" + id + "]");
		
		FileInputStream fis = new FileInputStream(file);
		try {
			byte[] bytes = new byte[0];
					
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				int count = 0;
				byte[] buffer = new byte[1024 * 8];
				while ((count = fis.read(buffer)) != -1)
					bos.write(buffer, 0, count);
				
				bytes = bos.toByteArray();
			} finally {
				bos.close();
			}
			
			if (bytes.length == 0)
				throw new Exception("模板[" + id + "]数据为空");
			
			Template template = new Template();
			template.load(bytes);
			return template;
		} finally {
			fis.close();
		}
	}
	/**
	 * 更新模板信息
	 * @param template
	 * @throws Exception
	 */
	public void upateTemplate(String templateId) throws Exception {
		Template template = get(templateId);
		if(template == null) {
			return;
		}
		String filePath = TEMPLATE_PATH + FILE_SEPARATOR + template.getInfo().getId() + TEMPLATE_SUFFIX;
		
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
		//重新创建
		add(template);
	}
	
	private String getFilePath(Template template) {
		File templateDir = new File(TEMPLATE_PATH);
		if (!templateDir.exists())
			templateDir.mkdirs();
		
		return TEMPLATE_PATH + FILE_SEPARATOR + template.getInfo().getId() + TEMPLATE_SUFFIX;
	}
	
	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("D:/Desktop.zip");
		try {
			byte[] bytes = new byte[0];
			
			//输入流转byte数组
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				int count = 0;
				byte[] buffer = new byte[1024 * 8];
				while ((count = fis.read(buffer)) != -1)
					bos.write(buffer, 0, count);
				
				bytes = bos.toByteArray();
			} finally {
				bos.close();
			}
			
			new TemplateManager().add(bytes);
		} finally {
			fis.close();
		}
	}
	
}
