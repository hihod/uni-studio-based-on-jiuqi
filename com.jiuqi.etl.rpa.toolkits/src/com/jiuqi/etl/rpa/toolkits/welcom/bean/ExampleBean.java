package com.jiuqi.etl.rpa.toolkits.welcom.bean;
/**
 * ����
* @author ���ߣ�houzhiyuan 2019��6��20�� ����3:48:21
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
	 * ������ʶ
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * ������ʶ
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * ��������
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * ��������
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * ��������
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * ��������
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * ��������ʶ
	 * @return the controlFlowId
	 */
	public String getControlFlowId() {
		return controlFlowId;
	}
	/**
	 * ��������ʶ
	 * @param controlFlowId the controlFlowId to set
	 */
	public void setControlFlowId(String controlFlowId) {
		this.controlFlowId = controlFlowId;
	}
	/**
	 * ����ͼ��·��
	 * @param imgPath
	 */
	public void setImg(String imgPath) {
		this.img = imgPath;
	}
	/**
	 * ����������
	 */
	public byte[] getControlFlowContent() {
		return controlFlowContent;
	}
	/**
	 * ����������
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
