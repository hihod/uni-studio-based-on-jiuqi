package com.jiuqi.etl.rpa.toolkits.welcom.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.rpa.toolkits.template.TemplateInfo;
import com.jiuqi.etl.rpa.toolkits.template.TemplateManager;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ExampleBean;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;
import com.jiuqi.etl.storage.WorkSpaceManager;
/**
* @author 作者：houzhiyuan 2019年6月20日 下午3:35:55
*/
public class WelcomContentManager {
	private static WelcomContentManager instance;
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public final static String KEY_WELOCOM_OPENENABLE = "OPENENABLE";
	public final static String CONFIG_WELCOM_PATH = WorkSpaceManager.getInstance().getLastWorkspace() + FILE_SEPARATOR + "config" + FILE_SEPARATOR + "welcom.config";
	
	private String TEMPLATE_PATH = WorkSpaceManager.getInstance().getLastWorkspace() + FILE_SEPARATOR + "Demo" + FILE_SEPARATOR;
	public static final String KEY_EXAMPLE_TITLE = "TITLE";
	public static final String KEY_EXAMPLE_DESC = "DESC";
	public static final String KEY_EXAMPLE_ORDER = "ORDER";

	public static WelcomContentManager getInstance() {
		if(null == instance) {
			instance = new WelcomContentManager();
		}
		return instance;
	}
	
	/**
	 * 获取经典案例列表
	 * @return
	 * @throws Exception
	 */
	public List<ExampleBean> getExamples() throws Exception{
		List<ExampleBean> beans = new ArrayList<ExampleBean>();
		String path = TEMPLATE_PATH;
		File fileExample = new File(path);
		if(!fileExample.exists()) {
			return beans;
		}
		File[] files = fileExample.listFiles();
		for(File subFile : files) {
			if(subFile.isDirectory()) {
				continue;
			}
			String ext = getFileExt(subFile.getName());
			if(!"ZIP".equals(ext)) {
				continue;
			}
			ExampleBean bean = null;
			bean = getExampleBeanFromZip(subFile);
			beans.add(bean);
		}
		
		Collections.sort(beans);
		return  beans;
	}
	public List<ProcessTemplateBean> getProcessTemplateBeans() throws Exception{
		return getProcessTemplateBeans(null);
	}
	/**
	 * 从工作空间中获取流程模板对象信息
	 * 分组和子节点根据名称正序排序
	 * @return
	 * @throws Exception
	 */
	public List<ProcessTemplateBean> getProcessTemplateBeans(String searchValue) throws Exception{
		List<ProcessTemplateBean> beans = new ArrayList<ProcessTemplateBean>();
		TemplateManager tplManager = new TemplateManager();
		List<TemplateInfo> templateInfos = null;
		try {
			templateInfos = tplManager.getInfos(searchValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(templateInfos==null || templateInfos.size() ==0) {
			return beans;
		}
		Map<String, List<TemplateInfo>> map =buildTemplateGroupMap(templateInfos);
		if(map == null) {
			return beans;
		}
		int pId = 1;
		ProcessTemplateBean pBean = null;
		for(Map.Entry<String , List<TemplateInfo>> entry : map.entrySet()) {
			pBean = createParentProcessTemplateBean(pId++, entry.getKey());
			List<ProcessTemplateBean> children = new ArrayList<ProcessTemplateBean>();
			for(TemplateInfo info : entry.getValue()) {
				ProcessTemplateBean child = convert2ProcessTplBean(info);
				child.setpId(pBean.getId());
				children.add(child);
			}
			Collections.sort(children);
			pBean.setChildren(children);
			beans.add(pBean);
		}
		
		return beans;
	}
	/**
	 * 获取欢迎页面是否打开
	 * @return
	 * @throws Exception
	 */
	public boolean getOpenEnable(){
		File file = new File(CONFIG_WELCOM_PATH);
		if(!file.exists()) {
			return true;
		}
		try {
			byte[] data = readStreamToBytes(new FileInputStream(file));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			try {
				Properties properties = new Properties();
				properties.load(new InputStreamReader(bis, "UTF-8"));
				String strEnable = properties.getProperty(KEY_WELOCOM_OPENENABLE);
				if(StringUtils.isEmpty(strEnable)) {
					return true;
				}
				return Integer.parseInt(strEnable) == 1?true:false;
			} finally {
				bis.close();
			}
		} catch (Exception e) {
			return true;
		}
		
		
	}
	/**
	 * 保存欢迎页面打开状态
	 * @param isOpen
	 * @throws Exception
	 */
	public void setOpenEnable(boolean isOpen){
		File file = new File(CONFIG_WELCOM_PATH);
		String pPath = file.getParent();
		File dir = new File(pPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		Properties p = new Properties();
		p.setProperty(KEY_WELOCOM_OPENENABLE, String.valueOf((isOpen?1:0)));
		try {
			FileOutputStream os = new FileOutputStream(file);
			try {
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				try {
					p.store(osw, "");
				} finally {
					osw.close();
				}
				
			} finally {
				os.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//转换为欢迎页面自己的模板对象
	private ProcessTemplateBean convert2ProcessTplBean(TemplateInfo info) throws Exception{
		ProcessTemplateBean bean = new ProcessTemplateBean();
		bean.setId(info.getId());
		bean.setTitle(info.getTitle());
		if(!StringUtils.isEmpty(info.getOrder())) {
			bean.setOrder(Long.parseLong(info.getOrder()));
		}
		return bean;
	}
	/**
	 * 创建流程模板父节点
	 * @param pId
	 * @param title
	 * @return
	 * @throws Exception
	 */
	private ProcessTemplateBean createParentProcessTemplateBean(int pId, String title) throws Exception{
		ProcessTemplateBean bean = new ProcessTemplateBean();
		bean.setId(String.valueOf(pId));
		bean.setTitle(title);
		return bean;
	}
	private Map<String, List<TemplateInfo>> buildTemplateGroupMap(List<TemplateInfo> templateInfos) throws Exception{
		Map<String, List<TemplateInfo>> map = new HashMap<String, List<TemplateInfo>>();
		for(TemplateInfo info: templateInfos) {
			if(map.containsKey(info.getCategory())) {
				map.get(info.getCategory()).add(info);
			}
			else {
				List<TemplateInfo> infos = new ArrayList<TemplateInfo>();
				infos.add(info);
				map.put(info.getCategory(), infos);
			}
		}
		return map;
	}
	
	private ExampleBean getExampleBeanFromZip(File file) throws Exception{
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		ExampleBean bean = new ExampleBean();
		try {
			ZipInputStream zis = new ZipInputStream(fis);
			try {
				ZipEntry entry = zis.getNextEntry();
				while(entry != null) {
					String ext = getFileExt(entry.getName());
					if("MF".equals(ext)) {//配置文件
						readExampleMF(bean, zis);
					}
					else if("ETL".equals(ext)) {//etl文件
						bean.setControlFlowContent(readStreamToBytes(zis));
					}
					entry = zis.getNextEntry();
				}
			} finally {
				if(zis != null) {
					zis.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(fis != null) {
				fis.close();
			}
		}
		return bean;
	}
	private  void readExampleMF(ExampleBean bean, InputStream is) throws Exception{
		byte[] data = readStreamToBytes(is);
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		try {
			Properties properties = new Properties();
			properties.load(new InputStreamReader(bis, "UTF-8"));
			bean.setTitle(properties.getProperty(KEY_EXAMPLE_TITLE));
			bean.setDesc(properties.getProperty(KEY_EXAMPLE_DESC));
			String strOrder = properties.getProperty(KEY_EXAMPLE_ORDER);
			int order = 0;
			if(!StringUtils.isEmpty(strOrder)) {
				order = Integer.parseInt(strOrder);
			}
			bean.setOrder(order);
		} finally {
			bis.close();
		}
	}
	private  byte[] readStreamToBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		int count = 0;
		byte[] buffer = new byte[1024 * 8];
		while ((count = is.read(buffer)) != -1)
			bos.write(buffer, 0, count);
		return bos.toByteArray();
	}
	private String getFileExt(String fileName) throws Exception{
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix.toUpperCase();
	}

}
