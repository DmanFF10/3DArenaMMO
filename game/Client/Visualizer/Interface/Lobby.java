package Client.Visualizer.Interface;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import Client.Manager.ICallbacks;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

public class Lobby extends Widget implements ILobby{
	
	final Button logoutButton = new Button("Logout");
	final Button settingsButton = new Button("Settings");
	final Button profileButton = new Button("Profile");
	final Button storeButton = new Button("Store");
	final Button newsButton = new Button("News");
	final Button playButton = new Button("Play");
	final ListBox<String> playersListBox = new ListBox<String>();
	SimpleChangableListModel<String> sclm = new SimpleChangableListModel<String>();
	final ChatFrame chatFrame;
	ICallbacks.visualizerCBs callbacks;
	
	public Lobby(ICallbacks.visualizerCBs cbs){
		callbacks = cbs;
		chatFrame = new ChatFrame(cbs);
		// display size
		int height = Display.getHeight();
		int width = Display.getWidth();
		// each objects sizes
		int objWidth, objHeight;
		
		objWidth = 65;
		objHeight = 30;
		logoutButton.setTheme("button");
		logoutButton.setSize(objWidth, objHeight);
		logoutButton.setPosition(width-(objWidth+15), 0);
		
		settingsButton.setTheme("button");
		settingsButton.setSize(objWidth, objHeight);
		settingsButton.setPosition(width-((objWidth+15)*2), 0);
		
		profileButton.setTheme("button");
		profileButton.setSize(objWidth, objHeight);
		profileButton.setPosition(width-((objWidth+15)*3), 0);
		
		storeButton.setTheme("button");
		storeButton.setSize(objWidth, objHeight);
		storeButton.setPosition(width-((objWidth+15)*4), 0);
		
		newsButton.setTheme("button");
		newsButton.setSize(objWidth, objHeight);
		newsButton.setPosition(width-((objWidth+15)*5), 0);
		
		playButton.setTheme("button");
		playButton.setSize(objWidth, objHeight);
		playButton.setPosition(width-((objWidth+15)*6), 0);
		
		objWidth = width/4;
		objHeight = height-45;
		playersListBox.setTheme("listbox");
		playersListBox.setSize(objWidth, objHeight);
		playersListBox.setPosition(width-(objWidth+15), 35);
		playersListBox.setModel(sclm);
		
		chatFrame.setSize(300, 300);
		chatFrame.setPosition(0, height-(chatFrame.getHeight()));
		
		logoutButton.addCallback(new Runnable() {
            public void run() {
            	callbacks.logout();
            }
        });
		
		add(logoutButton);
		add(settingsButton);
		add(profileButton);
		add(storeButton);
		add(newsButton);
		add(playButton);
		add(playersListBox);
		add(chatFrame);
	}
	@Override
	public void update(ArrayList<String> usernames, ArrayList<String[]> chat) {
		chatFrame.update(chat);
		sclm.clear();
		sclm.addElements(usernames);
		
	}
}

class ChatFrame extends ResizableFrame {
	private final SimpleTextAreaModel textAreaModel;
	private final TextArea textArea;
	private final EditField editField;
	private final ScrollPane scrollPane;
	ICallbacks.visualizerCBs callbacks;
	
	public ChatFrame(ICallbacks.visualizerCBs cbs) {
		callbacks = cbs;
		setTitle("Chat");

		this.textAreaModel = new SimpleTextAreaModel();
		this.textArea = new TextArea(textAreaModel);
		this.editField = new EditField();
		
		editField.addCallback(new EditField.Callback() {
			public void callback(int key) {
				if(key == Event.KEY_RETURN && !editField.getText().equals("")) {
					callbacks.sendChat(editField.getText());
					editField.setText("");
				}
			}
		});
		
		scrollPane = new ScrollPane(textArea);
		scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
		
		DialogLayout chatBox = new DialogLayout();
		chatBox.setTheme("content");
		chatBox.setHorizontalGroup(chatBox.createParallelGroup(scrollPane, editField));
		chatBox.setVerticalGroup(chatBox.createSequentialGroup(scrollPane, editField));
		
		add(chatBox);
	}
	
	public void update(ArrayList<String[]> chat){
		String chatString = new String();
		for (String[] data : chat){
			chatString += (data[0] + ": " + data[1]+ "\n");
		}
		textAreaModel.setText(chatString);
		boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();
		
		if(isAtEnd) {
			scrollPane.validateLayout();
			scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
		}
	}
}
