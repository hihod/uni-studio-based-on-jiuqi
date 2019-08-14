package com.jiuqi.rpa.action;

/**
 * ÐéÄâ¼üÖµ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class VirtualKey {
	public static final int VK_LBUTTON        = 0x01;	//Êó±ê×ó¼ü
	public static final int VK_RBUTTON        = 0x02;	//Êó±êÓÒ¼ü 
	public static final int VK_CANCEL         = 0x03;	//Ctrl + Break
	public static final int VK_MBUTTON        = 0x04;	//Êó±êÖÐ¼ü/* NOT contiguous with L & RBUTTON */
	public static final int VK_BACK           = 0x08;	//Backspace ¼ü
	public static final int VK_TAB            = 0x09;	//Tab ¼ü
	public static final int VK_CLEAR          = 0x0C;
	public static final int VK_RETURN         = 0x0D;	//»Ø³µ¼ü
	public static final int VK_SHIFT          = 0x10;
	public static final int VK_CONTROL        = 0x11;
	public static final int VK_MENU           = 0x12;	//Alt ¼ü
	public static final int VK_PAUSE          = 0x13;
	public static final int VK_CAPITAL        = 0x14;	//Caps Lock ¼ü 
	public static final int VK_KANA           = 0x15;
	public static final int VK_HANGEUL        = 0x15;	/* old name - should be here for compatibility */
	public static final int VK_HANGUL         = 0x15;
	public static final int VK_JUNJA          = 0x17;
	public static final int VK_FINAL          = 0x18;
	public static final int VK_HANJA          = 0x19;
	public static final int VK_KANJI          = 0x19;
	public static final int VK_ESCAPE         = 0x1B;   //Esc ¼ü
	public static final int VK_CONVERT        = 0x1C;
	public static final int VK_NONCONVERT     = 0x1D;
	public static final int VK_ACCEPT         = 0x1E;
	public static final int VK_MODECHANGE     = 0x1F;
	public static final int VK_SPACE          = 0x20;   //¿Õ¸ñ
	public static final int VK_PRIOR          = 0x21;   //Page Up ¼ü
	public static final int VK_NEXT           = 0x22;   //Page Down ¼ü
	public static final int VK_END            = 0x23;   //End ¼ü
	public static final int VK_HOME           = 0x24;   //Home ¼ü
	public static final int VK_LEFT           = 0x25;	/*·½Ïò¼ü*/
	public static final int VK_UP             = 0x26;
	public static final int VK_RIGHT          = 0x27;
	public static final int VK_DOWN           = 0x28;
	public static final int VK_SELECT         = 0x29;
	public static final int VK_PRINT          = 0x2A;
	public static final int VK_EXECUTE        = 0x2B;
	public static final int VK_SNAPSHOT       = 0x2C;	//Print Screen ¼ü
	public static final int VK_INSERT         = 0x2D;	//Insert¼ü
	public static final int VK_DELETE         = 0x2E;	//Delete¼ü
	public static final int VK_HELP           = 0x2F;
	/* VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
	/* VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
	public static final int VK_LWIN           = 0x5B;	//×óWinKey(104¼üÅÌ²ÅÓÐ) 
	public static final int VK_RWIN           = 0x5C;	//ÓÒWinKey(104¼üÅÌ²ÅÓÐ)
	public static final int VK_APPS           = 0x5D;	//AppsKey(104¼üÅÌ²ÅÓÐ) 
	public static final int VK_NUMPAD0        = 0x60;	//Ð¡¼üÅÌ0-9
	public static final int VK_NUMPAD1        = 0x61;
	public static final int VK_NUMPAD2        = 0x62;
	public static final int VK_NUMPAD3        = 0x63;
	public static final int VK_NUMPAD4        = 0x64;
	public static final int VK_NUMPAD5        = 0x65;
	public static final int VK_NUMPAD6        = 0x66;
	public static final int VK_NUMPAD7        = 0x67;
	public static final int VK_NUMPAD8        = 0x68;
	public static final int VK_NUMPAD9        = 0x69;
	public static final int VK_MULTIPLY       = 0x6A;	//³Ë
	public static final int VK_ADD            = 0x6B;	//¼Ó
	public static final int VK_SEPARATOR      = 0x6C;	//³ý
	public static final int VK_SUBTRACT       = 0x6D;	//¼õ
	public static final int VK_DECIMAL        = 0x6E;	//Ð¡Êýµã
	public static final int VK_DIVIDE         = 0x6F;
	public static final int VK_F1             = 0x70;	//¹¦ÄÜ¼üF1-F24
	public static final int VK_F2             = 0x71;
	public static final int VK_F3             = 0x72;
	public static final int VK_F4             = 0x73;
	public static final int VK_F5             = 0x74;
	public static final int VK_F6             = 0x75;
	public static final int VK_F7             = 0x76;
	public static final int VK_F8             = 0x77;
	public static final int VK_F9             = 0x78;
	public static final int VK_F10            = 0x79;
	public static final int VK_F11            = 0x7A;
	public static final int VK_F12            = 0x7B;
	public static final int VK_F13            = 0x7C;
	public static final int VK_F14            = 0x7D;
	public static final int VK_F15            = 0x7E;
	public static final int VK_F16            = 0x7F;
	public static final int VK_F17            = 0x80;
	public static final int VK_F18            = 0x81;
	public static final int VK_F19            = 0x82;
	public static final int VK_F20            = 0x83;
	public static final int VK_F21            = 0x84;
	public static final int VK_F22            = 0x85;
	public static final int VK_F23            = 0x86;
	public static final int VK_F24            = 0x87;
	public static final int VK_NUMLOCK        = 0x90;	//Num Lock ¼ü
	public static final int VK_SCROLL         = 0x91;	//Scroll Lock ¼ü
	/*
	 * VK_L* & VK_R* - left and right Alt, Ctrl and Shift virtual keys.
	 * Used only as parameters to GetAsyncKeyState() and GetKeyState().
	 * No other API or message will distinguish left and right keys in this way.
	 */
	public static final int VK_LSHIFT          = 0xA0;
	public static final int VK_RSHIFT          = 0xA1;
	public static final int VK_LCONTROL        = 0xA2;
	public static final int VK_RCONTROL        = 0xA3;
	public static final int VK_LMENU           = 0xA4;
	public static final int VK_RMENU           = 0xA5;
	public static final int VK_PROCESSKEY      = 0xE5;
	public static final int VK_ATTN            = 0xF6;
	public static final int VK_CRSEL           = 0xF7;
	public static final int VK_EXSEL           = 0xF8;
	public static final int VK_EREOF           = 0xF9;
	public static final int VK_PLAY            = 0xFA;
	public static final int VK_ZOOM            = 0xFB;
	public static final int VK_NONAME          = 0xFC;
	public static final int VK_PA1             = 0xFD;
	public static final int VK_OEM_CLEAR       = 0xFE;
}
