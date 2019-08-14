package com.jiuqi.etl.rpa.ui;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * RPA组件安装器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class RPAInstaller {
	static final String FILE_SEPARATOR = System.getProperty("file.separator");
	static final String RPA_WORKSPACE = "RPA";
	static final String BIN_FOLDER = "bin";
	static final String SELENIUM_FOLDER = "Selenium";
	static String dirPath = (String) System.getenv().get("APPDATA") + FILE_SEPARATOR + RPA_WORKSPACE;
	static String rpaRunningPath = System.getProperty("user.dir") + FILE_SEPARATOR + BIN_FOLDER;

	public static void install() {
		RPAInstaller installer = new RPAInstaller();

		File dirFile = new File(dirPath);
		if (!dirFile.exists())
			dirFile.mkdir();
		
		File seleniumDirFile = new File(dirPath + FILE_SEPARATOR + SELENIUM_FOLDER);
		if (!seleniumDirFile.exists())
			seleniumDirFile.mkdir();
		
		installer.installDll();
		installer.installDrivers();
	}

	public static void readfile(String filepath, List<File> files) {
		File file = new File(filepath);
		String[] filelist = file.list();
		for (int i = 0; i < filelist.length; i++) {
			File readfile = new File(filepath + "\\" + filelist[i]);
			if (!readfile.isDirectory()) {
				files.add(readfile);
			}
		}
	}

	private void installDll() {
		List<File> files = new ArrayList<File>();
		readfile(rpaRunningPath, files);
		if (files.isEmpty()) {
			return;
		}
		for (File file : files) {
			if (file.getName().toUpperCase().indexOf("DLL") > -1) {
				try {
					Files.copy(file.toPath(), new File(dirPath + FILE_SEPARATOR + file.getName()).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("安装dll文件失败");
				}
			}
		}
	}

	private void installDrivers() {
		List<File> files = new ArrayList<File>();
		readfile(rpaRunningPath + FILE_SEPARATOR + SELENIUM_FOLDER, files);
		if (files.isEmpty()) {
			return;
		}
		for (File file : files) {
			try {
				Files.copy(file.toPath(),
						new File(dirPath + FILE_SEPARATOR + SELENIUM_FOLDER + FILE_SEPARATOR + file.getName()).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("安装浏览器驱动文件失败");
			}
		}
	}

}
