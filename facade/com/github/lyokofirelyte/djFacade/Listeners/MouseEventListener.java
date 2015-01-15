package com.github.lyokofirelyte.djFacade.Listeners;

import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Panels.Panel;

public class MouseEventListener implements MouseListener {
	
	public String type;
	public DJFacade main;
	public Map<String, String> map = new THashMap<String, String>();
	
	public MouseEventListener(DJFacade i, String name){
		main = i;
		type = name;
		map.put("add", "new");
		map.put("settings", "overflow");
		map.put("refresh", "refresh");
		map.put("remove", "remove");
		map.put("video", "video");
		map.put("favorite", "favorite");
		map.put("veto", "discard");
		map.put("list", "add_to_queue");
		map.put("download", "download");
		map.put("chat", "chat");
		map.put("unlock", "not_secure");
		if (!main.bools.containsKey("unlocked")){
			main.bools.put("unlocked", false);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (main.bools.get("unlocked") && !type.contains("unlock")){
			return;
		}
		
		GUI gui = main.getPanel(Resource.MAIN).getGui();
		
		if (type.contains("_settings")){
			GUI settingsGUI = main.getPanel(Resource.PANEL_SETTINGS).getGui();
			List<String> allowed = main.files.get(Resource.SETTINGS).getList("allowedButtons");
			if (allowed.contains(type.replace("_settings", ""))){
				allowed.remove(type.replace("_settings", ""));
				settingsGUI.getLabel(type).setIcon(main.getImage("icons/ic_action_" + map.get(type.replace("_settings", "")) + ".png", 40, 40));
			} else {
				allowed.add(type.replace("_settings", ""));
				settingsGUI.getLabel(type).setIcon(main.getImage("icons/dark/ic_action_" + map.get(type.replace("_settings", "")) + ".png", 40, 40));
			}
			gui.dispose();
			main.getPanel(Resource.MAIN).display();
			main.getPanel(Resource.MAIN).getGui().setLocation(gui.getX(), gui.getY());
		}

		switch (type){
		
			case "add":
				
				
				
			break;
			
			case "remove":
				
				System.out.println("Remove!");
				
			break;
			
			case "unlock":

				if (!main.bools.containsKey("unlocked")){
					main.bools.put("unlocked", false);
				}
				
				Panel currGui = main.getPanel(main.bools.get("unlocked") ? Resource.MAIN : Resource.MAIN_UNLOCKED);
				Panel oldGui = main.getPanel(!main.bools.get("unlocked") ? Resource.MAIN : Resource.MAIN_UNLOCKED);
				
				currGui.display();
				currGui.getGui().setLocation(oldGui.getGui().getLocation());
				oldGui.destroy();
				
				main.onlyShow(currGui);
				main.bools.put("unlocked", !main.bools.get("unlocked"));
				
			break;
			
			case "settings":
				
				if (!main.bools.containsKey("settings")){
					main.getPanel(Resource.PANEL_SETTINGS).display();
					main.onlyShow(main.getPanel(Resource.PANEL_SETTINGS), main.getPanel(Resource.MAIN));
					main.bools.put("settings", true);
				} else {
					main.bools.remove("settings");
					main.onlyShow(main.getPanel(Resource.MAIN));
				}

				
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		GUI gui = type.contains("_settings") ? main.getPanel(Resource.PANEL_SETTINGS).getGui() : main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeIn", main.files.get(Resource.SETTINGS).getFloat("transparency"));
		
		if ((main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) || type.contains("_settings")) && !main.bools.get("unlocked")){
				
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()-5);
				if (!type.contains("_settings")){
					if (main.files.get(Resource.SETTINGS).getBool("tool_tips")){
						gui.getPanel("info").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + type + main.loadStyle("setup_end.dj")));
					}
					gui.getLabel(type).setIcon(main.getImage("icons/dark/ic_action_" + map.get(type) + ".png"));
					e.getComponent().setBackground(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				}
				gui.repaint();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
		GUI gui = type.contains("_settings") ? main.getPanel(Resource.PANEL_SETTINGS).getGui() : main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeOut", 1F);
		
		if ((main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) || type.contains("settings")) && !main.bools.get("unlocked")){
			
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()+5);
				if (!type.contains("_settings")){
					gui.getLabel(type).setIcon(main.getImage("icons/ic_action_" + map.get(type) + ".png"));
					gui.getPanel("info").setBorder(BorderFactory.createEmptyBorder());
					e.getComponent().setBackground(main.getEventListener().getColor(Color.decode(main.files.get(Resource.SETTINGS).getStr("color_tint"))));
				}
				gui.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if (main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type)){
			GUI gui = main.getPanel(Resource.MAIN).getGui();
			gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()+2);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type)){
			GUI gui = main.getPanel(Resource.MAIN).getGui();
			gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()-2);
		}
	}
}