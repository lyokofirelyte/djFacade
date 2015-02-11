package com.github.lyokofirelyte.djFacade.Listeners;


import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import com.github.lyokofirelyte.djFacade.DJFacade;
import com.github.lyokofirelyte.djFacade.Identifiers.AR;

public class LineEventListener implements AR, LineListener {

	public DJFacade main;
	private boolean playing = false;
	
	public LineEventListener(DJFacade i){
		main = i;
	}
	
	@Override
	public void update(LineEvent e){
		
		if (e.getType().equals(Type.STOP)){
			playing = false;
			notifyAll();
		} else if (e.getType().equals(Type.START)){
			playing = true;
		}
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	public void setPlaying(boolean status){
		playing = status;
	}
}