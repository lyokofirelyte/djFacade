package com.github.lyokofirelyte.djFacade.Identifiers;

public enum ActionCommand {

	SETUP_TEXT("SETUP_TEXT"),
	COLOR("COLOR"),
	TOOL_TIPS("TOOL_TIPS"),
	ON_TOP("ON_TOP"),
	RESET("RESET"),
	PANEL("PANEL"),
	SEARCH_BAR("SEARCH_BAR"),
	VIDEO("VIDEO"),
	TEXT("TEXT"),
	CHAT_BAR("CHAT_BAR");
	
	ActionCommand(String type){
		this.type = type;
	}
	
	String type;
	
	public String getType(){
		return type;
	}
}