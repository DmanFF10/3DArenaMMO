package Client.Visualizer.Interface;

import org.lwjgl.opengl.Display;

import Client.Manager.ICallbacks;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class Login extends Widget {
	final Button loginButton = new Button("Login");
	final EditField nameTextBox = new EditField();
	final EditField passTextBox = new EditField();
	final Label nameLabel = new Label("UserName:");
	final Label passLabel = new Label("Password:");
	final DialogLayout loginPanel = new DialogLayout();
	ICallbacks.visualizerCBs callbacks;
	
	public Login(ICallbacks.visualizerCBs cbs) {
		// sets callbacks
		callbacks = cbs;
		
		// display size
		int height = Display.getHeight()/2;
		int width = Display.getWidth()/2;
		// each objects sizes
		int objWidth, objHeight;
	
		passTextBox.setPasswordMasking(true);
		
		// event listeners
		nameTextBox.addCallback(new Callback() {
			public void callback(int key) {
				if(key == Event.KEY_RETURN) {
					passTextBox.requestKeyboardFocus();
				}
			}
		});
		
		passTextBox.addCallback(new Callback(){
			public void callback(int key){
				if(key == Event.KEY_RETURN && test()){
					callbacks.connect(nameTextBox.getText(), passTextBox.getText());
				}
			}
		});
		
		loginButton.addCallback(new Runnable() {
            public void run() {
            	if (test()){
            		callbacks.connect(nameTextBox.getText(), passTextBox.getText());
            	}
            }
        });
		


		DialogLayout.Group hLabels = loginPanel.createParallelGroup(nameLabel, passLabel);
		DialogLayout.Group hTextBoxs = loginPanel.createParallelGroup(nameTextBox, passTextBox);
		DialogLayout.Group hButton = loginPanel.createSequentialGroup().addGap().addWidget(loginButton);

		loginPanel.setHorizontalGroup(loginPanel.createParallelGroup()
				.addGroup(loginPanel.createSequentialGroup(hLabels, hTextBoxs))
				.addGroup(hButton));

		loginPanel.setVerticalGroup(loginPanel.createSequentialGroup()
				.addGroup(loginPanel.createParallelGroup(nameLabel, nameTextBox))
				.addGroup(loginPanel.createParallelGroup(passLabel, passTextBox))
				.addWidget(loginButton));
		
		objWidth = width/2;
		objHeight = 90;
		loginPanel.setTheme("login-panel");
		loginPanel.setSize(objWidth, objHeight);
		loginPanel.setPosition(width-(objWidth/2), height-(objHeight/2));
		
		add(loginPanel);
	}
	
	private boolean test(){
		if (nameTextBox.getText().equals("")){
			nameTextBox.requestKeyboardFocus();
			return false;
		}
		if (passTextBox.getText().equals("")){
			passTextBox.requestKeyboardFocus();
			return false;
		}
		return true;
	}
}
