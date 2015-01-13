package com.github.lyokofirelyte.djFacade.Panels;

import com.github.lyokofirelyte.djFacade.GUI;

public interface Panel {
	public void display();
	public void hide();
	public void destroy();
	public GUI getGui();
}