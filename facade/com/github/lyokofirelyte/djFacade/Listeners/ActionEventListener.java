package com.github.lyokofirelyte.djFacade.Listeners;

import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.json.simple.JSONObject;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.HintPasswordField;
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.QueueTimer;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Panels.Panel;
import com.github.lyokofirelyte.djFacade.Panels.PanelChat;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.base.Joiner;

public class ActionEventListener implements AR, ActionListener {
	
	public DJFacade main;
	private String lastSearch = "";
	
	public ActionEventListener(DJFacade i){
		main = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			main.getPanel(Resource.PANEL_SETTINGS).getGui().repaint();
		} catch (Exception eee){}

		switch (ActionCommand.valueOf(e.getActionCommand().toString())){
		
			case CHAT_BAR:
				
				main.sendChat(main.getPanel(Resource.PANEL_CHAT_BAR).getGui().getHintTextField("chat").getText());
				main.getPanel(Resource.PANEL_CHAT_BAR).getGui().getHintTextField("chat").setText("");
				((PanelChat) main.getPanel(Resource.PANEL_CHAT)).updateChat();
				
			break;
		
			case TOOL_TIPS:
				
				main.files.get(Resource.SETTINGS).set("tool_tips", !main.files.get(Resource.SETTINGS).getBool("tool_tips"));
				
			break;
			
			case ON_TOP:
				
				main.files.get(Resource.SETTINGS).set("on_top", !main.files.get(Resource.SETTINGS).getBool("on_top"));
				main.getPanel(Resource.MAIN).getGui().setAlwaysOnTop(main.files.get(Resource.SETTINGS).getBool("on_top"));
				
			break;
			
			case PANEL:
				
				main.files.get(Resource.SETTINGS).set("applyPanel", !main.files.get(Resource.SETTINGS).getBool("applyPanel"));
				
			break;
			
			case TEXT:
				
				main.files.get(Resource.SETTINGS).set("applyText", !main.files.get(Resource.SETTINGS).getBool("applyText"));
				
			break;
			
			case SEARCH_BAR:
				
				System.out.println("Loading search...");
				GUI gui = main.getPanel(Resource.PANEL_SEARCH).getGui();
				String query = main.getPanel(Resource.PANEL_SEARCH_BAR).getGui().getHintTextField("search").getText();
				gui.getPanel("main").removeAll();
				gui.getPanel("main").setLayout(new GridLayout(0, 2));
				String front = main.loadStyle("search.dj");
				String back = main.loadStyle("setup_end.dj");
				
				if (!lastSearch.equals(query)){
					lastSearch = query;
					List<SearchResult> results = main.getYouTubeHandler().getVideos(query);
			        List<String> videoIds = new ArrayList<String>();
			        List<Video> videoList = new ArrayList<Video>();

			        for (SearchResult searchResult : results){
			        	videoIds.add(searchResult.getId().getVideoId());
			        }
			        
			        try {
			        
				        Joiner stringJoiner = Joiner.on(',');
				        String videoId = stringJoiner.join(videoIds);
				        YouTube.Videos.List listVideosRequest = main.getYouTubeHandler().getYouTube().videos().list("snippet,recordingDetails,contentDetails").setId(videoId);
				        VideoListResponse listResponse = listVideosRequest.execute();
				        videoList = listResponse.getItems();
				        
			        } catch (Exception ee){
			        	ee.printStackTrace();
			        }
					
					gui.getScroll("main_scroll").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), front + "> " + results.size() + "/20 results" + back));
					int x = 0;
					
					for (SearchResult result : results){
						gui.addAttr(new JPanel(), x + "_panel");
						
						PeriodFormatter formatter = ISOPeriodFormat.standard();
						Period p = formatter.parsePeriod(videoList.get(x).getContentDetails().getDuration());
						Seconds s = p.toStandardSeconds();
						String duration = main.getTimeFromSeconds(s.getSeconds());
						String title = result.getSnippet().getTitle();
						
						if (title.length() > 20){
							title = title.substring(0, 20) + "...";
						}
						
						gui.getPanel(x + "_panel").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), front + title + " (" + duration + ")" + back));
						try {
							gui.addAttr(new JLabel(main.getImageFromUrl(result.getSnippet().getThumbnails().getMaxres().getUrl(), 160, 100)), x + "_thumb");
						} catch (Exception ee){
							gui.addAttr(new JLabel(main.getImageFromUrl(result.getSnippet().getThumbnails().getDefault().getUrl(), 160, 100)), x + "_thumb");
						}
						gui.addAttr(new JLabel(main.getImage("icons/ic_action_new.png", 50, 50)), x + "_textLabel");
						gui.getLabel(x + "_textLabel").addMouseListener(new MouseEventListener(main, x + "_textLabel"));
						gui.getLabel(x + "_textLabel").setToolTipText(result.getId().getVideoId());

						gui.getPanel(x + "_panel").add(gui.getLabel(x + "_thumb"));
						gui.getPanel(x + "_panel").setOpaque(false);
						gui.getPanel(x + "_panel").add(gui.getLabel(x + "_textLabel"));
						gui.getLabel(x + "_textLabel").setOpaque(false);
						gui.label().setPreferredSize(new Dimension(100, 100));
						gui.getPanel("main").add(gui.getPanel(x + "_panel"));
						x++;
					}
					System.out.println(results.size() + " videos found.");
				}

				gui.repaint();
				gui.setVisible(true);
				System.out.println("Search repainted.");
				
			break;
		
			case SETUP_TEXT:
				
				final JSONMap map = main.files.get(Resource.SETTINGS);
				final Panel panel = main.getPanel(Resource.PANEL_LOGIN);
				String text = panel.getGui().getLabel("whoAreYou").getText();
				
				if (text.contains("What's your worldscolli.de username")){
					System.out.println("Username recorded - transition moving.");
					panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("What's your worldscolli.de username?", "Saving..."));
					ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
					scheduler.schedule(new Runnable(){
						public void run(){
							fadeOut(panel.getGui());
							map.put("username", panel.getGui().getHintTextField("username").getText());
							panel.getGui().getPanel("login_main").remove(panel.getGui().getHintTextField("username"));
							panel.getGui().addAttr(new HintPasswordField("password"), "password");
							panel.getGui().getHintPasswordField("password").setPreferredSize(new Dimension(panel.getGui().getHintTextField("username").getWidth(), panel.getGui().getHintTextField("username").getHeight()));
							panel.getGui().getHintPasswordField("password").setActionCommand(ActionCommand.SETUP_TEXT.toString());
							panel.getGui().getHintPasswordField("password").addActionListener(main.getEventListener());
							panel.getGui().getPanel("login_main").add(panel.getGui().getHintPasswordField("password"));
							panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("Saving...", "What is your worldscolli.de password?"));
							fadeIn(panel.getGui());
						}
					}, 100L, TimeUnit.MILLISECONDS);
				}
				
				else if (text.contains("What is your worldscolli.de password?")){
					
					System.out.println("Password recorded - transition moving.");
					panel.getGui().getLabel("whoAreYou").setText(panel.getGui().getLabel("whoAreYou").getText().replace("What is your worldscolli.de password?", "Saving..."));
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
							main.timer = new QueueTimer(main);
							main.timer.start();
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