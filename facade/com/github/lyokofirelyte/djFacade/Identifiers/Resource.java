package com.github.lyokofirelyte.djFacade.Identifiers;

import com.github.lyokofirelyte.djFacade.Panels.PanelLogin;
import com.github.lyokofirelyte.djFacade.Panels.PanelMain;
import com.github.lyokofirelyte.djFacade.Panels.PanelMainUnlocked;
import com.github.lyokofirelyte.djFacade.Panels.PanelSettings;

public enum Resource {

	SETTINGS("data/settings.json"),
	PANEL_LOGIN(PanelLogin.class.toString()),
	MAIN(PanelMain.class.toString()),
	MAIN_UNLOCKED(PanelMainUnlocked.class.toString()),
	PANEL_SETTINGS(PanelSettings.class.toString());
	
	Resource(String name){
		type = name;
	}
	
	String type;
	
	public String getType(){
		return type;
	}
}