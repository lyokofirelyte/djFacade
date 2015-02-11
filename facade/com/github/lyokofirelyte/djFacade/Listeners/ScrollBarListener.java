package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;

public class ScrollBarListener implements AR, AdjustmentListener {

	public DJFacade main;
	String type;
	
	public ScrollBarListener(DJFacade i, String type){
		main = i;
		this.type = type;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent bar) {
		
		switch (type){
		
			case "queue_bar":
				
				main.getPanel(Resource.PANEL_QUEUE).getGui().repaint();
				
			break;
			
			case "search_bar":
				
				main.getPanel(Resource.PANEL_SEARCH).getGui().repaint();
				
			break;
			
			case "chat_bar":
				
				main.getPanel(Resource.PANEL_CHAT).getGui().getEditorPane("textPane").repaint();
				
			break;
		
		}
	}
}