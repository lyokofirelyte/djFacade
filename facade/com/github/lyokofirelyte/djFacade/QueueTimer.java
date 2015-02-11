package com.github.lyokofirelyte.djFacade;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;

import org.json.simple.JSONObject;

import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Panels.PanelChat;

public class QueueTimer implements AR {

	public DJFacade main;
	private ScheduledExecutorService oneSecondTimer;
	private ScheduledExecutorService fiveSecondTimer;
	private String songName = "";
	private float seconds = 0;
	private String style = "";
	private String styleEnd = "";
	
	public QueueTimer(DJFacade i){
		main = i;
		style = main.loadStyle("setup.dj");
		styleEnd = main.loadStyle("setup_end.dj");
	}
	
	public void start(){
		
		oneSecondTimer = Executors.newScheduledThreadPool(1);
		oneSecondTimer.scheduleAtFixedRate(new Runnable(){
			public void run(){
				
				try {
				
					if (!songName.equals("")){
						seconds--;
						update();
					}
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}, 1000L, 1000L, TimeUnit.MILLISECONDS);
		
		fiveSecondTimer = Executors.newScheduledThreadPool(1);
		fiveSecondTimer.scheduleAtFixedRate(new Runnable(){
			public void run(){
				
				try {
					
					if (main.bools.containsKey("chat") && main.bools.get("chat")){
						PanelChat chat = (PanelChat) main.getPanel(Resource.PANEL_CHAT);
						chat.updateChat();
					}
					
					List<JSONObject> queue = main.getQueue();
					
					if (queue.size() > 0){
						String newName = (String) queue.get(0).get("title");
						if (!songName.equals(newName)){
							songName = (String) queue.get(0).get("title");
							seconds = (Long) queue.get(0).get("duration");
							if (main.files.get(Resource.SETTINGS).getBool("list")){
								main.getPanel(Resource.PANEL_QUEUE).destroy();
								main.getPanel(Resource.PANEL_QUEUE).display();
							}
							update();
						}
					} else {
						seconds = 0;
						songName = "";
						if (main.files.get(Resource.SETTINGS).getBool("list")){
							main.getPanel(Resource.PANEL_QUEUE).destroy();
							main.getPanel(Resource.PANEL_QUEUE).display();
						}
						update();
					}
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}, 1000L, 5000L, TimeUnit.MILLISECONDS);
		
		System.out.println("Timers have begun!");
	}
	
	public void stop(){
		
		oneSecondTimer.shutdown();
		fiveSecondTimer.shutdown();
	}
	
	public void setSeconds(float secs){
		seconds = secs;
	}
	
	public float getSeconds(){
		return seconds;
	}
	
	public void update(){
		if (main.bools.containsKey("list") && main.bools.get("list")){
			GUI gui = main.getPanel(Resource.PANEL_QUEUE).getGui();
			gui.getPanel("0_inQueue").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), style + (seconds > 0 ? main.getTimeFromSeconds(Integer.parseInt((seconds + "").replace(".0", ""))): "00:00") + styleEnd));
			gui.repaint();
		}
	}
}