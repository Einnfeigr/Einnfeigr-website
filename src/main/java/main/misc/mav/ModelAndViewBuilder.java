package main.misc.mav;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.web.servlet.ModelAndView;

import main.misc.Util;
import main.misc.mav.mustache.MustachePage;

public class ModelAndViewBuilder {

	private String basisPath;
	private HttpStatus status;
	private final Page page = new MustachePage(this);
	private static final String DEFAULT_INDEX_PATH = "basis";
	private final Map<String, Object> basisData = new HashMap<>();
	
	public ModelAndViewBuilder(Device device) {
		mobile(!device.isNormal());
	}
	
	public ModelAndViewBuilder mobile(boolean isMobile) {
		page.data("isMobile", isMobile);
		basisData.put("isMobile", isMobile);
		return this;
	}
	
	public ModelAndViewBuilder title(String title) {
		basisData.put("title", title);
		return this;
	}
	
	public ModelAndViewBuilder extraHref(String title, String href) {
		basisData.put("extra.title", title);
		basisData.put("extra.href", href);
		return this;
	}
	
	public ModelAndViewBuilder extraAjaxReload(String title,
			String url,
			String method,
			String successMessage,
			String errorMessage,
			String...data) {
		basisData.put("extra.title", title);
		StringBuilder dataString = new StringBuilder();
		for(int x = 0; x < data.length; x += 2) {
			if(x != 0) {
				dataString.append("&");
			}
			dataString.append(data[x]+"="+data[x+1]);
		}
		basisData.put("extra.onclick", "sendRequest('"+url+"', '"+method+
				"', '"+dataString+"', '"+successMessage+"', '"+errorMessage
				+"');"
				+ "location.reload();");
		return this;
	}
	
	public ModelAndViewBuilder extraAjax(String title, 
			String url,
			String method, 
			String successMessage,
			String errorMessage,
			String...data) {
		basisData.put("extra.title", title);
		StringBuilder dataString = new StringBuilder();
		for(int x = 0; x < data.length; x += 2) {
			if(x != 0) {
				dataString.append("&");
			}
			dataString.append(data[x]+"="+data[x+1]);
		}
		basisData.put("extra.onclick", "sendRequest('"+url+"', '"+method+
				"', '"+dataString+"', '"+successMessage+"', '"+errorMessage+"');");
		return this;
	}
	
	public ModelAndViewBuilder extraOnclick(String title, String onclick) {
		basisData.put("extra.title", title);
		basisData.put("extra.onclick", onclick);
		return this;
	}
	
	public ModelAndViewBuilder status(HttpStatus status) {
		this.status = status;
		return this;
	}
	
	public ModelAndViewBuilder data(Object...arg0) {
		basisData.putAll(Util.arrayToMap(arg0));
		return this;
	}
	
	public ModelAndViewBuilder extraLink(String title, String href) {
		basisData.put("extra.title", title);
		basisData.put("extra.href", href);
		return this;
	}
	
	public ModelAndViewBuilder extraOnClick(String title, String onclick) {
		basisData.put("extra.title", title);
		basisData.put("extra.onclick", onclick);
		return this;
	}

	public Page page() {
		return page;
	}
	
	public ModelAndViewBuilder excludeBasis(boolean excludeBasis) {
		if(excludeBasis) {
			basisData.put("includeBasis", excludeBasis);
		} else {
			basisData.remove("includeBasis");
		}
		return this;
	}
	
	public ModelAndView build() throws IOException {
		if(basisPath == null) {
			basisPath = DEFAULT_INDEX_PATH;
		}
		if(basisData.get("title") == null) {
			basisData.put("title", basisData.get("pageTitle"));
		}
		addExistsMark(basisData);
		addExistsMark(page.getData());
		data("page", page.compile());
		ModelAndView mav = new ModelAndView(basisPath);
		mav.getModel().putAll(basisData);
		if(status != null) {
			mav.setStatus(status);
		}
		return mav;
	}
	
	private Map<String, Object> addExistsMark(Map<String, Object> map) {
		Map<String, Object> tempMap = new HashMap<>();
		map.forEach((k, v) -> {
			if(v != null && v instanceof Collection 
					&& ((Collection<?>)v).size() > 0) {
				tempMap.put(k+".exists", true);
			}
		});
		map.putAll(tempMap);
		return map;
	}

}
