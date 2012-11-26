package Client.Visualizer.Interface.Panels;

import Client.Manager.ICallbacks;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;

public class lobbyButtonPanel extends DialogLayout {
	final Button logout = new Button("Logout");
	final Button settings = new Button("Settings");
	final Button profile = new Button("Profile");
	final Button store = new Button("Store");
	final Button news = new Button("News");
	final Button play = new Button("Play");
	ICallbacks.visualizerCBs callbacks;
	public lobbyButtonPanel(ICallbacks.visualizerCBs cbs){
		callbacks = cbs;
		
		logout.addCallback(new Runnable() {
            public void run() {
            	callbacks.logout();
            }
        });
		
		this.setHorizontalGroup(this.createSequentialGroup(play, news, store, profile, settings, logout));
		this.setVerticalGroup(this.createParallelGroup(play, news, store, profile, settings, logout));
	}
}
