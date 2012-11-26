package Client.Visualizer.Interface.Panels;

import Client.Manager.ICallbacks;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.EditField.Callback;

public class loginPanel extends DialogLayout {
	final Button login = new Button("Login");
	final EditField tbname = new EditField();
	final EditField tbpass = new EditField();
	final Label lname = new Label("UserName:");
	final Label lpass = new Label("Password:");
	final ICallbacks.visualizerCBs callbacks;
	
	public loginPanel(ICallbacks.visualizerCBs cbs){
		this.callbacks = cbs;
		tbpass.setPasswordMasking(true);
		
		// event listeners
		tbname.addCallback(new Callback() {
			public void callback(int key) {
				if(key == Event.KEY_RETURN) {
					tbpass.requestKeyboardFocus();
				}
			}
		});
		
		tbpass.addCallback(new Callback(){
			public void callback(int key){
				if(key == Event.KEY_RETURN && test()){
					callbacks.connect(tbname.getText(), tbpass.getText());
				}
			}
		});
		
		login.addCallback(new Runnable() {
            public void run() {
            	if (test()){
            		callbacks.connect(tbname.getText(), tbpass.getText());
            	}
            }
        });


		DialogLayout.Group hLabels = this.createParallelGroup(lname, lpass);
		DialogLayout.Group hTextBoxs = this.createParallelGroup(tbname, tbpass);
		DialogLayout.Group hButton = this.createSequentialGroup().addGap().addWidget(login);

		this.setHorizontalGroup(this.createParallelGroup()
				.addGroup(this.createSequentialGroup(hLabels, hTextBoxs))
				.addGroup(hButton));

		this.setVerticalGroup(this.createSequentialGroup()
				.addGroup(this.createParallelGroup(lname, tbname))
				.addGroup(this.createParallelGroup(lpass, tbpass))
				.addWidget(login));
		
		this.setTheme("login-panel");
		this.setSize(250, 100);
	}
	
	private boolean test(){
		if (tbname.getText().equals("")){
			tbname.requestKeyboardFocus();
			return false;
		}
		if (tbpass.getText().equals("")){
			tbpass.requestKeyboardFocus();
			return false;
		}
		return true;
	}
}
