package test.home.jfinal.start;

import test.home.jfinal.controller.SheetController;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.template.Engine;

public class JFinalStarter extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/sheet", SheetController.class, "");
	}

	@Override
	public void configEngine(Engine me) {
		me.addSharedFunction("/static/common/common.html");
	}

	@Override
	public void configPlugin(Plugins me) {
		
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {

	}

	public static void main(String[] args) {
		JFinal.start("WebContent", 8080, "/", 5);
	}

}
