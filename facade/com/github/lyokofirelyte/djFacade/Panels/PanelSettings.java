package com.github.lyokofirelyte.djFacade.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.GUI;
import com.github.lyokofirelyte.djFacade.HintTextField;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.ActionCommand;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.SliderEventListener;

public class PanelSettings implements AR, Panel {
	
	public DJFacade main;
	private GUI gui;
	
	public PanelSettings(DJFacade i){
		main = i;
	}

	public void display(){
		gui = new GUI("settings");
		gui.setLayout(new FlowLayout());
		gui.setResizable(false);
		gui.setTitle("djFacade");
		gui.setUndecorated(true);
		gui.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		
		GUI mainGui = main.getPanel(Resource.MAIN).getGui();
		
		gui.addAttr(new JLabel(main.getImage("bg4.png", 720, 300)), "bg");
		gui.addAttr(new JPanel(), "main");
		gui.addAttr(new JPanel(), "allowedButtons");
		gui.addAttr(new JPanel(), "transparency");
		gui.addAttr(new JPanel(), "info");
		gui.addAttr(new JPanel(), "colors");
		gui.addAttr(new JTextArea(), "info_text");
		gui.addAttr(new JCheckBox(), "tool_tip");
		gui.addAttr(new JCheckBox(), "on_top");
		gui.addAttr(new JCheckBox(), "applyPanel");
		gui.addAttr(new JCheckBox(), "applyText");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 255, Math.round(main.files.get(Resource.SETTINGS).getFloat("color_slider_red")*100)), "color_slider_red");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 255, Math.round(main.files.get(Resource.SETTINGS).getFloat("color_slider_green")*100)), "color_slider_green");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 255, Math.round(main.files.get(Resource.SETTINGS).getFloat("color_slider_blue")*100)), "color_slider_blue");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 1000, main.files.get(Resource.SETTINGS).getInt("main_height")), "size_slider");
		gui.addAttr(new HintTextField(main.files.get(Resource.SETTINGS).getStr("color_tint")), "color_tint");
		gui.addAttr(new JSlider(JSlider.HORIZONTAL, 0, 100, Math.round(main.files.get(Resource.SETTINGS).getFloat("transparency")*100)), "transparency_slider");
		gui.addAttr(new JLabel(main.getImage("icons/ic_action_accept.png", 70, 70)), "accept");
		gui.getLabel("bg").setPreferredSize(new Dimension(700, 300));
		gui.label().setOpaque(true);
		gui.label().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gui.label().setLayout(new FlowLayout(0, 0, 0));
		gui.getPanel("main").setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		gui.panel().setLayout(new GridLayout(0, 1));
		
		for (String button : main.getMouseListener().map.keySet()){
			if (!button.contains("settings")){
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
			}
		}

		gui.getPanel("allowedButtons").setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "<div style='font-size: 15'>Enable/Disable Buttons</div>" + main.loadStyle("setup_end.dj"));
		b.setTitleJustification(TitledBorder.CENTER);
		gui.panel().setBorder(b);
		gui.getPanel("main").add(gui.getPanel("allowedButtons"));
		
		gui.getSlider("size_slider").addChangeListener(new SliderEventListener(main, "size"));
		gui.slider().setOpaque(false);
		gui.slider().setMinorTickSpacing(1);
		gui.slider().setMajorTickSpacing(100);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Size" + main.loadStyle("setup_end.dj")));
		gui.slider().setPaintTicks(true);
		
		gui.getSlider("transparency_slider").setMajorTickSpacing(20);
		gui.slider().setMinorTickSpacing(10);
		gui.slider().setOpaque(false);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Opacity" + main.loadStyle("setup_end.dj")));
		gui.slider().addChangeListener(new SliderEventListener(main, "transparency"));
		gui.slider().setPaintTicks(true);
		
		gui.getSlider("color_slider_red").setMajorTickSpacing(20);
		gui.slider().setMinorTickSpacing(1);
		gui.slider().setOpaque(false);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Red Tint" + main.loadStyle("setup_end.dj")));
		gui.slider().addChangeListener(new SliderEventListener(main, "color_slider_red"));
		gui.slider().setPaintTicks(true);
		
		gui.getSlider("color_slider_green").setMajorTickSpacing(20);
		gui.slider().setMinorTickSpacing(1);
		gui.slider().setOpaque(false);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Green Tint" + main.loadStyle("setup_end.dj")));
		gui.slider().addChangeListener(new SliderEventListener(main, "color_slider_green"));
		gui.slider().setPaintTicks(true);
		
		gui.getSlider("color_slider_blue").setMajorTickSpacing(20);
		gui.slider().setMinorTickSpacing(1);
		gui.slider().setOpaque(false);
		gui.slider().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), main.loadStyle("setup.dj") + "Blue Tint" + main.loadStyle("setup_end.dj")));
		gui.slider().addChangeListener(new SliderEventListener(main, "color_slider_blue"));
		gui.slider().setPaintTicks(true);
		
		gui.getPanel("transparency").setOpaque(false);
		gui.panel().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		//gui.getPanel("transparency").add(gui.getSlider("size_slider"));
		gui.getPanel("transparency").add(gui.getSlider("transparency_slider"));
		
		gui.getPanel("colors").add(gui.getSlider("color_slider_red"));
		gui.getPanel("colors").add(gui.getSlider("color_slider_green"));
		gui.getPanel("colors").add(gui.getSlider("color_slider_blue"));
		gui.getPanel("colors").setOpaque(false);
		
		gui.getCheckBox("tool_tip").setText(main.loadStyle("setup.dj") + "Tool Tips" + main.loadStyle("setup_end.dj"));
		gui.checkBox().setSelected(main.files.get(Resource.SETTINGS).getBool("tool_tips"));
		gui.checkBox().addActionListener(main.getEventListener());
		gui.checkBox().setActionCommand(ActionCommand.TOOL_TIPS.toString());
		gui.checkBox().setOpaque(false);
		
		gui.getCheckBox("on_top").setText(main.loadStyle("setup.dj") + "On Top" + main.loadStyle("setup_end.dj"));
		gui.checkBox().setSelected(main.files.get(Resource.SETTINGS).getBool("on_top"));
		gui.checkBox().addActionListener(main.getEventListener());
		gui.checkBox().setActionCommand(ActionCommand.ON_TOP.toString());
		gui.checkBox().setOpaque(false);
		
		gui.getCheckBox("applyPanel").setText(main.loadStyle("setup.dj") + "Apply to Panel" + main.loadStyle("setup_end.dj"));
		gui.checkBox().setSelected(main.files.get(Resource.SETTINGS).getBool("applyPanel"));
		gui.checkBox().addActionListener(main.getEventListener());
		gui.checkBox().setActionCommand(ActionCommand.PANEL.toString());
		gui.checkBox().setOpaque(false);
		
		gui.getCheckBox("applyText").setText(main.loadStyle("setup.dj") + "Apply to Text" + main.loadStyle("setup_end.dj"));
		gui.checkBox().setSelected(main.files.get(Resource.SETTINGS).getBool("applyText"));
		gui.checkBox().addActionListener(main.getEventListener());
		gui.checkBox().setActionCommand(ActionCommand.TEXT.toString());
		gui.checkBox().setOpaque(false);
		
		gui.getTextArea("info_text").setText("djFacade Settings v1.0");
		gui.textArea().setOpaque(false);
		gui.textArea().setForeground(Color.WHITE);
		gui.textArea().setFont(new Font("Trebuchet MS", Font.ITALIC, 12));
		gui.getPanel("info").setOpaque(false);
		
		gui.getPanel("info").add(gui.getCheckBox("applyPanel"));
		gui.getPanel("info").add(gui.getCheckBox("applyText"));
		gui.getPanel("info").add(gui.getCheckBox("tool_tip"));
		gui.getPanel("info").add(gui.getCheckBox("on_top"));
		gui.getPanel("info").add(gui.getTextArea("info_text"));
		gui.getPanel("main").add(gui.getPanel("transparency"));
		gui.getPanel("main").add(gui.getPanel("colors"));
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