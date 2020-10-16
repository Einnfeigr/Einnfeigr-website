package main.controller.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.album.AlbumController;
import main.misc.mav.ModelAndViewBuilder;
import main.template.TemplateFactory;

@RestController
public class ViewController {

	@Autowired
	private AlbumController albumController;
	
	@GetMapping("/")
	public ModelAndView showMain(Device device,
			@RequestHeader(value="target", required=false, defaultValue="all")
				String target
			) throws IOException {
		return new ModelAndViewBuilder(device)
				.page()
					.path("page/main")
					.and()
				.title("Главная")
				.excludeBasis(target.equals("body"))
				.build();
	}
	
	@GetMapping("/portfolio")
	public ModelAndView showPortfolio(Device device,
			@RequestHeader(value="target", required=false, defaultValue="all")
				String target
			) throws IOException {
		return new ModelAndViewBuilder(device)
				.page()
					.path("page/portfolio")
					.data("albums", TemplateFactory.createAlbumListTemplate(
							albumController.getRootAlbums()))
					.and()
				.title("Портфолио")
				.excludeBasis(target.equals("body"))
				.build();
	}
	
	@GetMapping("/about")
	public ModelAndView showAbout(Device device,
			@RequestHeader(value="target", required=false, defaultValue="all")
				String target,
			HttpServletResponse response
			) throws IOException {
		ModelAndView mav = new ModelAndViewBuilder(device)
				.page()
					.path("page/about")
					.and()
				.title("О сайте")
				.excludeBasis(target.equals("body"))
				.build();
		mav.getModel().put("title", "О сайте");
		return mav;
	}
	
}
