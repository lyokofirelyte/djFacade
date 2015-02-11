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

public class PanelSearch implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelSearch(DJFacade i){
		main = i;
	}
	
	public void display(){
		  
		try {
			
			JSONMap settings = main.files.get(Resource.SETTINGS);
			
			gui = new GUI("search");
			gui.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			gui.setResizable(false);
			gui.setTitle("djFacade");
			gui.setUndecorated(true);
			gui.setBackground(new Color(settings.getFloat("color_slider_red"), settings.getFloat("color_slider_green"), settings.getFloat("color_slider_blue"), settings.getFloat("transparency")));

			
			GUI mainGui = main.getPanel(Resource.MAIN).getGui();
			JSONMap map = main.files.get(Resource.SETTINGS);
			
			gui.addAttr(new JPanel(), "main");
			gui.addAttr(new JScrollPane(gui.getPanel("main")), "main_scroll");
			gui.getScroll("main_scroll").setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			gui.scroll().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			gui.scroll().setOpaque(false);
			gui.scroll().setBackground(new Color(0.0f, 0, 0, 0f));
			gui.scroll().setPreferredSize(new Dimension(700, 250));
			gui.scroll().getVerticalScrollBar().addAdjustmentListener(new ScrollBarListener(main, "search_bar"));
			gui.scroll().getVerticalScrollBar().setOpaque(false);
			gui.scroll().getVerticalScrollBar().setUI(new BasicScrollBarUI());
			gui.scroll().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "> 0/0 results" + main.loadStyle("setup_end.dj")));
			
			gui.getPanel("main").setBackground(new Color(0, 0, 0, 0.0f));
			
			gui.add(gui.getScroll("main_scroll"));
			gui.setLocation(mainGui.getX()+5, (mainGui.getY() - mainGui.getHeight()) - 150);
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