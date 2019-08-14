package com.jiuqi.etl.rpa.ui.keyboard.typesecuretext;

import java.text.MessageFormat;	// Not using ICU to support standalone JFace scenario

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jiuqi.bi.util.JqLib;

/**
 * A cell editor that manages a text entry field.
 * The cell editor's value is the text string itself.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class SecureTextCellEditor extends CellEditor {

    /**
     * The text control; initially <code>null</code>.
     */
    protected Text text;
    private ModifyListener modifyListener;

    private boolean isSelection = false;

    private boolean isDeleteable = false;

    private boolean isSelectable = false;

    private static final int defaultStyle = SWT.SINGLE | SWT.PASSWORD;

    public SecureTextCellEditor() {
        setStyle(defaultStyle);
    }

    public SecureTextCellEditor(Composite parent) {
        this(parent, defaultStyle);
    }

    public SecureTextCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    private void checkDeleteable() {
        boolean oldIsDeleteable = isDeleteable;
        isDeleteable = isDeleteEnabled();
        if (oldIsDeleteable != isDeleteable) {
            fireEnablementChanged(DELETE);
        }
    }

    private void checkSelectable() {
        boolean oldIsSelectable = isSelectable;
        isSelectable = isSelectAllEnabled();
        if (oldIsSelectable != isSelectable) {
            fireEnablementChanged(SELECT_ALL);
        }
    }

    private void checkSelection() {
        boolean oldIsSelection = isSelection;
        isSelection = text.getSelectionCount() > 0;
        if (oldIsSelection != isSelection) {
            fireEnablementChanged(COPY);
            fireEnablementChanged(CUT);
        }
    }

    protected Control createControl(Composite parent) {
        text = new Text(parent, getStyle() | SWT.PASSWORD);
        text.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                handleDefaultSelection(e);
            }
        });
        text.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201  
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);

                // as a result of processing the above call, clients may have
                // disposed this cell editor
                if ((getControl() == null) || getControl().isDisposed()) {
					return;
				}
                checkSelection(); // see explanation below
                checkDeleteable();
                checkSelectable();
            }
        });
        text.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
        // We really want a selection listener but it is not supported so we
        // use a key listener and a mouse listener to know when selection changes
        // may have occurred
        text.addMouseListener(new MouseAdapter() {
            public void mouseUp(MouseEvent e) {
                checkSelection();
                checkDeleteable();
                checkSelectable();
            }
        });
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	SecureTextCellEditor.this.focusLost();
            }
        });
        text.setFont(parent.getFont());
        text.setBackground(parent.getBackground());
        text.setText("");//$NON-NLS-1$
        text.addModifyListener(getModifyListener());
        return text;
    }

    protected Object doGetValue() {
        return text.getText();
    }

    protected void doSetFocus() {
        if (text != null) {
            text.selectAll();
            text.setFocus();
            checkSelection();
            checkDeleteable();
            checkSelectable();
        }
    }
    protected void doSetValue(Object value) {
        Assert.isTrue(text != null && (value instanceof String));
        text.removeModifyListener(getModifyListener());
        text.setText(JqLib.decodePassword((String) value));
        //text.setText(JqLib.decodePassword(""));//鼠标点击进入清空
        text.addModifyListener(getModifyListener());
    }
    @SuppressWarnings("unused")
	protected void editOccured(ModifyEvent e) {
        String value = text.getText();
        if (value == null) {
			value = "";//$NON-NLS-1$
		}
        Object typedValue = value;
        boolean oldValidState = isValueValid();
        boolean newValidState = isCorrect(typedValue);
        if (typedValue == null && newValidState) {
			Assert.isTrue(false,"Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
        if (!newValidState) {
            setErrorMessage(MessageFormat.format(getErrorMessage(),
                    new Object[] { value }));
        }
        valueChanged(oldValidState, newValidState);
    }

    public LayoutData getLayoutData() {
        return new LayoutData();
    }

    private ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    editOccured(e);
                }
            };
        }
        return modifyListener;
    }

    protected void handleDefaultSelection(SelectionEvent event) {
        fireApplyEditorValue();
        deactivate();
    }

    public boolean isCopyEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return text.getSelectionCount() > 0;
    }

    public boolean isCutEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return text.getSelectionCount() > 0;
    }

    public boolean isDeleteEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return text.getSelectionCount() > 0
                || text.getCaretPosition() < text.getCharCount();
    }

    public boolean isPasteEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return true;
    }

    public boolean isSaveAllEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return true;
    }

    public boolean isSelectAllEnabled() {
        if (text == null || text.isDisposed()) {
			return false;
		}
        return text.getCharCount() > 0;
    }

    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\r') { 
            if (text != null && !text.isDisposed()
                    && (text.getStyle() & SWT.MULTI) != 0) {
                if ((keyEvent.stateMask & SWT.CTRL) != 0) {
                    super.keyReleaseOccured(keyEvent);
                }
            }
            return;
        }
        super.keyReleaseOccured(keyEvent);
    }

    public void performCopy() {
        text.copy();
    }

    public void performCut() {
        text.cut();
        checkSelection();
        checkDeleteable();
        checkSelectable();
    }

    public void performDelete() {
        if (text.getSelectionCount() > 0) {
            text.insert(""); //$NON-NLS-1$
		} else {
            int pos = text.getCaretPosition();
            if (pos < text.getCharCount()) {
                text.setSelection(pos, pos + 1);
                text.insert(""); //$NON-NLS-1$
            }
        }
        checkSelection();
        checkDeleteable();
        checkSelectable();
    }

    public void performPaste() {
        text.paste();
        checkSelection();
        checkDeleteable();
        checkSelectable();
    }

    public void performSelectAll() {
        text.selectAll();
        checkSelection();
        checkDeleteable();
    }

	protected boolean dependsOnExternalFocusListener() {
		return getClass() != SecureTextCellEditor.class;
	}
}
