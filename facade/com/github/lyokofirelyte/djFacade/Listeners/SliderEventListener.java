package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;

public class SliderEventListener implements ChangeListener {
	
	DJFacade main;
	String name;
	
	public SliderEventListener(DJFacade i, String name){
		this.name = name;
		main = i;
	}

	@Override
	public void stateChanged(ChangeEvent e){
		
		switch (name){
		
			case "transparency":
				
				main.files.get(Resource.SETTINGS).set("transparency", ((JSlider) e.getSource()).getValue()/100f);
				
				for (String thing : main.getPanel(Resource.MAIN).getGui().objects.keySet()){
					if (thing.contains("_panel")){
						main.getPanel(Resource.MAIN).getGui().getPanel(thing).setBackground(main.getEventListener().getColor(Color.decode(main.files.get(Resource.SETTINGS).getStr("color_tint"))));
					}
				}
				
				main.getPanel(Resource.MAIN).getGui().repaint();
				
			break;
			
			case "size":
				
				
			break;
		}
	}
}