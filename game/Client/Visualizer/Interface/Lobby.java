package Client.Visualizer.Interface;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import Client.Manager.ICallbacks;
import Client.Visualizer.Interface.Panels.debugPanel;
import Client.Visualizer.Interface.Panels.chatFrame;
import Client.Visualizer.Interface.Panels.lobbyButtonPanel;

import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class Lobby extends Widget {
	
	final debugPanel debugPanel = new debugPanel();
	final lobbyButtonPanel buttonPanel;
	final chatFrame chatFrame;
	final ListBox<String> playersListBox = new ListBox<String>();
	SimpleChangableListModel<String> sclm = new SimpleChangableListModel<String>();
	
	public Lobby(ICallbacks.visualizerCBs cbs, boolean debugMode){
		// display size
		int height = Display.getHeight();
		int width = Display.getWidth();
		// each objects sizes
		int objWidth, objHeight;
		
		chatFrame = new chatFrame(cbs);
		chatFrame.setPosition(0, height-(chatFrame.getHeight()));
		
		debugPanel.setVisible(debugMode);
		debugPanel.setTheme("debug-panel");
		debugPanel.setPosition(0, 50);
		
		buttonPanel = new lobbyButtonPanel(cbs);
		buttonPanel.setTheme("button-panel");
		buttonPanel.setSize(width-20, 30);
		
		objWidth = width/4;
		objHeight = height-60;
		playersListBox.setTheme("listbox");
		playersListBox.setSize(objWidth, objHeight);
		playersListBox.setPosition(width-(objWidth+20), 50);
		playersListBox.setModel(sclm);
		
		add(playersListBox);
		add(chatFrame);
		add(debugPanel);
		add(buttonPanel);
	}

	public void update(ArrayList<String> usernames, ArrayList<String[]> chat, int fps) {
		chatFrame.update(chat);
		int selected = playersListBox.getSelected();
		sclm.clear();
		sclm.addElements(usernames);
		if (selected < sclm.getNumEntries()){
			playersListBox.setSelected(selected);
		}
		debugPanel.update(fps, 0, 0, 0, 0, 0, 0);
	}
}
