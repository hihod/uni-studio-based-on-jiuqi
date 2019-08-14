package com.jiuqi.rpa.lib.browser;


/**
 * ‰Ø¿¿∆˜¿‡–Õ√∂æŸ
 * @author wangshanyu
 *
 */
public enum WebBrowserType {

	FIREFOX("firefox", 1), EDGE("edge", 2), CHROME("chrome", 3),IE("ie", 4),PHANTOMJS("phantomjs", 5);
 
    public String value;
    public int key;
 
    WebBrowserType(String value, int key) {
        this.value = value;
        this.key = key;
    }
 
    public String getValue(){
    	return this.value;
    }
    public int getKey(){
    	return this.key;
    }
    /**
	 * <em>priavte use</em>,parse process name to browser type
	 * @param pn
	 * @return {@link WebBrowserType}
	 */
	public static WebBrowserType parseProcessNameToBrowserType(String pn){
		if(pn.toLowerCase().indexOf("chrome")!=-1){
			return WebBrowserType.CHROME;
		}
		if(pn.toLowerCase().indexOf("iexplore")!=-1){
			return WebBrowserType.IE;
		}
		if(pn.toLowerCase().indexOf("firefox")!=-1){
			return WebBrowserType.FIREFOX;
		}
		if(pn.toLowerCase().indexOf("edge")!=-1){
			return WebBrowserType.EDGE;
		}
		if(pn.toLowerCase().indexOf("phantom")!=-1){
			return WebBrowserType.PHANTOMJS;
		}
		return null;
	}
	/**
	 * @param type
	 * @return
	 */
	public static String parseTypeToProcessName(WebBrowserType type){
		switch (type) {
		case IE:
			return "iexplore.exe";
		case EDGE:
			return "MicrosoftEdge.exe";
		case FIREFOX:
			return "firefox.exe";
		case CHROME:
			return "chrome.exe";
		case PHANTOMJS:
			return "phantomjs.exe";
		default:
			break;
		}
		return "";
	}
	
}


