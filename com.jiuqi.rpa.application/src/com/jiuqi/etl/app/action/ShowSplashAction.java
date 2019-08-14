/*
 * 北京久其软件有限公司 版权所有(c) 2009
 * 文件所含类：ShowSomethingAction
 * 作者：	宋雨明 	软件研究院组件部
 * 修改记录：
 * 2009-6-9 	宋雨明 	创建文件
 */
package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.ApplicationPlugin;

/**
 * 类名：显示闪屏<p>
 * @deprecated 使用{@link KeySequenceAction}代替
 */
public class ShowSplashAction extends Action implements IWorkbenchAction
{
	public static final String ID = "com.jiuqi.etl.showsplash";
	
	private static final String CODE = "showmetheteam";
	private IWorkbenchWindow window;
	private Listener listener;
	private StringBuffer keySequence; 
	private Image img;
	
	public ShowSplashAction(IWorkbenchWindow workbenchWindow)
    {
		window = workbenchWindow;
		setId("showsplash");
		setActionDefinitionId(ID);
		setEnabled(true);
		keySequence = new StringBuffer();
		img = ApplicationPlugin.imageDescriptorFromPlugin("icons/splash.jpg").createImage();
		listener = new Listener(){
			public void handleEvent(Event event)
            {
				keySequence.append(event.character);
				System.out.println("1");
				if (CODE.startsWith(keySequence.toString()))
				{
					System.out.println("2");
					event.type = SWT.NONE;
					if (CODE.equals(keySequence.toString()))
						showSplash();
					else
						return;
				}
				System.out.println("3");
				keySequence.setLength(0);
				Display.getCurrent().beep();
				Display.getCurrent().removeFilter(SWT.KeyDown, this);
            }
		};
    }
	
	private void showSplash()
    {
		final Shell shell = new Shell(window.getShell(), SWT.NO_TRIM);
		shell.setLayout(new FillLayout());
		Label imgLabel = new Label(shell, SWT.NONE);
		imgLabel.setImage(img);
		imgLabel.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent e)
			{
			    shell.close();
			}
		});
		shell.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.character == SWT.CR)
					shell.close();
			}
		});
		shell.addShellListener(new ShellAdapter(){
			@Override
			public void shellDeactivated(ShellEvent e)
			{
			    shell.close();
			}
		});
		shell.pack();
		Shell parentShell = (Shell) shell.getParent();
		Rectangle parentBounds = parentShell.getBounds();
		Point size = shell.getSize();
		int x = parentBounds.x + (parentBounds.width - size.x)/2;
		int y = parentBounds.y + (parentBounds.height - size.y)/2;
		shell.setLocation(x, y);
		shell.open();
    }
	
	@Override
	public void run()
	{
	    Display display = Display.getCurrent();
	    display.addFilter(SWT.KeyDown, listener);
	}
	
	public void dispose()
	{
		img.dispose();
	}
}
