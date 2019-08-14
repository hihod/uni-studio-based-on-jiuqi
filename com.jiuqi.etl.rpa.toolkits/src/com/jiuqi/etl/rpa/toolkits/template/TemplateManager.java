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
 * ͨ��ģ�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TemplateManager {
	private static final String TEMPLATE_SUFFIX = ".template";
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private String TEMPLATE_PATH = WorkSpaceManager.getInstance().getLastWorkspace() + FILE_SEPARATOR + "Templates";
	
	/**
	 * ��ȡ���鼯��
	 * 
	 * @return ���ط��鼯��
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
	 * ���ݷ����ȡģ����Ϣ�б�
	 * 
	 * @param category ����
	 * @return ����ģ����Ϣ�б�
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
				
				//������תbyte����
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
				
				//��byte�������ģ������
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
		//��ѯ����Ϊ�գ����������
		if(StringUtils.isEmpty(keyValue)) {
			return getInfos();
		}
		
		List<TemplateInfo> infos = new ArrayList<TemplateInfo>();
		for (File file: directory.listFiles()) {
			FileInputStream fis = new FileInputStream(file);
			try {
				byte[] bytes = new byte[0];
				
				//������תbyte����
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
				
				//��byte�������ģ������
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
	 * �ж��ϴ�ģ���Ƿ��Ѵ���(ͨ��id)
	 * 
	 * @param bytes ģ���ֽ�����
	 * @return ����ģ���Ƿ������
	 * @throws Exception
	 */
	public boolean exists(byte[] bytes) throws Exception {
		//����ͨ��ģ��
		Template template = new Template();
		template.load(bytes);

		//�жϹ����ռ����Ƿ����
		File file = new File(getFilePath(template));
		return file.exists();
	}
	
	/**
	 * ���ģ��
	 * 
	 * @param bytes ģ���ֽ�����
	 * @return ��ӳɹ�����true������false
	 * @throws Exception
	 */
	public boolean add(byte[] bytes) throws Exception {
		//����ͨ��ģ��
		Template template = new Template();
		template.load(bytes);
		template.getInfo().setOrder(String.valueOf(System.currentTimeMillis()));
		
		//workspace�´�����ɾ��
		File file = new File(getFilePath(template));
		if (file.exists())
			file.delete();
		
		add(template);
		
		//������workspace
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
	 * ��ģ����Ϣ��ӵ�ѹ���ļ�
	 * @param template
	 * @throws Exception
	 */
	public void add(Template template) throws Exception{
		String filePath = getFilePath(template);
		FileOutputStream fos = new FileOutputStream(filePath);
		try {
			ZipOutputStream zos = new ZipOutputStream(fos);
			try {
				//���������Ϣ
				ZipEntry zip = new ZipEntry("manifest.mf");
				zos.putNextEntry(zip);
				Properties mf = template.getInfo().save();
				mf.store(zos, "");
				zos.flush();
				//���etl
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
	 * �Ƴ�ģ��
	 * 
	 * @param id ģ��id
	 * @return �Ƴ��ɹ�����true������false
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
			throw new Exception("δ�ҵ�ָ��ģ��[" + id + "]");
		
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
				throw new Exception("ģ��[" + id + "]����Ϊ��");
			
			Template template = new Template();
			template.load(bytes);
			return template;
		} finally {
			fis.close();
		}
	}
	/**
	 * ����ģ����Ϣ
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
		//���´���
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
			
			//������תbyte����
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
