package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;

public class WindowListener implements AR {
	
	public DJFacade main;
	
	public WindowListener(DJFacade i){
		main = i;
	}
	
	public WindowAdapter adapter = new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			JSONMap settings = main.files.get(Resource.SETTINGS);
			settings.set("main_width", main.getPanel(Resource.MAIN).getGui().getPanel("settings_panel").getWidth());
			settings.set("main_height", main.getPanel(Resource.MAIN).getGui().getPanel("settings_panel").getHeight());
			main.tryCatch(main, "saveAll");
			NativeInterface.close();
			System.out.println("Shutting down...");
			System.exit(0);
		}
	};
}