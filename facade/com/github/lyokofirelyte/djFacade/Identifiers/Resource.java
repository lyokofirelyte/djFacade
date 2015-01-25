package com.github.lyokofirelyte.djFacade.Identifiers;

import com.github.lyokofirelyte.djFacade.Panels.PanelChat;
import com.github.lyokofirelyte.djFacade.Panels.PanelChatBar;
import com.github.lyokofirelyte.djFacade.Panels.PanelLogin;
import com.github.lyokofirelyte.djFacade.Panels.PanelMain;
import com.github.lyokofirelyte.djFacade.Panels.PanelMainUnlocked;
import com.github.lyokofirelyte.djFacade.Panels.PanelQueue;
import com.github.lyokofirelyte.djFacade.Panels.PanelSearch;
import com.github.lyokofirelyte.djFacade.Panels.PanelSearchBar;
import com.github.lyokofirelyte.djFacade.Panels.PanelSettings;
import com.github.lyokofirelyte.djFacade.Panels.PanelVideo;

public enum Resource {

	SETTINGS("data/settings.json"),
	PANEL_LOGIN(PanelLogin.class.toString()),
	MAIN(PanelMain.class.toString()),
	PANEL_SEARCH(PanelSearch.class.toString()),
	MAIN_UNLOCKED(PanelMainUnlocked.class.toString()),
	PANEL_SETTINGS(PanelSettings.class.toString()),
	PANEL_QUEUE(PanelQueue.class.toString()),
	PANEL_SEARCH_BAR(PanelSearchBar.class.toString()),
	PANEL_VIDEO(PanelVideo.class.toString()),
	PANEL_CHAT(PanelChat.class.toString()),
	PANEL_CHAT_BAR(PanelChatBar.class.toString());
	
	Resource(String name){
		type = name;
	}
	
	String type;
	
	public String getType(){
		return type;
	}
}