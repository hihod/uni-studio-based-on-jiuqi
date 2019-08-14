/*
 * ��������������޹�˾ ��Ȩ����(c) 2009
 * �ļ������ࣺApplicationConsole
 * ���ߣ�	������ 	����о�Ժ�����
 * �޸ļ�¼��
 * 2009-6-12 	������ 	�����ļ�
 */
package com.jiuqi.etl.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jiuqi.etl.app.util.AppUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ETLEngineManager;
import com.jiuqi.etl.engine.IExecutor;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.bean.ControlFlowBean;
import com.jiuqi.etl.model.bean.ControlFlowFolderBean;
import com.jiuqi.etl.model.bean.SchemeBean;
import com.jiuqi.etl.storage.IControlFlowStorage;
import com.jiuqi.etl.storage.ISchemeStorage;
import com.jiuqi.etl.storage.IStorageProvider;
import com.jiuqi.etl.storage.StorageProviderManager;

/**
 * ������ʹ�ÿ���̨�������С�
 */
public class ApplicationConsole
{
	private static final String ARG_SERVER = "-server";
	private static final String ARG_USERNAME = "-uname";
	private static final String ARG_PASSWORD = "-pword";
	private static final String ARG_CONTROLFLOW = "-controlflow";
	
	private static String[] arguments;
	private static String serverURL;
	private static String username;
	private static String password;
	private static String controlflowPath;
	
	public static void run(String[] args)
	{
		arguments = args;
		serverURL = getArgument(ARG_SERVER);
		if (serverURL == null)
		{
			System.out.print("�������������ַ��");
			serverURL = getInput();
		}
		username = getArgument(ARG_USERNAME);
		if (username == null)
		{
			System.out.print("�������û�����");
			username = getInput();
		}
		password = getArgument(ARG_PASSWORD);
		if (password == null)
		{
			System.out.print("���������룺");
			password = getInput();
		}
		controlflowPath = getArgument(ARG_CONTROLFLOW);
		if (controlflowPath == null)
		{
			System.out.print("������Ҫִ�еĿ���������������/[�ļ�������/]���������ƣ���");
			controlflowPath = getInput();
		}
		StorageProviderManager storageMgr = StorageProviderManager.getInstance();
		try
        {
	        if(storageMgr.loginEx(serverURL, username, password, AppUtils.getVersion()))
	        {
	        	String[] pathSegment = controlflowPath.split("/");
	        	if (pathSegment.length >= 2)
	        	{
		        	String schemeGuid = null;
		        	IStorageProvider storageProvider = storageMgr.getStorageProvider();
		        	ISchemeStorage schemeStorage = storageProvider.getSchemeStorage(null);
		        	SchemeBean[] schemes = schemeStorage.getAllScheme();
		        	for (SchemeBean scheme : schemes)
		        	{
		        		if (scheme.getTitle().equals(pathSegment[0]))
		        		{
		        			schemeGuid = scheme.getGuid();
		        			break;
		        		}
		        	}
		        	if (schemeGuid != null)
		        	{
		        		IControlFlowStorage cfStorage = storageProvider.getControlFlowStorage(schemeGuid);
		        		String folderGuid = "";
		        		for(int i = 1; i < pathSegment.length-1; i++)
		        		{
		        			ControlFlowFolderBean[] folders = cfStorage.getChildControlFlowFolders(folderGuid);
		        			for (ControlFlowFolderBean folder : folders)
		        			{
		        				if (folder.getTitle().equalsIgnoreCase(pathSegment[i]))
		        				{
		        					folderGuid = folder.getGuid();
		        					break;
		        				}
		        			}
		        		}
		        		ControlFlowBean[] contorlflows = cfStorage.getChildControlFlows(folderGuid);
		        		String controlFlowGuid = null;
		        		for(ControlFlowBean controlflow : contorlflows)
		        		{
		        			if(controlflow.getTitle().equalsIgnoreCase(pathSegment[pathSegment.length-1]))
		        			{
		        				controlFlowGuid = controlflow.getGuid();
		        				break;
		        			}
		        		}
		        		if (controlFlowGuid != null)
		        		{
		        			ControlFlowModel model = cfStorage.getControlFlowData(controlFlowGuid);
		        			IExecutor exe = ETLEngineManager.getExecutor(model);
		    	        	try
		                    {
		    	                exe.start(null);
		                    }
		                    catch (ETLEngineException e)
		                    {
		    	                System.out.println("ִ�г���");
		                    }
		        		}
		        		else
		        			System.out.println("������·������");
		        	}
		        	else
		        		System.out.println("������·������");
	        	}
	        	else
	        		System.out.println("������·������");
	        }
	        else
	        	System.out.println("�û��������������");
        }
        catch (Exception e)
        {
	        System.out.println("�޷����ӷ�������");
        }
        clear();
	}
	
	private static String getArgument(String key)
	{
		if(arguments != null)
		{
			for(int i = 0; i < arguments.length; i++)
			{
				String arg = arguments[i];
				if (arg.equals(key) && i+1 < arguments.length)
				{
					return arguments[i+1];
				}
			}
		}
		return null;
	}
	
	private static String getInput()
	{
		String input  = null;
		InputStreamReader stdin = new InputStreamReader(System.in);
		BufferedReader bufin = new BufferedReader(stdin);
		try
        {
	        input = bufin.readLine();
        }
        catch (IOException e){
        }
		return input;
	}
	
	private static void clear()
	{
		arguments = null;
		serverURL = null;
		username = null;
		password = null;
		controlflowPath = null;
	}
	
}
