package main.img.preview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.drive.DriveUtils;
import main.http.RequestBuilder;
import main.misc.Util;

@RestController
public class DriveImageController implements ImageController {

	private Logger logger = LoggerFactory.getLogger(DriveImageController.class);
	private Map<String, String> cache = new HashMap<>();
	private static boolean isCachingAvailable = true;
	
	public DriveImageController() throws FileNotFoundException, IOException {
		//getImage(PortfolioDriveDao.getRootId());
	}
	
	@Override
	public ModelAndView getPreview(String size, String id) {
		return null;
	}

	@Override 
	public @ResponseBody String getImage(HttpServletResponse response, 
			@PathVariable String id) throws FileNotFoundException, IOException {
		StringBuilder content = new StringBuilder();
		if(!isCachingAvailable) {
			response.sendRedirect(DriveUtils.getClientDownloadUrl(id));
			return null;
		}
		if(cache.containsKey(id)) {
			File file = new File(cache.get(id));
			try(BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)))) {
				while(br.ready()) {
					content.append(br.read()); 
				}
			}
		} else {
			String url;
			try {
				url = DriveUtils.getServerDownloadUrl(id); 
				content.append(RequestBuilder.performGet(url));
				File dir = new File("/imgCache/");
				dir.mkdirs();
				File file = new File(dir, id);
				if(!file.exists()) {
					file.createNewFile();
				}
				try(BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file)))) {
					bw.write(content.toString());
				}
				cache.put(id, file.getAbsolutePath());	
			} catch(IOException e) {
				isCachingAvailable = false;
				logger.error("Unable to download data for file with id "+id);
				logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
				response.sendRedirect(DriveUtils.getClientDownloadUrl(id));
				return null;
			}	
		}
		ModelAndView mav = new ModelAndView();
		mav.getModel().put("body", content.toString());
		return content.toString();	
	}
}
