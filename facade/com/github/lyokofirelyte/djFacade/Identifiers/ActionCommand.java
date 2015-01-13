package com.github.lyokofirelyte.djFacade.Identifiers;

public enum ActionCommand {

	SETUP_TEXT("SETUP_TEXT");
	
	ActionCommand(String type){
		this.type = type;
	}
	
	String type;
	
	public String getType(){
		return type;
	}
}