package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.ScrollBarListener;

public class PanelQueue implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelQueue(DJFacade i){
		main = i;
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> getQueue(){
		JSONMap sendMap = new JSONMap();
		JSONObject loginMap = new JSONObject();
		sendMap.put("type", "refresh");
		loginMap.put("username", main.files.get(Resource.SETTINGS).getStr("username"));
		loginMap.put("password", main.files.get(Resource.SETTINGS).getStr("password"));
		sendMap.put("data", loginMap);
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			list = ((List<JSONObject>) ((JSONObject) ((JSONObject) main.sendPost("/api/dj", sendMap)).get("data")).get("queue"));
			main.files.get(Resource.SETTINGS).set("nowPlaying", list.get(0).get("title"));
		} catch (Exception e){}
		return list;
	}
	
	public void display(){
		  
		try {
			
			gui = new GUI("queue");
			gui.setLayout(new FlowLayout(0, 0, 0));
			gui.setResizable(false);
			gui.setTitle("djFacade");
			gui.setUndecorated(true);
			gui.setBackground(new Color(0, 0, 0, 0.0f));
			
			GUI mainGui = main.getPanel(Resource.MAIN).getGui();
			JSONMap map = main.files.get(Resource.SETTINGS);
			
			gui.addAttr(new JPanel(), "main");
			gui.addAttr(new JScrollPane(gui.getPanel("main")), "main_scroll");
			gui.getScroll("main_scroll").setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			gui.scroll().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			gui.scroll().setOpaque(false);
			gui.scroll().setBackground(new Color(0.0f, 0, 0, 0f));
			gui.scroll().setPreferredSize(new Dimension(700, 140));
			gui.scroll().getHorizontalScrollBar().addAdjustmentListener(new ScrollBarListener(main, "queue_bar"));
			gui.scroll().getHorizontalScrollBar().setOpaque(false);
			gui.scroll().getHorizontalScrollBar().setUI(new BasicScrollBarUI());
			gui.scroll().setBorder(BorderFactory.createEmptyBorder());
			
			int x = 0;
			List<JSONObject> queue = new ArrayList<JSONObject>(getQueue());
			
			for (JSONObject obj : queue){
				gui.addAttr(new JPanel(), x + "_inQueue");
				gui.getPanel(x + "_inQueue").setOpaque(false);
				gui.panel().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				gui.getPanel(x + "_inQueue").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + main.getTimeFromSeconds(Integer.parseInt(((Long) obj.get("duration")).toString())) + main.loadStyle("setup_end.dj")));
				gui.panel().setOpaque(false);
				gui.panel().addMouseListener(new MouseEventListener(main, x + "_inQueue"));
				gui.addAttr(new JLabel(main.getImageFromUrl((String) obj.get("thumb"), x == 0 ? 142 : 116, x == 0 ? 90 : 72)), x + "_label");
				gui.getLabel(x + "_label").setOpaque(false);
				gui.getPanel(x + "_inQueue").add(gui.getLabel(x + "_label"));
				map.put(x + "_inQueue_user", ((String) obj.get("user")));
				map.put(x + "_inQueue_title", ((String) obj.get("title")));
				map.put(x + "_inQueue_link", ((String) obj.get("link")));
				map.put(x + "_inQueue_duration", obj.get("duration"));
				gui.getPanel("main").add(gui.getPanel(x + "_inQueue"));
				x++;
			}
			
			if (queue.size() < 7){
				for (int i = queue.size(); i < 7; i++){
					gui.addAttr(new JPanel(), i + "_inQueue");
					gui.getPanel(i + "_inQueue").setOpaque(false);
					gui.panel().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
					gui.addAttr(new JLabel(main.getImage("icons/ic_action_about.png", 70, 70)), i + "_label");
					gui.getLabel(i + "_label").setOpaque(false);
					gui.getPanel(i + "_inQueue").add(gui.getLabel(i + "_label"));
					gui.getPanel(i + "_inQueue").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "00:00" + main.loadStyle("setup_end.dj")));
					gui.panel().setOpaque(false);
					gui.getPanel("main").add(gui.getPanel(i + "_inQueue"));
				}
			}

			gui.getPanel("main").setLayout(new FlowLayout());
			gui.panel().setBackground(new Color(0, 0, 0, 0.5f));
			
			gui.add(gui.getScroll("main_scroll"));
			gui.setLocation(mainGui.getX(), (mainGui.getY() - mainGui.getHeight()) - 35);
			gui.setAlwaysOnTop(main.files.get(Resource.SETTINGS).getBool("on_top"));
			gui.pack();
			gui.setVisible(true);
			gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			gui.addWindowListener(main.getListeners().adapter);
			mainGui.repaint();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void hide(){
		gui.setVisible(false);
	}
	
	public void destroy(){
		gui.dispose();
	}
	
	public GUI getGui(){
		return gui;
	}
}