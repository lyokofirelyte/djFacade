package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;

public class PanelSettings implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelSettings(DJFacade i){
		main = i;
	}

	public void display(){
		gui = new GUI("settings");
		gui.setLocationRelativeTo(null);
		gui.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		gui.setResizable(false);
		gui.setTitle("djFacade");
		gui.setUndecorated(true);
		gui.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		
		GUI mainGui = main.getPanel(Resource.MAIN).getGui();
		
		gui.addAttr(new JPanel(), "main");
		gui.addAttr(new JPanel(), "allowedButtons");
		gui.addAttr(new JLabel(main.getImage("icons/ic_action_accept.png", 70, 70)), "accept");
		gui.getPanel("main").setPreferredSize(new Dimension(1000, 100));
		gui.setLocation(mainGui.getX(), (mainGui.getY() - mainGui.getHeight()) - gui.getHeight());
		
		for (String button : main.getMouseListener().map.keySet()){
			if (main.files.get(Resource.SETTINGS).getList("allowedButtons").contains(button)){
				gui.addAttr(new JLabel(main.getImage("icons/dark/ic_action_" + main.getMouseListener().map.get(button) + ".png", 40, 40)), button + "_settings");
			} else {
				gui.addAttr(new JLabel(main.getImage("icons/ic_action_" + main.getMouseListener().map.get(button) + ".png", 40, 40)), button + "_settings");
			}
			gui.getLabel(button + "_settings").addMouseListener(new MouseEventListener(main, button + "_settings"));
			gui.addAttr(new JPanel(), main.getMouseListener().map.get(button));
			gui.getPanel(main.getMouseListener().map.get(button)).add(gui.getLabel(button + "_settings"));
			gui.getPanel("allowedButtons").add(gui.getPanel(main.getMouseListener().map.get(button)));
		}

		gui.getPanel("allowedButtons").setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		gui.panel().setBorder(BorderFactory.createTitledBorder(main.loadStyle("setup.dj") + "Displayed Buttons" + main.loadStyle("setup_end.dj")));
		gui.getPanel("main").add(gui.getPanel("allowedButtons"));
		gui.getPanel("main").setBackground(new Color(1.0f, 1.0f, 1.0f, 0.05f));
		gui.getPanel("main").add(gui.getLabel("accept"));
		gui.add(gui.getPanel("main"));
		
		gui.setAlwaysOnTop(true);
		gui.pack();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		gui.addWindowListener(main.getListeners().adapter);
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