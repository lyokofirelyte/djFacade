package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.SliderEventListener;

public class PanelQueue implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelQueue(DJFacade i){
		main = i;
	}

	public void display(){
		gui = new GUI("queue");
		gui.setLayout(new FlowLayout());
		gui.setResizable(false);
		gui.setTitle("djFacade");
		gui.setUndecorated(true);
		gui.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		
		GUI mainGui = main.getPanel(Resource.MAIN).getGui();
		
		gui.addAttr(new JLabel(main.getImage("settings.png")), "bg");
		gui.addAttr(new JPanel(), "main");
		gui.addAttr(new JPanel(), "allowedButtons");
		gui.addAttr(new JPanel(), "transparency");
		gui.addAttr(new JPanel(), "info");
		gui.addAttr(new JTextArea(), "info_text");
		gui.addAttr(new HintTextField(main.files.get(Resource.SETTINGS).getStr("color_tint")), "color_tint");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 100, Math.round(main.files.get(Resource.SETTINGS).getFloat("transparency")*100)), "transparency_slider");
		gui.addAttr(new JLabel(main.getImage("icons/ic_action_accept.png", 70, 70)), "accept");
		gui.getLabel("bg").setPreferredSize(new Dimension(700, 170));
		gui.label().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gui.label().setLayout(new FlowLayout(0, 0, 0));
		gui.getPanel("main").setPreferredSize(new Dimension(700, 220));
		gui.panel().setLayout(new GridLayout(0, 1));
		gui.panel().setOpaque(false);
		gui.panel().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.05f));
		
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
			gui.getPanel("allowedButtons").setOpaque(false);
			gui.panel().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.05f));
		}

		gui.getPanel("allowedButtons").setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "<div style='font-size: 15'>Enable/Disable Buttons</div>" + main.loadStyle("setup_end.dj"));
		b.setTitleJustification(TitledBorder.CENTER);
		gui.panel().setBorder(b);
		gui.getPanel("main").add(gui.getPanel("allowedButtons"));
		
		gui.getPanel("transparency").setOpaque(false);
		gui.panel().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		gui.getSlider("transparency_slider").setMajorTickSpacing(20);
		gui.slider().setMinorTickSpacing(1);
		gui.slider().setOpaque(false);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Opacity" + main.loadStyle("setup_end.dj")));
		gui.slider().addChangeListener(new SliderEventListener(main, "transparency"));
		gui.slider().setPaintTicks(true);
		
		gui.getPanel("transparency").add(gui.getSlider("transparency_slider"));
		
		gui.getHintTextField("color_tint").setOpaque(false);
		gui.hintTextField().addActionListener(main.getEventListener());
		gui.hintTextField().setActionCommand(ActionCommand.COLOR.toString());
		gui.hintTextField().setPreferredSize(new Dimension(140, 60));
		gui.hintTextField().setFont(new Font("Trebuchet MS", Font.ITALIC, 12));
		gui.hintTextField().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.05f));
		gui.hintTextField().setForeground(Color.ORANGE);
		gui.hintTextField().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Color Tint (HEX)" + main.loadStyle("setup_end.dj")));
		
		gui.getTextArea("info_text").setText("I hope you people enjoy this thing, it took me a long ass time! -David");
		gui.textArea().setOpaque(false);
		gui.textArea().setForeground(Color.WHITE);
		gui.textArea().setFont(new Font("Trebuchet MS", Font.ITALIC, 12));
		gui.getPanel("info").setOpaque(false);
		
		gui.getPanel("transparency").add(gui.getHintTextField("color_tint"));
		gui.getPanel("info").add(gui.getTextArea("info_text"));
		gui.getPanel("main").add(gui.getPanel("transparency"));
		gui.getPanel("main").add(gui.getPanel("info"));
		
		gui.getLabel("bg").add(gui.getPanel("main"));
		gui.add(gui.getLabel("bg"));
		
		gui.setLocation(mainGui.getX(), (mainGui.getY() - mainGui.getHeight()) - 200);
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