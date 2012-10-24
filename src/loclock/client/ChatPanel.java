package loclock.client;


//import java.util.Calendar;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FormLayoutType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.tab.Tab;

public class ChatPanel extends Tab{

	private MessageServiceAsync messageService = GWT.create(MessageService.class);

	private String fromUserName;
	private String toUserName;
	private TextAreaItem convoTextBox;
	//	private HorizontalPanel inputPane;
	private TextAreaItem inputTextBox;
	private ButtonItem sendBtn;
	private DynamicForm form;


	public ChatPanel(String from, String to)
	{		
		super(to);
		DataSourceTextField dt1=new DataSourceTextField("inputText","InputText",250,true);
		DataSourceTextField dt2=new DataSourceTextField("convoText","ConvoText");


		//
		DataSource ds=new DataSource();
		//ds.setID(to);
		ds.setFields(dt1,dt2);
		ds.setClientOnly(true);
		//		

		form=new DynamicForm();
		form.setWidth("100%");
		form.setHeight("20%");
		form.setIsGroup(true);
		form.setNumCols(4);  		
		form.setAutoFocus(false);  
		//		form.setUseAllDataSourceFields(true); 
		form.setDataSource(ds);

		inputTextBox=new TextAreaItem("inputText");
		inputTextBox.setWidth("*");
		inputTextBox.setHeight("20");
		inputTextBox.setColSpan(2);
		inputTextBox.setRowSpan(2);
		inputTextBox.setDefaultValue("");
		inputTextBox.setShowTitle(false);
		inputTextBox.setBrowserSpellCheck(true);
		inputTextBox.addKeyPressHandler(new KeyPressHandler(){

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName().equals("Enter"))
				{

					sendMessage(inputTextBox.getValueAsString());
					event.cancel();
				}

			}});


		sendBtn=new ButtonItem("send","Send");
		sendBtn.setWidth("*");
		sendBtn.setAlign(Alignment.LEFT); 
		sendBtn.setHeight("20");
		sendBtn.setColSpan(2);	
		sendBtn.setRowSpan(2);
		sendBtn.setShowTitle(false);
		sendBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {

				sendMessage(inputTextBox.getValueAsString());
			}});


		convoTextBox=new TextAreaItem("convoText");
		convoTextBox.setWidth("*");
		convoTextBox.setHeight("180");
		convoTextBox.setColSpan(4);
		convoTextBox.setRowSpan(3);
		convoTextBox.setShowTitle(false);
		convoTextBox.setCanFocus(false);

		convoTextBox.setCanEdit(false);
		convoTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				event.cancel();
				//cursorPos = convoTextBox.getSelectionRange()[0];
			}
		});


		//form.setColWidths(80,80,80,80);
		form.setFields(convoTextBox,inputTextBox,sendBtn);

		this.setCanClose(true);

		this.setPane(form);
		//this.add(inputPane);
		fromUserName=from;
		toUserName=to;
		
	}

	public void updateConvoTextBox(String content)
	{
		if (convoTextBox.getValueAsString()!=null)
			convoTextBox.setValue(content+"\n"+convoTextBox.getValueAsString());
		else			
			convoTextBox.setValue(content);

		//@@ TODO change from user to "me"

	}

	public void sendMessage(String content)
	{

		try {
			messageService.sendMessage(fromUserName, toUserName,content, new Date (), 
					new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					updateConvoTextBox("Failed to send the following message: "+ inputTextBox.getValueAsString());
				}

				@Override
				public void onSuccess(Void result) {
					updateConvoTextBox(fromUserName +": "+ inputTextBox.getValueAsString());
					inputTextBox.clearValue();	
				}});
		} catch (NotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub


	}

	public String getToUserName()
	{
		return toUserName;
	}

}
