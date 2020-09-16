package main.http;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

	public static Map<String, String> stringsToMap(String[] text) {
		Map<String, String> map = new HashMap<>();
		for(int x = 0; x < text.length; x += 2) {
			map.put(text[x], text[x+1]);
		}
		return map;
	}
	
}
