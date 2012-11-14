package loclock.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class NotificationTabService extends Service{
	
	private final static NotificationServiceAsync notificationService = GWT.create(NotificationService.class);
	//private final NotificationPushServiceAsync notificationPushService = GWT.create(NotificationPushService.class);
	private static String notificationContents;
	private static HTMLFlow notificationHtmlFlow;
	private static VLayout notificationVLayout;
	private static SectionStackSection notificationSection;
	private static SectionStack sectionStack;
	
//	private static final Domain DOMAIN = DomainFactory.getDomain("my_domain");
	
	public NotificationTabService()
	{
		super("Notifications", "http://cdn1.iconfinder.com/data/icons/Project_Icons___Version_1_1_9_by_bogo_d/PNG/Notification.png");
		
		// test add notification into system
//		notificationService.addNotification(MainServices.account.getEmailAddress(), MainServices.account.getEmailAddress(), "I see you.", new AsyncCallback<Void>() {
//			
//			@Override
//			public void onSuccess(Void result) {
//				// TODO Auto-generated method stub
//				System.out.println("Successful notification added.");
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				System.out.println("Failed failed failed notification added.");
//				System.out.println(caught.getMessage());
//			}
//		});
		
		// Used for notification listening and auto pushing to client
//		RemoteEventServiceFactory theEventServiceFactory = RemoteEventServiceFactory.getInstance();
//		RemoteEventService theEventService = theEventServiceFactory.getRemoteEventService();
//		
//		theEventService.addListener(DOMAIN, new NotificationListener(){
//			public void onMyEvent(NotificationEvent event)
//			{
//				System.out.println("The message is from: "
//						+ event.getFromUser() + " to: " + event.getToUser()
//						+ " with the contents: " + event.getContent());
//			}
//		});
		
		VLayout layout = new VLayout();  
        layout.setMembersMargin(10);  
        
		// ====================Code used to show notifications in an accordian manner====================            
        HTMLFlow htmlFlow = new HTMLFlow();  
        //htmlFlow.setOverflow(Overflow.AUTO);  
        //htmlFlow.setPadding(10);  
  
        String contents = "<b>Severity 1</b><br> Vote for me!";  
  
        htmlFlow.setContents(contents); 
        
        VLayout obamaNotifications = new VLayout();
        obamaNotifications.addMember(htmlFlow);
        
        HTMLFlow htmlFlow2 = new HTMLFlow();  
        //htmlFlow2.setOverflow(Overflow.AUTO);  
        //htmlFlow2.setPadding(10);  
  
        String contents2 = "<b>New Friend</b><br> You are friends with Neil Ernst!";  
  
        htmlFlow2.setContents(contents2); 
        
        HTMLFlow htmlFlow3 = new HTMLFlow();  
        //htmlFlow3.setOverflow(Overflow.AUTO);  
        //htmlFlow3.setPadding(10);  
  
        String contents3 = "<b>Away</b><br> I am away this week!";  
  
        htmlFlow3.setContents(contents3); 
        
        VLayout ernstNotifications = new VLayout();
        ernstNotifications.addMember(htmlFlow2);
        ernstNotifications.addMember(htmlFlow3);
  
        ImgButton addButton = new ImgButton();  
        addButton.setSrc("[SKIN]actions/add.png");  
        addButton.setSize(16);  
        addButton.setShowFocused(false);  
        addButton.setShowRollOver(false);  
        addButton.setShowDown(false);  
        addButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            }  
        });  
  
        ImgButton removeButton = new ImgButton();  
        removeButton.setSrc("[SKIN]actions/remove.png");  
        removeButton.setSize(16);  
        removeButton.setShowFocused(false);  
        removeButton.setShowRollOver(false);  
        removeButton.setShowDown(false);  
        removeButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            }  
        });    
  
        sectionStack = new SectionStack();  
  
//        System.out.println(GWT.getHostPageBaseURL() + "world.png");
        String title = Canvas.imgHTML("http://www1.usaid.gov/images/icons/obama_icon.jpg") + " Obama";
        SectionStackSection section1 = new SectionStackSection(title);  
        //section1.setTitle("Obama");  
        //section1.setItems(listGrid);  
        //section1.setControls(addButton, removeButton);  
        section1.addItem(obamaNotifications);
        section1.setExpanded(true);  
  
        
        
        	
        String title2 = Canvas.imgHTML("https://si0.twimg.com/profile_images/990437145/neil-cartoon_normal.jpg") + " Neil Ernst";
        SectionStackSection section2 = new SectionStackSection(title2);  
        //section2.setTitle("Neil Ernst");  
        section2.addItem(ernstNotifications);
        //section2.addItem(htmlFlow3);
        //section2.setItems(statusReport);  
        //section2.setControls(form);  
        section2.setControls(removeButton);
        section2.setExpanded(true);  
  
        sectionStack.setSections(section1, section2);  
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);  
        sectionStack.setAnimateSections(true);  
        sectionStack.setWidth(300);  
        sectionStack.setHeight(400);  
        sectionStack.setOverflow(Overflow.HIDDEN);  
  
        layout.addMember(sectionStack);
        
        sectionStack.draw();
        // ====================Code used to show notifications in an accordian manner====================
		
		// ====================Code used to show notifications to user with animated 'fly onscreen'====================  
        final Label label = new Label("J. K. Rowling accepted your friend request! Yay.");  
        label.setParentElement(layout);  
        label.setShowEdges(true);  
        label.setBackgroundColor("#ffffd0");  
        label.setPadding(5);  
        label.setWidth(200);  
        label.setTop(50);  
        label.setLeft(-220); //start off screen  
        label.setValign(VerticalAlignment.CENTER);  
        label.setAlign(Alignment.CENTER);  
        label.setAnimateTime(1200); // milliseconds  
  
        IButton moveInButton = new IButton();  
        moveInButton.setTitle("Move In");  
        moveInButton.setLeft(40);  
        moveInButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
                label.animateMove(10, 50);  
                
        		notificationService.addNotification(MainServices.account.getEmailAddress(), MainServices.account.getEmailAddress(), "I see you.", "New friend", new AsyncCallback<Void>() {
    			
    			@Override
    			public void onSuccess(Void result) {
    				// TODO Auto-generated method stub
    				System.out.println("Successful notification added.");
    			}
    			
    			@Override
    			public void onFailure(Throwable caught) {
    				// TODO Auto-generated method stub
    				System.out.println("Failed failed failed notification added.");
    				System.out.println(caught.getMessage());
    			}
    		});
                
//				// Test code to send notification to server
//				notificationPushService.sendNotificationToServer(
//						MainServices.account.getEmailAddress(),
//						MainServices.account.getEmailAddress(), "Hihi",
//						new AsyncCallback<Void>() {
//
//							@Override
//							public void onSuccess(Void result) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onFailure(Throwable caught) {
//								// TODO Auto-generated method stub
//
//							}
//						});
            }  
        });  
  
        IButton moveOutButton = new IButton();  
        moveOutButton.setTitle("Move Out");  
        moveOutButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
                label.animateMove(-220, 50);  
            }  
        });  
  
        HLayout hLayout = new HLayout();  
        hLayout.setMembersMargin(10);  
        hLayout.addMember(moveInButton);  
        hLayout.addMember(moveOutButton);  
        layout.addMember(hLayout);  
  
        layout.draw();  
        
        // ====================Code used to show notifications to user with animated 'fly onscreen'====================
        
        this.setPane(layout);
        
        notificationService.getNotificationsByUsername(MainServices.account.getEmailAddress(), new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub
				for (String aString : result)
				{
					sectionStack.addSection(produceNewNotification(aString));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println(caught.getMessage());
			}
		});
	}
	
	private static SectionStackSection produceNewNotification(String content)
	{
		notificationContents = content;
		
		notificationHtmlFlow = new HTMLFlow();
		//notificationHtmlFlow.setOverflow(Overflow.AUTO);  
		//notificationHtmlFlow.setPadding(10);
		
		notificationHtmlFlow.setContents(notificationContents);
		
		notificationVLayout = new VLayout();
		notificationVLayout.addMember(notificationHtmlFlow);
		
		notificationSection = new SectionStackSection(MainServices.account.getEmailAddress());
		notificationSection.addItem(notificationVLayout);
		notificationSection.setExpanded(true);
		
		return notificationSection;
	}
	
	public static void updateNotificationsStack()
	{
		System.out.println("The updateNotificationStack was called from server!");
		notificationService.getNotificationsByUsername(
				MainServices.account.getEmailAddress(),
				new AsyncCallback<List<String>>() {

					@Override
					public void onSuccess(List<String> result) {
						// TODO Auto-generated method stub
						for (String aString : result) {
							sectionStack
									.addSection(produceNewNotification(aString));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println(caught.getMessage());
					}
				});
	}
}


