package com.jiuqi.etl.rpa.toolkits.welcom.bean;
/**
 * 案例
* @author 作者：houzhiyuan 2019年6月20日 下午3:48:21
*/
public class ExampleBean implements Comparable<ExampleBean> {
	private String id;
	private String title;
	private String img;
	private String desc;
	private String controlFlowId;
	private byte[] controlFlowContent;
	private int order;
	/**
	 * 案例标识
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 案例标识
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 案例标题
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 案例标题
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 案例描述
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 案例描述
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 控制流标识
	 * @return the controlFlowId
	 */
	public String getControlFlowId() {
		return controlFlowId;
	}
	/**
	 * 控制流标识
	 * @param controlFlowId the controlFlowId to set
	 */
	public void setControlFlowId(String controlFlowId) {
		this.controlFlowId = controlFlowId;
	}
	/**
	 * 设置图标路径
	 * @param imgPath
	 */
	public void setImg(String imgPath) {
		this.img = imgPath;
	}
	/**
	 * 控制流内容
	 */
	public byte[] getControlFlowContent() {
		return controlFlowContent;
	}
	/**
	 * 控制流内容
	 */
	public void setControlFlowContent(byte[] controlFlowContent) {
		this.controlFlowContent = controlFlowContent;
	}
	
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}
	
	
	public int compareTo(ExampleBean bean) {
		return this.order - bean.getOrder();
	}
}
