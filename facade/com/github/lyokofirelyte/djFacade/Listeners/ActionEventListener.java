package com.github.lyokofirelyte.djFacade.Listeners;

import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.HintPasswordField;
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Panels.Panel;

public class ActionEventListener implements AR, ActionListener {
	
	public DJFacade main;
	
	public ActionEventListener(DJFacade i){
		main = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		main.getPanel(Resource.PANEL_SETTINGS).getGui().repaint();

		switch (ActionCommand.valueOf(e.getActionCommand().toString())){

			case TOOL_TIPS:
				
				main.files.get(Resource.SETTINGS).set("tool_tips", !main.files.get(Resource.SETTINGS).getBool("tool_tips"));
				
			break;
			
			case ON_TOP:
				
				main.files.get(Resource.SETTINGS).set("on_top", !main.files.get(Resource.SETTINGS).getBool("on_top"));
				main.getPanel(Resource.MAIN).getGui().setAlwaysOnTop(main.files.get(Resource.SETTINGS).getBool("on_top"));
				
			break;
		
			case SETUP_TEXT:
				
				final JSONMap map = main.files.get(Resource.SETTINGS);
				final Panel panel = main.getPanel(Resource.PANEL_LOGIN);
				String text = panel.getGui().getLabel("whoAreYou").getText();
				
				if (text.contains("Who are you?")){
					System.out.println("Username recorded - transition moving.");
					panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("Who are you?", "Saving..."));
					ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
					scheduler.schedule(new Runnable(){
						public void run(){http://marketplace.eclipse.org/content/eclipse-color-theme
							fadeOut(panel.getGui());
							map.put("username", panel.getGui().getHintTextField("username").getText());
							panel.getGui().getPanel("login_main").remove(panel.getGui().getHintTextField("username"));
							panel.getGui().addAttr(new HintPasswordField("password"), "password");
							panel.getGui().getHintPasswordField("password").setPreferredSize(new Dimension(panel.getGui().getHintTextField("username").getWidth(), panel.getGui().getHintTextField("username").getHeight()));
							panel.getGui().getHintPasswordField("password").setActionCommand(ActionCommand.SETUP_TEXT.toString());
							panel.getGui().getHintPasswordField("password").addActionListener(main.getEventListener());
							panel.getGui().getPanel("login_main").add(panel.getGui().getHintPasswordField("password"));
							panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("Saving...", "What is your ohsototes.com password?"));
							fadeIn(panel.getGui());
						}
					}, 100L, TimeUnit.MILLISECONDS);
				}
				
				else if (text.contains("What is your ohsototes.com password?")){
					
					System.out.println("Password recorded - transition moving.");
					panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("What is your ohsototes.com password?", "Saving..."));
					ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
					scheduler.schedule(new Runnable(){
						public void run(){
							fadeOut(panel.getGui());
							map.put("password", panel.getGui().getHintPasswordField("password").getText());
							panel.getGui().setTitle("djFacade - " + map.getStr("username"));
							panel.getGui().getPanel("login_main").remove(panel.getGui().getHintPasswordField("password"));
							panel.getGui().addAttr(new HintTextField("type 'confirm' or 'restart'."), "confirm");
							panel.getGui().getHintTextField("confirm").setPreferredSize(new Dimension(panel.getGui().getHintTextField("username").getWidth(), panel.getGui().getHintTextField("username").getHeight()));
							panel.getGui().getHintTextField("confirm").setActionCommand(ActionCommand.SETUP_TEXT.toString());
							panel.getGui().getHintTextField("confirm").addActionListener(main.getEventListener());
							panel.getGui().getPanel("login_main").add(panel.getGui().getHintTextField("confirm"));
							panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("Saving...", "Type 'confirm' to log in, or 'restart'."));
							fadeIn(panel.getGui());
						}
					}, 100L, TimeUnit.MILLISECONDS);
				}
				
				else if (text.contains("Type 'confirm' to log in, or 'restart'.")){
					
					if (panel.getGui().getHintTextField("confirm").getText().equals("confirm")){
					
						System.out.println("Authing w/ ohsototes.com...");
						Map<String, Object> sendMap = new THashMap<>();
						sendMap.put("username", map.getStr("username"));
						sendMap.put("password", map.getStr("password"));
						
						JSONObject obj = main.sendPost("/api/login", sendMap);

						if (obj.get("success").equals(true)){
							System.out.println("Username & password are correct.");
							map.put("loggedIn", true);
							main.getPanel(Resource.MAIN).display();
							panel.destroy();
							try {
								main.saveAll();
							} catch (Exception ee){
								ee.printStackTrace();
							}
						} else {
							panel.getGui().getHintTextField("confirm").setText("INVALID CREDENTIALS!");
						}
						
					} else if (panel.getGui().getHintTextField("confirm").getText().equals("restart")){
						panel.destroy();
						main.getPanel(Resource.PANEL_LOGIN).display();
					}
				}
				
			break;
			
			default: 
				
				System.out.println("No action command found! :3");
				
			break;
		}
	}
	
	public void fadeOut(GUI gui){
		
		try {
			for (float i = 1; i >= 0; i-= 0.01){
				gui.setOpacity(i);
				Thread.sleep(10);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void fadeIn(GUI gui){
		
		try {
			for (float i = 0; i <= 1; i+= 0.01){
				gui.setOpacity(i);
				Thread.sleep(10);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}