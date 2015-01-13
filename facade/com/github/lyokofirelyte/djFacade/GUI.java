package com.github.lyokofirelyte.djFacade;

import java.awt.Graphics;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class GUI extends JDialog {

	private static final long serialVersionUID = 1L;
	private boolean ready = false;

	public GUI(String name){
		this.name = name;
	}
	
	String name = "null";
	Map<String, Object> objects = new THashMap<String, Object>();
	Object curr;
	
	public String getName(){
		return name;
	}
	
	public JEditorPane getEditorPane(String name){
		return (JEditorPane) (curr = objects.get(name));
	}
	
	public JLabel getLabel(String name){
		return (JLabel) (curr = objects.get(name));
	}
	
	public JCheckBox getCheckBox(String name){
		return (JCheckBox) (curr = objects.get(name));
	}
	
	public JPanel getPanel(String name){
		return (JPanel) (curr = objects.get(name));
	}
	
	public JButton getButton(String name){
		return (JButton) (curr = objects.get(name));
	}
	
	public JTextField getTextField(String name){
		return (JTextField) (curr = objects.get(name));
	}
	
	public JTextPane getTextPane(String name){
		return (JTextPane) (curr = objects.get(name));
	}
	
	public JTextArea getTextArea(String name){
		return (JTextArea) (curr = objects.get(name));
	}
	
	public HintTextField getHintTextField(String name){
		return (HintTextField) (curr = objects.get(name));
	}
	
	public HintPasswordField getHintPasswordField(String name){
		return (HintPasswordField) (curr = objects.get(name));
	}
	
	public JEditorPane editorPane(){
		return (JEditorPane) curr;
	}

	public JLabel label(){
		return (JLabel) curr;
	}
	
	public JCheckBox checkBox(){
		return (JCheckBox) curr;
	}
	
	public JPanel panel(){
		return (JPanel) curr;
	}
	
	public JButton button(){
		return (JButton) curr;
	}
	
	public JTextField textField(){
		return (JTextField) curr;
	}
	
	public JTextPane textPane(){
		return (JTextPane) curr;
	}
	
	public JTextArea textArea(){
		return (JTextArea) curr;
	}
	
	public HintTextField hintTextField(){
		return (HintTextField) curr;
	}
	
	public HintPasswordField hintPasswordField(){
		return (HintPasswordField) curr;
	}
	
	public GUI addAttr(Object o, String name){
		objects.put(name, o);
		return this;
	}
	
	public GUI delAttr(String name){
		objects.remove(name);
		return this;
	}
	
	public boolean isReady(){
		return ready;
	}
	
	public void setReady(boolean ready){
		this.ready = ready;
	}
}