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

public class PanelLogin implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelLogin(DJFacade i){
		main = i;
	}

	public void display(){
		gui = new GUI("login");
		gui.setLocationRelativeTo(null);
		gui.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		gui.setResizable(false);
		gui.setPreferredSize(new Dimension(400, 50));
		gui.setTitle("djFacade");
		gui.setSize(new Dimension(400, 50));
		gui.setUndecorated(true);
		gui.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("bg.png")));

		gui.addAttr(new JLabel(main.getImage("bg.jpg", 400, 50)), "login_bg");
		gui.addAttr(new JPanel(), "login_main");
		gui.addAttr(new JLabel("TELL ME WHO YOU ARE, SCRUB!"), "whoAreYou");
		gui.addAttr(new HintTextField("user identification"), "username");
		
		gui.getLabel("whoAreYou").setPreferredSize(new Dimension(gui.getWidth(), 15));
		gui.label().setText(main.loadStyle("setup.dj") + "What's your worldscolli.de username?" + main.styles.get("setup_end"));
		
		gui.getHintTextField("username").setPreferredSize(new Dimension(gui.getWidth(), 25));
		gui.hintTextField().addActionListener(main.getEventListener());
		gui.hintTextField().setActionCommand(ActionCommand.SETUP_TEXT.getType());
		gui.hintTextField().setFont(new Font("Trebuchet MS", Font.ITALIC, 12));
		
		gui.getPanel("login_main").setVisible(true);
		gui.panel().setLayout(new GridLayout(0, 1));
		gui.panel().setOpaque(false);

		gui.getPanel("login_main").add(gui.getLabel("whoAreYou"));
		gui.getPanel("login_main").add(gui.getHintTextField("username"));
		
		gui.getLabel("login_bg").add(gui.getPanel("login_main"));
		gui.getLabel("login_bg").setLayout(new FlowLayout(FlowLayout.CENTER, 0, -1));
		gui.getLabel("login_bg").setPreferredSize(new Dimension(gui.getWidth(), gui.getHeight()));
		gui.add(gui.getLabel("login_bg"));
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