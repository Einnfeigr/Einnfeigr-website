package main.dashboard;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {

	@Autowired
	ZipImageStorageService storageService;
	
	@RequestMapping(value= "/dashboard/download/{filePath}", method=RequestMethod.GET)
	public ResponseEntity<Resource> downloadFile(@PathVariable String filePath) {
		Resource file = storageService.loadAsResource(filePath);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@RequestMapping(value="/dashboard/upload", method=RequestMethod.GET)
	public ModelAndView showUploadForm() {
		return new ModelAndView("dashboard/upload");
	}
	
	@RequestMapping(value="/dashboard/upload", method=RequestMethod.POST)
	public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
		if(path == null) {
			path = ((File)file).getParentFile().getAbsolutePath();
		}
		try {
			storageService.store(file, path);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION).body(null);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
		
	}
}
