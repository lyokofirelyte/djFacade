package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.QueueTimer;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.sun.javafx.tk.Toolkit;

public class PanelVideo implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelVideo(DJFacade i){
		main = i;
	}
	
	public void display(){
		  
		try {
			
			gui = new GUI("queue");
			gui.setResizable(false);
			gui.setTitle("djFacade");	
			gui.setUndecorated(true);
			GUI mainGui = main.getPanel(Resource.MAIN).getGui();
			
			gui.getContentPane().add(getBrowserPanel(), BorderLayout.CENTER);
			gui.setPreferredSize(new Dimension(670, 410));
			gui.setLocation(mainGui.getX() + 5, (mainGui.getY() - mainGui.getHeight()) - 300);
			gui.setAlwaysOnTop(main.files.get(Resource.SETTINGS).getBool("on_top"));
			gui.pack();
			gui.setVisible(true);
			gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			gui.addWindowListener(main.getListeners().adapter);
		    
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public JPanel getBrowserPanel(){

	    JPanel webBrowserPanel = new JPanel(new BorderLayout());
	    webBrowserPanel.setOpaque(false);
	    webBrowserPanel.setPreferredSize(new Dimension(670, 410));
	    JWebBrowser webBrowser = new JWebBrowser();
	    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
	    webBrowser.setBarsVisible(false);
	    JSONObject obj = main.getQueue().get(0);
	    long duration = (long) obj.get("duration");
	    float secs = main.getTimer().getSeconds();
	    float total = duration - secs;
	    webBrowser.navigate(((String) main.getQueue().get(0).get("link")) + "&t=" + (total + "").replace(".0", "") + "s");
	    return webBrowserPanel;
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