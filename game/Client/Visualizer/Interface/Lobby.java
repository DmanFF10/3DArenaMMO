package Client.Visualizer.Interface;

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
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

public class Lobby extends Widget implements ILobby{
	
	final Button logoutButton = new Button("Logout");
	final Button settingsButton = new Button("Settings");
	final Button profileButton = new Button("Profile");
	final Button storeButton = new Button("Store");
	final Button newsButton = new Button("News");
	final Button playButton = new Button("Play");
	@SuppressWarnings("rawtypes")
	final ListBox playersListBox = new ListBox();
	final ChatFrame chatFrame;
	
	public Lobby(ICallbacks.visualizerCBs cbs){
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
		
		playButton.addCallback(new Runnable() {
            public void run() {
                chatFrame.appendText(1, "just wondering", "i did it!!");
            }
        });
		
		objWidth = width/4;
		objHeight = height-45;
		playersListBox.setTheme("listbox");
		playersListBox.setSize(objWidth, objHeight);
		playersListBox.setPosition(width-(objWidth+15), 35);
		
		chatFrame.setSize(300, 300);
		chatFrame.setPosition(0, height-(chatFrame.getHeight()));
		
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
	public void appendText(int type, String user, String message) {
		chatFrame.appendText(type, user, message);
		
	}
	@Override
	public void update() {
		chatFrame.update();
		
	}
}

class ChatFrame extends ResizableFrame {
	private final StringBuilder sb;
	private final HTMLTextAreaModel textAreaModel;
	private final TextArea textArea;
	private final EditField editField;
	private final ScrollPane scrollPane;
	ICallbacks.visualizerCBs callbacks;
	
	public ChatFrame(ICallbacks.visualizerCBs cbs) {
		callbacks = cbs;
		setTitle("Chat");
		
		this.sb = new StringBuilder();
		this.textAreaModel = new HTMLTextAreaModel();
		this.textArea = new TextArea(textAreaModel);
		this.editField = new EditField();
		
		editField.addCallback(new EditField.Callback() {
			public void callback(int key) {
				if(key == Event.KEY_RETURN) {
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
	
	public void appendText(int type, String username, String text){
		text = username + ": " + text;
		sb.append("<div style=\"word-wrap: break-word;\">");
		for(int i=0,l=text.length() ; i<l ; i++) {
			char ch = text.charAt(i);
			switch(ch) {
				case '<': sb.append("&lt;"); break;
				case '>': sb.append("&gt;"); break;
				case '&': sb.append("&amp;"); break;
				case '"': sb.append("&quot;"); break;
				default: sb.append(ch);
			}
		}
		sb.append("</div>");
		textAreaModel.setHtml(sb.toString());
		boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();
		
		if(isAtEnd) {
			scrollPane.validateLayout();
			scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
		}
	}
	
	public void update(){
		textAreaModel.setHtml(sb.toString());
	}
}
