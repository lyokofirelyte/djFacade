package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.JSONMap;
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
		
		main.getPanel(Resource.PANEL_SETTINGS).getGui().repaint();
		JSONMap settings = main.files.get(Resource.SETTINGS);
		
		switch (name){
		
			case "transparency":
				
				main.files.get(Resource.SETTINGS).set("transparency", ((JSlider) e.getSource()).getValue()/100f);
				update();
				
			break;
			
			case "color_slider_red": case "color_slider_green": case "color_slider_blue":
				
				settings.set(name, ((JSlider) e.getSource()).getValue()/255f);
				update();
				
			break;
		}
	}
	
	private void update(){
		
		JSONMap settings = main.files.get(Resource.SETTINGS);
		
		for (String thing : main.getPanel(Resource.MAIN).getGui().objects.keySet()){
			if (thing.contains("_panel")){
				main.getPanel(Resource.MAIN).getGui().getPanel(thing).setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), settings.getFloat("transparency")));
			}
		}
		
		main.getPanel(Resource.MAIN).getGui().repaint();
	}
}