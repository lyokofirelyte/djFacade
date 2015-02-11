package com.github.lyokofirelyte.djFacade;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;

/**
 * 
 * @author Jesse Bryan
 *
 */

public class HintPasswordField extends JPasswordField implements FocusListener {
	
	private static final long serialVersionUID = -5514915189051982880L;
	
	private String hint;
	private Color colour;
	
	private boolean showing;
	
	public HintPasswordField(String hint){
		
		super(hint);
		colour = new Color(155, 155, 155);
		
		this.hint = hint;
		this.showing = true;
		
		super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(216, 216, 216)), BorderFactory.createEmptyBorder(2, 5, 2, 2)));
		super.addFocusListener(this);
		
		super.setForeground(colour);
		super.setBackground(Color.white);
		
		super.setEchoChar((char) 0);
		
	}
	
	public void setHint(String hint){
		
		hideHint();
		this.hint = hint;
		showHint();
	}
	
	public void focusGained(FocusEvent e){
		
		if (getText().isEmpty()){
			
			hideHint();
			
		}
		
	}
	
	public void focusLost(FocusEvent e){
		
		if (getText().isEmpty()){
			
			showHint();
			
		}
		
	}
	
	@Override
	public String getText(){
		
		return showing ? "" : new String(super.getPassword());
		
	}
	
	@Override
	public void setText(String text){
		
		if (text.equals("")){
			
			showHint();
			
		} else {
			
			hideHint();
			super.setText(text);
			
		}
		
	}
	
	private void showHint(){
		
		super.setText(hint);
		super.setForeground(colour);
		super.setEchoChar((char) 0);
		
		showing = true;
		
	}
	
	public void hideHint(){
		
		super.setText("");
		super.setForeground(new Color(45, 45, 45));
		super.setEchoChar('\u2022');
		
		showing = false;
		
	}
			
			
	
}