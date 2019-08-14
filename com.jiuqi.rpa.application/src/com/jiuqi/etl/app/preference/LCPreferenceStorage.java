package com.jiuqi.etl.app.preference;

import java.util.List;

import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.preference.IPreference;
import com.jiuqi.etl.preference.PreferenceModel;

/**
 * @author dangyingguang
 * 
 * ϵͳѡ��ͻ��˱���ģʽÿ��ֻ�ܻ�ȡһ��ϵͳѡ�ÿ��Ҳֻ�ܱ���һ��
 */
public class LCPreferenceStorage implements IPreference{

	public PreferenceModel getPreferenceByCateAndName(String category, String name) throws Exception {
		String value = ApplicationPlugin.getDefault().getPreferenceStore().getString(name);
		PreferenceModel model = new PreferenceModel();
		model.setCategory(category);
		model.setCategory(name);
		model.setValue(value);
		return model;
	}

	public List<PreferenceModel> getPreferenceByCategory(String category) throws Exception {
		return null;
	}

	public List<PreferenceModel> getAllPreference() throws Exception {
		return null;
	}

	public void savePreference(List<PreferenceModel> models) throws Exception {
	}

	public void savePreference(PreferenceModel model) throws Exception {
		ApplicationPlugin.getDefault().getPreferenceStore().setValue(model.getName(), model.getValue());
	}

	public String getPreferenceValue(String category, String name) throws Exception {
		return ApplicationPlugin.getDefault().getPreferenceStore().getString(name);
	}
}
