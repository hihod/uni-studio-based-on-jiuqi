package com.jiuqi.rpa.widgets.uipicker;
import java.text.MessageFormat;	
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

public abstract class ClearableDialogCellEditor extends DialogCellEditor {

    public static final String CELL_EDITOR_IMG_DOTS_BUTTON = "cell_editor_dots_button_image";//$NON-NLS-1$

    private Composite editor;

    private Control contents;
    private Label defaultLabel;

    private Button button;
    private Button clearButton;
	private FocusListener buttonFocusListener;

    private Object value = null;

    static {
        ImageRegistry reg = JFaceResources.getImageRegistry();
        reg.put(CELL_EDITOR_IMG_DOTS_BUTTON, ImageDescriptor.createFromFile(
                DialogCellEditor.class, "images/dots_button.gif"));//$NON-NLS-1$
    }


    private class DialogCellLayout extends Layout {
        public void layout(Composite editor, boolean force) {
            Rectangle bounds = editor.getClientArea();
            Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            if (contents != null) {
				contents.setBounds(0, 0, bounds.width - 2*size.x, bounds.height);
			}
            button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
            clearButton.setBounds(bounds.width - 2*size.x, 0, size.x, bounds.height);
        }

        public Point computeSize(Composite editor, int wHint, int hHint,
                boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
            Point contentsSize = contents.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            Point result = new Point(buttonSize.x, Math.max(contentsSize.y,
                    buttonSize.y));
            return result;
        }
    }

    /**
     * Default DialogCellEditor style
     */
    private static final int defaultStyle = SWT.NONE;

    /**
     * Creates a new dialog cell editor with no control
     * @since 2.1
     */
    public ClearableDialogCellEditor() {
        setStyle(defaultStyle);
    }

 
    protected ClearableDialogCellEditor(Composite parent) {
        this(parent, defaultStyle);
    }


    protected ClearableDialogCellEditor(Composite parent, int style) {
        super(parent, style);
    }


    protected Button createButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("..."); //$NON-NLS-1$
        return result;
    }
    protected Button createClearButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("X"); //$NON-NLS-1$
        return result;
    }

    protected Control createContents(Composite cell) {
        defaultLabel = new Label(cell, SWT.LEFT);
        defaultLabel.setFont(cell.getFont());
        defaultLabel.setBackground(cell.getBackground());
        return defaultLabel;
    }


    protected Control createControl(Composite parent) {

        Font font = parent.getFont();
        Color bg = parent.getBackground();

        editor = new Composite(parent, getStyle());
        editor.setFont(font);
        editor.setBackground(bg);
        editor.setLayout(new DialogCellLayout());

        contents = createContents(editor);
        updateContents(value);
        clearButton = createClearButton(editor);
        clearButton.setFont(font);
        clearButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	clearBtnFunc();
                doSetValue("");
        		fireApplyEditorValue();
            }
        });

        button = createButton(editor);
        button.setFont(font);

        button.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.character == '\u001b') { // Escape
                    fireCancelEditor();
                }
            }
        });
        
        button.addFocusListener(getButtonFocusListener());
        
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
            	button.removeFocusListener(getButtonFocusListener());
                
            	Object newValue = openDialogBox(editor);
            	
            	button.addFocusListener(getButtonFocusListener());

            	if (newValue != null) {
                    boolean newValidState = isCorrect(newValue);
                    if (newValidState) {
                        markDirty();
                        doSetValue(newValue);
                    } else {
                        setErrorMessage(MessageFormat.format(getErrorMessage(),
                                new Object[] { newValue.toString() }));
                    }
                    fireApplyEditorValue();
                }
                
            }
        });

        setValueValid(true);

        return editor;
    }


    public void deactivate() {
    	if (button != null && !button.isDisposed()) {
    		button.removeFocusListener(getButtonFocusListener());
    	}
		super.deactivate();
	}


    protected Object doGetValue() {
        return value;
    }


    protected void doSetFocus() {
        button.setFocus();
        button.addFocusListener(getButtonFocusListener());
    }

    private FocusListener getButtonFocusListener() {
    	if (buttonFocusListener == null) {
    		buttonFocusListener = new FocusListener() {


				public void focusGained(FocusEvent e) {
					// Do nothing
				}

				public void focusLost(FocusEvent e) {
					ClearableDialogCellEditor.this.focusLost();
				}
    		};
    	}
    	
    	return buttonFocusListener;
	}


    protected void doSetValue(Object value) {
        this.value = value;
        updateContents(value);
    }

    protected Label getDefaultLabel() {
        return defaultLabel;
    }


    protected abstract Object openDialogBox(Control cellEditorWindow);
    protected abstract Object clearBtnFunc();

    protected void updateContents(Object value) {
        if (defaultLabel == null) {
			return;
		}

        String text = "";//$NON-NLS-1$
        if (value != null) {
			text = value.toString();
		}
        defaultLabel.setText(text);
    }
}
