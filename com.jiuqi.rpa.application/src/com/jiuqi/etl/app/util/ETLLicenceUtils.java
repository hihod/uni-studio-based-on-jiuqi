package com.jiuqi.etl.app.util;

import com.jiuqi.etl.authz.ETLLicenceException;
import com.jiuqi.etl.authz.ETLLicenceManager;
import com.jiuqi.etl.storage.IStorageProvider;
import com.jiuqi.etl.storage.StorageException;
import com.jiuqi.etl.storage.StorageProviderManager;

/**
 * 
 * @author zhouxubo
 *
 */
public class ETLLicenceUtils {
	
	
	/**
	 * ETL系统注册授权工厂&加载授权数据
	 * @throws LicenceException
	 */
	public static void loadLicence() throws ETLLicenceException{	
		try {
			IStorageProvider storageProvider = StorageProviderManager.getInstance().getStorageProvider();
			byte[] bytes = storageProvider.getLicenceStorage().getLicence(ETLLicenceManager.PRODUCT_ID);
			if (bytes != null)					
				ETLLicenceManager.addLicence(bytes);
		} catch (StorageException e) {
			throw new ETLLicenceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 保存授权文件
	 * @param inStream 授权输入流
	 * @throws LicenceException
	 */
	public static void storeLicence(byte[] bytes) throws ETLLicenceException {				
		try {
			IStorageProvider storageProvider = StorageProviderManager.getInstance().getStorageProvider();
			storageProvider.getLicenceStorage().setLicence(ETLLicenceManager.PRODUCT_ID, bytes);
		} catch (StorageException e) {
			throw new ETLLicenceException(e.getMessage(), e);
		}		
	}
}
