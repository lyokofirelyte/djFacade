package com.github.lyokofirelyte.djFacade.Listeners;

import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;

import org.json.simple.JSONObject;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.github.axet.vget.VGet;
import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.JSONMap;
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
		map.put("remove", "remove");
		map.put("video", "video");
		map.put("favorite", "favorite");
		map.put("veto", "discard");
		map.put("list", "add_to_queue");
		map.put("download", "download");
		map.put("chat", "chat");
		map.put("unlock", "not_secure");
		map.put("search", "search");
		if (!main.bools.containsKey("unlocked")){
			main.bools.put("unlocked", false);
		}
	}
	
	public void addToQueue(String link, boolean remove, boolean veto){
		
		JSONObject sendMap = new JSONObject();
		JSONObject dataMap = new JSONObject();
		sendMap.put("type", remove ? (veto ? "veto" : "remove") : "add");
		dataMap.put("username", main.files.get(Resource.SETTINGS).getStr("username"));
		dataMap.put("password", main.files.get(Resource.SETTINGS).getStr("password"));
		dataMap.put("link", link);
		sendMap.put("data", dataMap);
		main.sendPost("/api/dj", sendMap);
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
		
		if (type.contains("_inQueue")){
			main.openWebpage(main.files.get(Resource.SETTINGS).getStr(type + "_link"));
		}
		
		if (type.contains("_textLabel")){
			addToQueue("https://youtube.com/watch?v=" + main.getPanel(Resource.PANEL_SEARCH).getGui().getLabel(type).getToolTipText(), false, false);
			main.updateHeader("Added!");
			System.out.println("https://youtube.com/watch?v=" + main.getPanel(Resource.PANEL_SEARCH).getGui().getLabel(type).getToolTipText());
		}

		switch (type){
		
		
			case "chat":
				
				if (!main.bools.containsKey("chat")){
					main.bools.put("chat", false);
				}
				
				if (main.bools.get("chat")){
					main.onlyShow(main.getPanel(Resource.MAIN));
				} else {
					main.getPanel(Resource.PANEL_CHAT).display();
					main.getPanel(Resource.PANEL_CHAT_BAR).display();
					main.onlyShow(main.getPanel(Resource.MAIN), main.getPanel(Resource.PANEL_CHAT), main.getPanel(Resource.PANEL_CHAT_BAR));
				}
				
				main.bools.put("chat", !main.bools.get("chat"));
				
			break;
		
			case "video":
				
				if (!main.bools.containsKey("video")){
					main.bools.put("video", false);
				}
				
				if (main.bools.get("video")){
					main.onlyShow(main.getPanel(Resource.MAIN));
				} else {
					main.getPanel(Resource.PANEL_VIDEO).display();
					main.onlyShow(main.getPanel(Resource.MAIN), main.getPanel(Resource.PANEL_VIDEO));
				}
				
				main.bools.put("video", !main.bools.get("video"));
				
			break;
		
			case "list":

				if (!main.bools.containsKey("list")){
					main.bools.put("list", false);
				}
				
				if (main.bools.get("list")){
					main.onlyShow(main.getPanel(Resource.MAIN));
				} else {
					main.getPanel(Resource.PANEL_QUEUE).display();
					main.onlyShow(main.getPanel(Resource.MAIN), main.getPanel(Resource.PANEL_QUEUE));
				}
				
				main.bools.put("list", !main.bools.get("list"));
				
			break;
		
			case "add":
				
			try {
				addToQueue((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor), false, false);
				main.updateHeader("Added!");
			} catch (Exception ee){
				main.updateHeader("Error parsing link!");
				ee.printStackTrace();
			}
				
			break;
			
			case "remove":
				
				addToQueue("remove", true, false);
				main.updateHeader("Removed!");
				
			break;
			
			case "veto":
				
				addToQueue("veto", true, true);
				main.updateHeader("Vetoed!");
				
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
			
			case "search":
				
				if (!main.bools.containsKey("search")){
					main.getPanel(Resource.PANEL_SEARCH).display();
					main.getPanel(Resource.PANEL_SEARCH_BAR).display();
					main.onlyShow(main.getPanel(Resource.PANEL_SEARCH), main.getPanel(Resource.PANEL_SEARCH_BAR), main.getPanel(Resource.MAIN));
					main.bools.put("search", true);
				} else {
					main.bools.remove("search");
					main.onlyShow(main.getPanel(Resource.MAIN));
				}
			
			break;
			
			case "download":
				
				JSONObject video = main.getQueue().get(0);
				String title = (String) video.get("title");
				String link = (String) video.get("link");
				
		        try {
		            main.getYouTubeDownloader().start(link, new File("data/downloads"));
		        } catch (Exception ee) {
		        	ee.printStackTrace();
		        }
				
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		GUI gui = type.contains("_settings") ? main.getPanel(Resource.PANEL_SETTINGS).getGui() : type.contains("_textLabel") ? main.getPanel(Resource.PANEL_SEARCH).getGui() : main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeIn", main.files.get(Resource.SETTINGS).getFloat("transparency"));
		
		if ((main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) || type.contains("_settings")) && !main.bools.get("unlocked")){
				
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()-5);
				if (!type.contains("_settings")){
					gui.getLabel(type).setIcon(main.getImage("icons/dark/ic_action_" + map.get(new String(type.replace("_settings", ""))) + ".png"));
					if (main.files.get(Resource.SETTINGS).getBool("tool_tips") && !type.contains("_textLabel")){
						gui.getPanel("info").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + type + main.loadStyle("setup_end.dj")));
					}
					e.getComponent().setBackground(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				}
				gui.repaint();
		}
		
		if (type.contains("_textLabel")){
			gui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			gui.getLabel(type).setIcon(main.getImage("icons/dark/ic_action_new.png"));
			e.getComponent().setBackground(new Color(1.0f, 1.0f, 1.0f, 1.0f));
			gui.repaint();
		}
		
		if (type.contains("_inQueue")){
			JSONMap settings = main.files.get(Resource.SETTINGS);
			String user = settings.getStr(type.substring(0, 1) + "_inQueue_user");
			String title = settings.getStr(type.substring(0, 1) + "_inQueue_title");
			main.getPanel(Resource.MAIN).getGui().getPanel("info").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + title + " (" + user + ")" + main.loadStyle("setup_end.dj")));
			main.getPanel(Resource.MAIN).getGui().repaint();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
		JSONMap settings = main.files.get(Resource.SETTINGS);
		GUI gui = type.contains("_settings") ? main.getPanel(Resource.PANEL_SETTINGS).getGui() : type.contains("_textLabel") ? main.getPanel(Resource.PANEL_SEARCH).getGui() : main.getPanel(Resource.MAIN).getGui();
		main.counters.put("fadeOut", 1F);
		
		if (type.contains("_textLabel")){
			gui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			gui.getLabel(type).setIcon(main.getImage("icons/ic_action_new.png"));
			gui.getLabel(type).setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), settings.getFloat("transparency")));
			gui.repaint();
		}
		
		if ((main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(type) || type.contains("settings")) && !main.bools.get("unlocked")){
			
				gui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gui.getLabel(type).setLocation(gui.getLabel(type).getX(), gui.getLabel(type).getY()+5);
				if (!type.contains("_settings")){
					gui.getLabel(type).setIcon(main.getImage("icons/ic_action_" + map.get(new String(type.replace("_settings", ""))) + ".png"));
					gui.getPanel(type + "_panel").setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), settings.getFloat("transparency")));
				}
				gui.repaint();
		}
		
		main.getPanel(Resource.MAIN).getGui().getPanel("info").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + main.files.get(Resource.SETTINGS).getStr("nowPlaying") + main.loadStyle("setup_end.dj")));
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