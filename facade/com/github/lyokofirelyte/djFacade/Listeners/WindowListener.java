package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;

public class WindowListener implements AR {
	
	public DJFacade main;
	
	public WindowListener(DJFacade i){
		main = i;
	}
	
	public WindowAdapter adapter = new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			main.tryCatch(main, "saveAll");
			System.out.println("Shutting down...");
			System.exit(0);
		}
	};
}