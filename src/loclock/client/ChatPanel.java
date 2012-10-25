package loclock.client;


//import java.util.Calendar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
	private List<Object> messageBuffer;
	

	public ChatPanel(String from, String to)
	{		
		super(to);
		DataSourceTextField dt1=new DataSourceTextField("inputText","InputText",250,true);
		DataSourceTextField dt2=new DataSourceTextField("convoText","ConvoText");
		messageBuffer=new ArrayList<Object>();

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

//	public void removeBufferDuplicates()
//	{
//		HashSet h= new HashSet(messageBuffer);
//		messageBuffer.clear();
//		messageBuffer.addAll(h);
//		Set<Object> s = new TreeSet<Object>(new Comparator<Object>() {
//
//	        @Override
//	        public int compare(Object o1, Object o2) {
//	        	String[] m1=(String[])o1;
//	        	String[] m2=(String[])o2;
//	        	
//	            for (int i =0;i<m1.length;i++)
//	            {
//	            	if (m1[i].compareTo(m2[i])!=0)
//	            	{
//	            		System.out.println(m1[i]+" "+m2[i]);
//	            		return 1;
//	            	}
//	            }
//	            //System.out.println("lol");
//	            return 0;
//	        }
//	    });
//		
//		 s.addAll(messageBuffer);
//		 messageBuffer.clear();
//		 messageBuffer.addAll(s);
//		 //sortedMessageBuffer = Arrays.asList(s.toArray());
//		 //messageBuffer.add(new String[]{"0","1","2","4"});
//		 
//		Collections.sort(messageBuffer,new Comparator<Object>() {
//
//	        @Override
//	        public int compare(Object o1, Object o2) {
//	        	String[] m1=(String[])o1;
//	        	String[] m2=(String[])o2;
//	        	if (new Date(m1[3]).before(new Date(m2[3])))
//	        		return -1;
//	        	else if (new Date(m1[3]).after(new Date(m2[3])))
//	        		return 1;
//	            return 0;
//	        }
//	    });
//	   
//	    
//	}
	public void updateConvoTextBox(String content, String Date)
	{
		updateConvoTextBox(fromUserName, content, Date);
	}
	
//	public void refreshConvoTextBox()
//	{
//		convoTextBox.clearValue();
//		for (Object i: messageBuffer)
//		{
//			String[] m=(String[]) i;
//			if (convoTextBox.getValueAsString()!=null)
//				convoTextBox.setValue(m[0]+" ("+m[3]+"):"+"\n"+m[2]+"\n"+convoTextBox.getValueAsString());
//			else			
//				convoTextBox.setValue(m[0]+" ("+m[3]+"):"+"\n"+m[2]);
//		}
//	}
	public void updateConvoTextBox(String fromUser, String content, String Date)
	{
		if (convoTextBox.getValueAsString()!=null)
			convoTextBox.setValue(fromUser+" ("+Date+"):"+"\n"+content+"\n"+convoTextBox.getValueAsString());
		else			
			convoTextBox.setValue(fromUser+" ("+Date+"):"+"\n"+content);
//		String[] m=new String[4];
//		m[0]=fromUser;
//		m[1]="";
//		m[2]=content;
//		m[3]=Date;
//		System.out.println(m[0]+" "+m[1]+" "+m[2]+" "+m[3]+" ");
//		
//		System.out.println(messageBuffer==null);
//		
//		messageBuffer.add(m);
		
//		removeBufferDuplicates();
//		refreshConvoTextBox();

		//@@ TODO change from user to "me"

	}
	

	public void sendMessage(String content)
	{

		try {
			final Date timestamp=new Date();
			messageService.sendMessage(fromUserName, toUserName,content, timestamp, 
					new AsyncCallback<Void>(){

				@Override
				public void onFailure(Throwable caught) {
					updateConvoTextBox("Failed to send the following message: "+ inputTextBox.getValueAsString(),timestamp.toString());
				}

				@Override
				public void onSuccess(Void result) {
					updateConvoTextBox(inputTextBox.getValueAsString(),timestamp.toString());
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
