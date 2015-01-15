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

public class PanelMain implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelMain(DJFacade i){
		main = i;
	}

	public void display(){
		gui = new GUI("main");
		gui.setLocationRelativeTo(null);
		gui.setLayout(new GridLayout(0, 1));
		gui.setResizable(false);
		gui.setTitle("djFacade");
		gui.setUndecorated(true);
		gui.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		gui.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("bg.png")));
		
		gui.addAttr(new JPanel(), "main");
		gui.addAttr(new JPanel(), "info");
		
		for (String name : main.files.get(Resource.SETTINGS).getList("allowedButtons")){
			System.out.println("icons/ic_action_" + main.getMouseListener().map.get(name) + ".png");
			gui.addAttr(new JLabel(main.getImage("icons/ic_action_" + main.getMouseListener().map.get(name) + ".png")), name);
			gui.addAttr(new JPanel(), name + "_panel");
			gui.getPanel(name + "_panel").setBackground(main.getEventListener().getColor(Color.decode(main.files.get(Resource.SETTINGS).getStr("color_tint"))));
			gui.panel().addMouseListener(new MouseEventListener(main, name));
			gui.getPanel(name + "_panel").add(gui.getLabel(name));
			gui.getPanel("main").add(gui.getPanel(name + "_panel"));
		}

		gui.getPanel("main").setOpaque(false);
		gui.getPanel("info").setOpaque(false);
		gui.panel().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "djFacade" + main.loadStyle("setup_end.dj")));

		gui.getPanel("main").setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
		gui.add(gui.getPanel("main"));
		gui.add(gui.getPanel("info"));
		
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