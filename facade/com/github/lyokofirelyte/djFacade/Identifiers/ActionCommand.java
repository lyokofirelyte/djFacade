package com.github.lyokofirelyte.djFacade.Identifiers;

public enum ActionCommand {

	SETUP_TEXT("SETUP_TEXT"),
	COLOR("COLOR"),
	TOOL_TIPS("TOOL_TIPS"),
	ON_TOP("ON_TOP"),
	RESET("RESET");
	
	ActionCommand(String type){
		this.type = type;
	}
	
	String type;
	
	public String getType(){
		return type;
	}
}