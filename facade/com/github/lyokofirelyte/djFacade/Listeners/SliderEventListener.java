package com.github.lyokofirelyte.djFacade.Listeners;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
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
				
				if (settings.getBool("applyPanel")){
					settings.set(name, ((JSlider) e.getSource()).getValue()/255f);
				}
				
				if (settings.getBool("applyText")){
					settings.set(name + "_text",((JSlider) e.getSource()).getValue());
				}
				
				update();
				
			break;
		}
	}
	
	private void update(){
		
		JSONMap settings = main.files.get(Resource.SETTINGS);
		
		if (settings.getBool("applyPanel")){
			for (String thing : main.getPanel(Resource.MAIN).getGui().objects.keySet()){
				if (thing.contains("_panel")){
					main.getPanel(Resource.MAIN).getGui().getPanel(thing).setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), settings.getFloat("transparency")));
				}
			}
		}
		
		if (settings.getBool("applyText")){
			main.getPanel(Resource.MAIN).getGui().getPanel("info").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "djFacade" + main.loadStyle("setup_end.dj")));
		}
		
		main.getPanel(Resource.MAIN).getGui().repaint();
		GUI gui = main.getPanel(Resource.PANEL_SETTINGS).getGui();
		
		try {
			gui.getCheckBox("tool_tip").setText(main.loadStyle("setup.dj") + "Tool Tips" + main.loadStyle("setup_end.dj"));
			gui.getCheckBox("on_top").setText(main.loadStyle("setup.dj") + "On Top" + main.loadStyle("setup_end.dj"));
			gui.getCheckBox("applyPanel").setText(main.loadStyle("setup.dj") + "Apply to Panel" + main.loadStyle("setup_end.dj"));
			gui.getCheckBox("applyText").setText(main.loadStyle("setup.dj") + "Apply to Text" + main.loadStyle("setup_end.dj"));
			gui.getSlider("size_slider").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Size" + main.loadStyle("setup_end.dj")));
			gui.getSlider("transparency_slider").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Opacity" + main.loadStyle("setup_end.dj")));
			gui.getSlider("color_slider_red").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Red Tint" + main.loadStyle("setup_end.dj")));
			gui.getSlider("color_slider_green").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Green Tint" + main.loadStyle("setup_end.dj")));
			gui.getSlider("color_slider_blue").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Blue Tint" + main.loadStyle("setup_end.dj")));
			TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "<div style='font-size: 15'>Enable/Disable Buttons</div>" + main.loadStyle("setup_end.dj"));
			b.setTitleJustification(TitledBorder.CENTER);
			gui.getPanel("allowedButtons").setBorder(b);
			main.getPanel(Resource.PANEL_SETTINGS).getGui().repaint();
		} catch (Exception e){}
	}
}