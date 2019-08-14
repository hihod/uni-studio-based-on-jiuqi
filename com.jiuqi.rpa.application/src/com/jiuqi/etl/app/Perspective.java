package com.jiuqi.etl.app;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.jiuqi.etl.ui.view.parameter.ParameterView;
import com.jiuqi.etl.ui.view.problem.ProblemView;
import com.jiuqi.etl.ui.view.server.ServerView;
import com.jiuqi.etl.ui.view.thumbnail.ThumbnailView;


public class Perspective implements IPerspectiveFactory
{
	public static final String LEFT_FOLDER = "com.jiuqi.etl.left";
	public static final String BOTTOM_FOLDER = "com.jiuqi.etl.bottom";
	public static final String RIGHT_FOLDER = "com.jiuqi.etl.right";
	public static final String RIGHT_TOP_FOLDER = "com.jiuqi.etl.right.top";
	public static final String RIGHT_BOTTOM_FOLDER = "com.jiuqi.etl.right.bottom";
	
	private IFolderLayout leftFolder;
	private IFolderLayout bottomFolder;
	private IFolderLayout rightFolder;
	private IFolderLayout rightBottomFolder;
	
	public void createInitialLayout(IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();
		//左边，服务器视图
		leftFolder = layout.createFolder(LEFT_FOLDER, IPageLayout.LEFT, 0.25f, editorArea);
		leftFolder.addView(ServerView.VIEW_ID);
		
		//下面，问题视图和控制台视图
		bottomFolder = layout.createFolder(BOTTOM_FOLDER, IPageLayout.BOTTOM, 0.75f, editorArea);
		bottomFolder.addView(ProblemView.VIEW_ID);
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottomFolder.addView(ParameterView.VIEW_ID);
		
		//右边，大纲视图和属性视图
		rightFolder = layout.createFolder(RIGHT_FOLDER, IPageLayout.RIGHT, 0.7f, editorArea);
		rightFolder.addView(IPageLayout.ID_OUTLINE);
		rightFolder.addView(ThumbnailView.VIEW_ID);
		rightBottomFolder = layout.createFolder(RIGHT_BOTTOM_FOLDER, IPageLayout.BOTTOM, 0.5f, RIGHT_FOLDER);
		rightBottomFolder.addView(IPageLayout.ID_PROP_SHEET);
	}
}
