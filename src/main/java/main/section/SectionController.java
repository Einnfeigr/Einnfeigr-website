package main.section;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.drive.DriveDao;
import main.img.ImageDataController;
import main.misc.RequestUtils;
import main.misc.Util;

public class SectionController {
	
	private final static Logger logger = 
			LoggerFactory.getLogger(ImageDataController.class);
	
	private static Section mainSection;
	private static Map<String, Section> sections;
	private DriveDao dao;
	
	public SectionController(DriveDao dao) {
		this.dao = dao;
		loadSections();
	}
	
	public Map<String, Section> getSections() {
		return sections;
	}
	
	public Section getMainSection() {
		return mainSection;
	}
	
	public Section getSection(String id) throws NullPointerException {
		if(!sections.containsKey(id)) {
			throw new NullPointerException("'"+id+"' section can't be found");
		}
		return sections.get(id);
	}
	
	public void loadSections() {
		try {
			List<Section> sectionList = dao.getAllFolders();
			sections = new HashMap<>();
			sectionList.forEach(s -> {
				if(s.getId().equals(RequestUtils.rootId)) {
					mainSection = s;
				}
				sections.put(s.getId(), s);
			});
		} catch(IOException e) {
			logger.error(Util.EXCEPTION_LOG_MESSAGE, e);
		} finally {
			if(sections == null) {
				sections = new HashMap<>();
			}
		}
	}
	
}