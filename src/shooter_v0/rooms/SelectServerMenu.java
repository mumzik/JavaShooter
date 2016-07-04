package shooter_v0.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import shooter_v0.Engine;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.helper_parent.Menu;

public class SelectServerMenu extends InsertedMenu {

	private Button nextButton;
	private Text selectIpEdit;
	private Label selectIpLabel;

	public SelectServerMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine, backMenu);
		name="меню выбора сервера";
		int leftBorder=composite.getClientArea().width/2-COLUMN_WIDHT/2;
		selectIpLabel=new Label(composite, SWT.NONE);
		selectIpLabel.setText("введите ip сервера");
		selectIpLabel.setBounds(leftBorder, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		selectIpEdit=new Text(composite, SWT.NONE);
		selectIpEdit.setText("127.0.0.1");
		selectIpEdit.setBounds(leftBorder, BUTTONS_HEIGHT+1, COLUMN_WIDHT, BUTTONS_HEIGHT);
		nextButton=new Button(composite, SWT.PUSH);
		nextButton.setText("присоедениться");
		nextButton.setBounds(composite.getClientArea().width-COLUMN_WIDHT, composite.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}

	private void setListeners() {
		setNextButLst();
	}

	private void setNextButLst() {	
		nextButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.client.connect(selectIpEdit.getText());
				parentEngine.netType="client";
				parentEngine.joinGameMenu.open();					
			}			
		});	
	}

	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}


}
