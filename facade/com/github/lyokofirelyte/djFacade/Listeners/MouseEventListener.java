package com.github.lyokofirelyte.djFacade.Listeners;

import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
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
		
		GUI gui = main.getPanel(Resource.MAIN).getGui();

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
		
		GUI gui = main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeIn", 0.05F);
		
		if (main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) && !main.bools.get("unlocked")){
				
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()-5);
				gui.getLabel(type).setIcon(main.getImage("icons/dark/ic_action_" + map.get(type) + ".png"));
				//alphaFadeIn(e.getComponent());
				e.getComponent().setBackground(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				gui.repaint();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
		GUI gui = main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeOut", 1F);
		
		if (main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) && !main.bools.get("unlocked")){
			
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()+5);
				gui.getLabel(type).setIcon(main.getImage("icons/ic_action_" + map.get(type) + ".png"));
				//alphaFadeOut(e.getComponent());
				e.getComponent().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.05f));
				gui.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		GUI gui = main.getPanel(Resource.MAIN).getGui();
		gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()+2);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		GUI gui = main.getPanel(Resource.MAIN).getGui();
		gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()-2);
	}
	
	public void alphaFadeIn(final Component c){
		
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				c.setBackground(new Color(1.0f, 1.0f, 1.0f, main.counters.get("fadeIn")));
				main.counters.put("fadeIn", main.counters.get("fadeIn") + 0.1f);
				if (main.counters.get("fadeIn") > 1.0f){
					scheduler.shutdown();
				}
		}}, 0L, 10L, TimeUnit.MILLISECONDS);
	}
	
	public void alphaFadeOut(final Component c){
		
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				c.setBackground(new Color(1.0f, 1.0f, 1.0f, main.counters.get("fadeOut")));
				main.getPanel(Resource.MAIN).getGui().repaint();
				main.counters.put("fadeOut", main.counters.get("fadeOut") - 0.1f);
				if (main.counters.get("fadeOut") < 0.05f){
					scheduler.shutdown();
				}
		}}, 0L, 10L, TimeUnit.MILLISECONDS);
	}
}