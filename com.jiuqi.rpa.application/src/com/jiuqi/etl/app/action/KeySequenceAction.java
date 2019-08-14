/*
 * 2010-9-16
 *
 * 王星宇
 */
package com.jiuqi.etl.app.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * 通过键盘输入一系列字符,根据输入的字符串执行相应的动作
 * @author 王星宇
 *
 */
public class KeySequenceAction extends Action implements IWorkbenchAction {
	
	public static final String ID = "com.jiuqi.etl.keysequence";
	private IWorkbenchWindow window;
	private StringBuffer keySequence;
	private Listener listener;
	private List<KeySequenceExecutor> executors;

	public KeySequenceAction(IWorkbenchWindow workbenchWindow)
    {
		window = workbenchWindow;
		setId(ID);
		setActionDefinitionId(ID);
		setEnabled(true);
		keySequence = new StringBuffer();
		executors = new ArrayList<KeySequenceExecutor>();
//		img = ApplicationPlugin.imageDescriptorFromPlugin("icons/splash.jpg").createImage();
		listener = new Listener(){
			public void handleEvent(Event event)
            {
				keySequence.append(event.character);
				String seq = keySequence.toString();
				boolean flag = false;
				for (KeySequenceExecutor exe : executors) {
					if (exe.getActivationString().startsWith(seq)) {
						flag = true;
						event.type = SWT.NONE;
						if (exe.getActivationString().equals(seq)) {
							exe.run(window);
							break;
						}
					}
				}
				
//				if (CODE.startsWith())
//				{
//					event.type = SWT.NONE;
//					if (CODE.equals(keySequence.toString()))
//						showSplash();
//					else
//						return;
//				}
				if (!flag) {
					keySequence.setLength(0);
					window.getWorkbench().getDisplay().beep();
					window.getWorkbench().getDisplay().removeFilter(SWT.KeyDown, this);
				}
            }
		};
    }
	
	public void addKeySequenceExecutor(KeySequenceExecutor exe) {
		executors.add(exe);
	}
	
	public void removeKeySequenceExecutor(KeySequenceExecutor exe) {
		executors.remove(exe);
	}
	
	@Override
	public void run() {
		Display display = window.getWorkbench().getDisplay();
		display.addFilter(SWT.KeyDown, listener);
	}
	
	public void dispose() {
		for (KeySequenceExecutor exe : executors) {
			exe.dispose();
		}
		
		executors.clear();
	}

}
