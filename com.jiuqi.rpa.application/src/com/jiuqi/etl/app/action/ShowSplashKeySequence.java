/*
 * 2010-9-16
 *
 * Õı–«”Ó
 */
package com.jiuqi.etl.app.action;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import com.jiuqi.etl.app.ApplicationPlugin;

/**
 * @author Õı–«”Ó
 *
 */
public class ShowSplashKeySequence implements KeySequenceExecutor {

	private Image img;
	
	public ShowSplashKeySequence() {
		img = ApplicationPlugin.imageDescriptorFromPlugin("icons/splash.jpg").createImage();
	}
	
	public void dispose() {
		img.dispose();
	}
	
	public String getActivationString() {
		return "showmetheteam";
	}
	
	public void run(IWorkbenchWindow window) {
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
	};
}
