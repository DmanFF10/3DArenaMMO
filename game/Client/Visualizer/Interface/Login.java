package Client.Visualizer.Interface;

import org.lwjgl.opengl.Display;

import Client.Manager.Callbacks;

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
	Callbacks.visualizerCBs callbacks;
	
	public Login(Callbacks.visualizerCBs cbs) {
		// sets callbacks
		callbacks = cbs;
		
		// display size
		int height = Display.getHeight()/2;
		int width = Display.getWidth()/2;
		// each objects sizes
		int objWidth, objHeight;
		
		loginPanel.setTheme("login-panel");
		
		// buttons
		/*objWidth = 65;
		objHeight = 30;
		loginButton.setTheme("button");
		loginButton.setSize(objWidth, objHeight);
		loginButton.setPosition(width-(objWidth/2), height+(objHeight*2));
		
		// textboxes
		objWidth = 150;
		objHeight = 20;
		nameTextBox.setTheme("textBox");
		nameTextBox.setSize(objWidth, objHeight);
		nameTextBox.setPosition(width-(objWidth/2), (int)(height-(objHeight*1.5)));
		
		passTextBox.setTheme("textBox");
		passTextBox.setSize(objWidth, objHeight);
		passTextBox.setPosition(width-(objWidth/2), height);
		passTextBox.setPasswordMasking(true);
		
		// labels
		objWidth = 5*nameLabel.getText().length();
		nameLabel.setTheme("label");
		nameLabel.setPosition(nameTextBox.getX()-(objWidth+nameTextBox.getWidth()/4),
		nameTextBox.getY()+(nameTextBox.getHeight()/2));
		
		objWidth = 5*passLabel.getText().length();
		passLabel.setTheme("label");
		passLabel.setPosition(passTextBox.getX()-(objWidth+(passTextBox.getWidth()/4)),
		passTextBox.getY()+(passTextBox.getHeight()/2));
		*/
		
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
				if(key == Event.KEY_RETURN){
					loginButton.requestKeyboardFocus();
				}
			}
		});
		
		loginButton.addCallback(new Runnable() {
            public void run() {
                callbacks.connect(nameTextBox.getText(), passTextBox.getText());
            }
        });
		
		DialogLayout.Group verticalGroup = loginPanel.createSequentialGroup()
				.addGroup(loginPanel.createParallelGroup(nameLabel, nameTextBox))
				.addGroup(loginPanel.createParallelGroup(passLabel, passTextBox))
				.addWidget(loginButton);
		
		loginPanel.setVerticalGroup(verticalGroup);
		
		add(loginPanel);
	}
}
