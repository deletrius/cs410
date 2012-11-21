package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SettingTabService extends Service{

	private final UserLocationServiceAsync userLocationService = GWT.create(UserLocationService.class);

	private VLayout layout;  
	
	private String publicSentence = "Your profile is <u>public</u>. Other users of this website besides your friends can discover you and see your location on the map when nearby.";  
    private String privateSentence = "Your profile is <u>private</u>. <b>Only</b> your friends can see your location on the map.";
	
  private HTMLFlow htmlFlow2 = new HTMLFlow();
  private HTMLFlow htmlFlowName = new HTMLFlow();
  
  private String firstName;
  private String lastName;
  
 
  private TextBox firstNameText = new TextBox();
  private TextBox lastNameText = new TextBox();
    
    public SettingTabService()
	{
		super("Settings", "http://i50.tinypic.com/16iintd.png");
		
        
		layout = new VLayout();  
		layout.setSize("100%", "15%");
        
        final HLayout horizLayout = new HLayout();
        
        htmlFlow2 = new HTMLFlow();  
        
        htmlFlow2.setOverflow(Overflow.AUTO);  
        
        htmlFlowName = new HTMLFlow();  
        
        htmlFlowName.setOverflow(Overflow.AUTO);  
  
          
  
        
        HTMLFlow htmlFlow = new HTMLFlow();  
  
        String contents = "<b>Privacy Setting</b><br> Decide how you would like to share your location.";  
        
        
  
        htmlFlow.setContents(contents); 
        horizLayout.addMember(htmlFlow);
        
        final ListBox dropBox = new ListBox(false);
        dropBox.addItem("Private", "private");
        dropBox.addItem("Public", "public");
        
        
        
        
        horizLayout.addMember(dropBox);
        horizLayout.addMember(htmlFlow2);
        
        layout.addMember(horizLayout);
        
        setUpNameFields();
        layout.draw();  
        
        this.setPane(layout);
        
        userLocationService.getUsersAsArrayList(MainServices.account.getEmailAddress(), new AsyncCallback<List<ArrayList<Object>>>() {

        	@Override
        	public void onSuccess(List<ArrayList<Object>> result) {
        		// TODO Auto-generated method stub
        		String privacy = (String) result.get(0).get(3);
        		
        		firstName = (String) result.get(0).get(4);
        		lastName = (String) result.get(0).get(5);
        		
        		if (firstName.length() > 0 || lastName.length() > 0)
				{
					htmlFlowName.setContents("Hello <b>" + firstName + " " + lastName + "</b>.");
					firstNameText.setText(firstName);
	        		lastNameText.setText(lastName);
				}
				else 
				{
					htmlFlowName.setContents("You don't have a name yet. <b>:(</b>");
				}
        		
//        		htmlFlowName.setContents("Hello <b>" + firstName + " " + lastName + "</b>.");
        		
        		layout.redraw();
        		
        		if (privacy != null)
        		{
        			if (privacy.equalsIgnoreCase("private"))
        			{
        				dropBox.setSelectedIndex(0);
        				htmlFlow2.setContents(privateSentence);
        				layout.redraw();
        				horizLayout.redraw();
        				System.out.println("setting drop list to private");
        			}
        			else if (privacy.equalsIgnoreCase("public"))
        			{
        				dropBox.setSelectedIndex(1);
        				htmlFlow2.setContents(publicSentence);
        				layout.redraw();
        				horizLayout.redraw();
        				System.out.println("setting drop list to public");
        			}
        		}
        		
        		
        	}

        	@Override
        	public void onFailure(Throwable caught) {
        		// TODO Auto-generated method stub
        		System.out.println("fail setting default");
        	}
        });



        dropBox.addChangeHandler(new ChangeHandler() {

        	@Override
        	public void onChange(ChangeEvent event) {
        		// TODO Auto-generated method stub
        		int selected = dropBox.getSelectedIndex();
        		if (selected == 0)
        		{
        			userLocationService.updateUserPrivacy(MainServices.account.getEmailAddress(), "private", new AsyncCallback<Void>() {

        				@Override
        				public void onSuccess(Void result) {
        					// TODO Auto-generated method stub
        					htmlFlow2.setContents(privateSentence);
        					layout.redraw();
        					horizLayout.redraw();
        					System.out.println("success at updating user privacy");
        					System.out.println("set privacy to private");
        				}

        				@Override
        				public void onFailure(Throwable caught) {
        					// TODO Auto-generated method stub
        					System.out.println("fail changing user to private");
        				}
        			});
        		}
        		else if (selected == 1)
        		{
        			userLocationService.updateUserPrivacy(MainServices.account.getEmailAddress(), "public", new AsyncCallback<Void>() {

        				@Override
        				public void onSuccess(Void result) {
        					// TODO Auto-generated method stub
        					htmlFlow2.setContents(publicSentence);
        					layout.redraw();
        					horizLayout.redraw();
        					System.out.println("success at updating user privacy");
        					System.out.println("set privacy to public");
        				}

        				@Override
        				public void onFailure(Throwable caught) {
        					System.out.println("fail changing user");
        				}
        			});
        		}
        	}
        });
	}
    
    private void setUpNameFields()
    {
    	HLayout horiz2Layout = new HLayout();
    	VLayout nameBoxes = new VLayout();
    	String contents = "<b>Personal Setting</b><br> First and last name.";
    	HTMLFlow htmlFlow = new HTMLFlow();
    	htmlFlow.setContents(contents);

    	horiz2Layout.addMember(htmlFlow);
    	
    	firstNameText = new TextBox();
    	firstNameText.getElement().setAttribute("placeholder", "First Name");
    	lastNameText = new TextBox();
    	lastNameText.getElement().setAttribute("placeholder", "Last Name");
    	
    	nameBoxes.addMember(firstNameText);
    	nameBoxes.addMember(lastNameText);
    	
    	horiz2Layout.addMember(nameBoxes);
    	horiz2Layout.addMember(htmlFlowName);
    	
    	firstNameText.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				// TODO Auto-generated method stub
				userLocationService.updateUserFirstName(MainServices.account.getEmailAddress(), firstNameText.getText(), new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						System.out.println("success first name change");
						firstName = firstNameText.getText();
						if (firstName.length() > 0 || lastName.length() > 0)
    					{
							htmlFlowName.setContents("Hello <b>" + firstName + " " + lastName + "</b>.");
    					}
    					else 
    					{
    						htmlFlowName.setContents("You don't have a name yet. <b>:(</b>");
    					}
						layout.redraw();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						System.out.println("failed first name change");
					}
				});
			}
		});
    	
    	lastNameText.addBlurHandler(new BlurHandler() {

    		@Override
    		public void onBlur(BlurEvent event) {
    			userLocationService.updateUserLastName(MainServices.account.getEmailAddress(), lastNameText.getText(), new AsyncCallback<Void>() {

    				@Override
    				public void onSuccess(Void result) {
    					System.out.println("success last name change");
    					lastName = lastNameText.getText();
    					if (firstName.length() > 0 || lastName.length() > 0)
    					{
    						htmlFlowName.setContents("Hello <b>" + firstName + " " + lastName + "</b>.");
    					}
    					else
    					{
    						htmlFlowName.setContents("You don't have a name yet. <b>:(</b>");
    					}
						layout.redraw();
    				}

    				@Override
    				public void onFailure(Throwable caught) {
    					System.out.println("failed last name change");
    				}
    			});
    		}
    	});
    	
    	layout.addMember(horiz2Layout);
    }
}
