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
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.JSONMap;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.ScrollBarListener;

public class PanelSearchBar implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelSearchBar(DJFacade i){
		main = i;
	}
	
	public void display(){
		  
		try {
			
			gui = new GUI("searchBar");
			gui.setLayout(new FlowLayout(0, 0, 0));
			gui.setResizable(false);
			gui.setTitle("djFacade");
			gui.setUndecorated(true);
			gui.setBackground(new Color(0, 0, 0, 0.0f));
			
			GUI mainGui = main.getPanel(Resource.MAIN).getGui();
			JSONMap map = main.files.get(Resource.SETTINGS);
			
			gui.addAttr(new JPanel(), "main");
			gui.addAttr(new HintTextField("search..."), "search");
			
			gui.getPanel("main").setLayout(new FlowLayout());
			gui.panel().setBackground(new Color(0, 0, 0, 0.5f));
			gui.getHintTextField("search").setPreferredSize(new Dimension(mainGui.getWidth()-10, 25));
			gui.hintTextField().setActionCommand(ActionCommand.SEARCH_BAR.toString());
			gui.hintTextField().addActionListener(main.getEventListener());
			
			gui.getPanel("main").add(gui.getHintTextField("search"));
			gui.add(gui.getPanel("main"));
			
			gui.setLocation(mainGui.getX(), (mainGui.getY() + mainGui.getHeight() - 38));
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