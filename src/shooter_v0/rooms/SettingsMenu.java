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

public class SettingsMenu extends InsertedMenu{

	private Label selectLoginLabel;
	private Text selectLoginEdit;
	private Button submitButton;

	public SettingsMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine, backMenu);
		name="���������";
		int leftBorder=composite.getClientArea().width/2-COLUMN_WIDHT/2;
		selectLoginLabel=new Label(composite, SWT.NONE);
		selectLoginLabel.setText("������� ����� ���������");
		selectLoginLabel.setBounds(leftBorder, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		selectLoginEdit=new Text(composite, SWT.NONE);
		selectLoginEdit.setBounds(leftBorder, BUTTONS_HEIGHT+1, COLUMN_WIDHT, BUTTONS_HEIGHT);
		selectLoginEdit.setText(parentEngine.login);
		submitButton=new Button(composite, SWT.PUSH);
		submitButton.setText("���������");
		submitButton.setBounds(composite.getClientArea().width-COLUMN_WIDHT, composite.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}
	private void setListeners() {
		setSubmitButLst();
	}

	private void setSubmitButLst() {	
		submitButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				parentEngine.login=selectLoginEdit.getText();
				parentEngine.showMessage("������������ ���������");					
			}			
		});	
	}
	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}


}
