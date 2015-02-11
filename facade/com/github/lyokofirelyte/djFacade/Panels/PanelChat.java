package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.ScrollBarListener;

public class PanelChat implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	private String lastMessage = "";
	
	public PanelChat(DJFacade i){
		main = i;
	}
	
	public void display(){
		  
		try {
			
			JSONMap settings = main.files.get(Resource.SETTINGS);
			
			gui = new GUI("chat");
			gui.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			gui.setResizable(false);
			gui.setTitle("djFacade");
			gui.setUndecorated(true);
			gui.setPreferredSize(new Dimension(720, 300));
			gui.setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), 1));

			GUI mainGui = main.getPanel(Resource.MAIN).getGui();
			JSONMap map = main.files.get(Resource.SETTINGS);
			
			gui.addAttr(new JPanel(), "main");
			gui.addAttr(new JEditorPane("text/html", main.loadStyle("chat.dj")), "textPane");
			gui.addAttr(new JScrollPane(gui.getPanel("main")), "main_scroll");
			
			gui.getScroll("main_scroll").setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			gui.scroll().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			gui.scroll().setOpaque(false);
			gui.scroll().setBackground(new Color(0.0f, 0, 0, 0f));
			gui.scroll().setPreferredSize(new Dimension(720, 300));
			gui.scroll().getHorizontalScrollBar().addAdjustmentListener(new ScrollBarListener(main, "chat_bar"));
			gui.scroll().getHorizontalScrollBar().setOpaque(false);
			gui.scroll().getHorizontalScrollBar().setUI(new BasicScrollBarUI());
			gui.scroll().setBorder(BorderFactory.createEmptyBorder());
			
			gui.getEditorPane("textPane").setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			gui.editorPane().setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), 1));

			gui.editorPane().setEditable(false);
			//gui.editorPane().setOpaque(false);
			
			gui.getPanel("main").setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), 1));

			gui.getPanel("main").add(gui.getEditorPane("textPane"));
			gui.getPanel("main").setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
			gui.add(gui.getScroll("main_scroll"));
			gui.setLocation(mainGui.getX()+5, (mainGui.getY() - mainGui.getHeight()) - 200);
			gui.setAlwaysOnTop(main.files.get(Resource.SETTINGS).getBool("on_top"));
			gui.pack();
			gui.setVisible(true);
			gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			gui.addWindowListener(main.getListeners().adapter);
			updateChat();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateChat(){
		
		String finalText = main.loadStyle("chat.dj");
		
		JSONObject chatObj = main.getChat();
		List<String> chats = (List<String>) chatObj.get("message");
		
		if (chats.size() > 0 && !lastMessage.equals(chats.get(chats.size()-1))){
			
			for (String chat : chats){
				String user = "test";
				try {
					user = chat.split("] ")[1].split(":")[0];
				} catch (Exception e){}
				finalText += "<br /><br /><img src=\"https://minotar.net/avatar/" + user + "/40.png\"/>&nbsp;&nbsp;&nbsp;" + chat;
			}
			
			finalText += "<br /><br /><br /><br />" + main.loadStyle("setup_end.dj");
			
			if (!gui.getEditorPane("textPane").getText().equals(finalText)){
				gui.getEditorPane("textPane").setText(finalText);
			}
			
			lastMessage = chats.get(chats.size()-1);
		}
		
		gui.getEditorPane("textPane").repaint();
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