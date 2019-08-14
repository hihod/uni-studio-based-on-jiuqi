package com.jiuqi.rpa.action.application.killprocess;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.application.UIAApplicationLibary;

/**
 * ����رս���
 * 
 * @author liangxiao01
 */
public class KillProcessAction extends Action {
	private KillProcessInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public KillProcessAction(KillProcessInput input) {
		super(input);
		
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		UIAApplicationLibary library = new UIAApplicationLibary(getContext());
		try {
			long[] id = library.findProcess(input.getProcessName());
			for (long l : id) {
				library.killProcess(l);
			}
		} catch (LibraryException e) {
			throw new ActionException("�رս��̻�쳣", e);
		}
		
		return new KillProcessOutput();
	}

}
