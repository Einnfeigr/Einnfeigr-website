package main;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackReciever {

	@RequestMapping("api/drive/callback")
	public void handleCallback() {
		
	}
}
