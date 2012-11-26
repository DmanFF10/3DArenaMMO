package Client.Visualizer.Interface.Panels;

import java.util.ArrayList;

import Client.Manager.ICallbacks;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

public class chatFrame extends ResizableFrame {
	private final SimpleTextAreaModel textAreaModel;
	private final TextArea textArea;
	private final EditField editField;
	private final ScrollPane scrollPane;
	ICallbacks.visualizerCBs callbacks;
	
	public chatFrame(ICallbacks.visualizerCBs cbs) {
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
		this.setSize(300, 300);
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
