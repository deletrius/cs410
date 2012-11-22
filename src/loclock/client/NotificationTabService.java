package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationAcceleration;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.Visibility;
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
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;

public class NotificationTabService extends Service {

	private final static NotificationServiceAsync notificationService = GWT
			.create(NotificationService.class);
	private static CalendarServiceAsync calendarService = GWT
			.create(CalendarService.class);
	// private final NotificationPushServiceAsync notificationPushService =
	// GWT.create(NotificationPushService.class);
	private static String notificationContents;
	private static HTMLFlow notificationHtmlFlow;
	private static VLayout notificationVLayout;
	private static SectionStackSection notificationSection;
	private static SectionStack sectionStack;
	private static List<String> currentlyShownNotifications;
	private static ImgButton removeButton;
	//private static TimeTableService timeTableService;
	private static Timer refreshTimer;
	

	private static List<String> stackIdsRemoved;


	private static Label popInNotificationLabel;
	
	private HLayout rightTabLayout;
	
	private boolean isFirstRun = true;
	
	private VLayout notificationPopInPanel;
	
	private int numberOfNewNotifications = 0;
	private HTMLFlow popInContent;
	
	public NotificationTabService() {
		super(
				"Notifications",
				"http://i48.tinypic.com/dlgi15.png");

		currentlyShownNotifications = new ArrayList<String>();
		stackIdsRemoved = new ArrayList<String>();

		VLayout layout = new VLayout();
		layout.setMembersMargin(10);

		// ====================Code used to show notifications in an accordian
		// manner====================
		HTMLFlow htmlFlow = new HTMLFlow();
		// htmlFlow.setOverflow(Overflow.AUTO);
		// htmlFlow.setPadding(10);

		// String contents = "<b>Severity 1</b><br> Vote for me!";

		// htmlFlow.setContents(contents);

		VLayout obamaNotifications = new VLayout();
		obamaNotifications.addMember(htmlFlow);

		HTMLFlow htmlFlow2 = new HTMLFlow();
		// htmlFlow2.setOverflow(Overflow.AUTO);
		// htmlFlow2.setPadding(10);

		// String contents2 =
		// "<b>New Friend</b><br> You are friends with Neil Ernst!";

		// htmlFlow2.setContents(contents2);

		HTMLFlow htmlFlow3 = new HTMLFlow();
		// htmlFlow3.setOverflow(Overflow.AUTO);
		// htmlFlow3.setPadding(10);

		// String contents3 = "<b>Away</b><br> I am away this week!";

		// htmlFlow3.setContents(contents3);

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

		// System.out.println(GWT.getHostPageBaseURL() + "world.png");
//		String title = Canvas
//				.imgHTML("http://www1.usaid.gov/images/icons/obama_icon.jpg")
//				+ " Obama";
		SectionStackSection section1 = new SectionStackSection();
		section1.setTitle("Notifications (" + currentlyShownNotifications.size() + ")");
		// section1.setItems(listGrid);
		// section1.setControls(addButton, removeButton);
//		section1.addItem(obamaNotifications);
		section1.setExpanded(true);
		section1.setCanCollapse(false);

		String title2 = Canvas
				.imgHTML("https://si0.twimg.com/profile_images/990437145/neil-cartoon_normal.jpg")
				+ " Neil Ernst";
		SectionStackSection section2 = new SectionStackSection(title2);
		// section2.setTitle("Neil Ernst");
		section2.addItem(ernstNotifications);
		// section2.addItem(htmlFlow3);
		// section2.setItems(statusReport);
		// section2.setControls(form);
		section2.setControls(removeButton);
		section2.setExpanded(true);

//		 sectionStack.setSections(section1);
		sectionStack.setShowEdges(true);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setAlign(Alignment.CENTER);
		sectionStack.setWidth("100%");
		sectionStack.setHeight("95%");
		sectionStack.setOverflow(Overflow.AUTO);

		layout.addMember(sectionStack);

		sectionStack.draw();
		// ====================Code used to show notifications in an accordian
		// manner====================

		// ====================Code used to show notifications to user with
		// animated 'fly onscreen'====================
		final Label label = new Label(
				"J. K. Rowling accepted your friend request! Yay.");
		label.setParentElement(layout);
		label.setShowEdges(true);
		label.setBackgroundColor("#ffffd0");
		label.setPadding(5);
		label.setWidth(200);
		label.setTop(50);
		label.setLeft(-220); // start off screen
		label.setValign(VerticalAlignment.CENTER);
		label.setAlign(Alignment.CENTER);
		label.setAnimateTime(1200); // milliseconds

		IButton moveInButton = new IButton();
		moveInButton.setTitle("Move In");
		moveInButton.setLeft(40);
		moveInButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				label.animateMove(10, 50);
			}
		});

		IButton moveOutButton = new IButton();
		moveOutButton.setTitle("Move Out");
		moveOutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				label.animateMove(-220, 50);
				sendOutInvites();

			}
		});
		
		setUpPopInNotification();

		HLayout hLayout = new HLayout();
		hLayout.setMembersMargin(10);
		
		// Code used here for notification testing purpose
		//hLayout.addMember(moveInButton);
		//hLayout.addMember(moveOutButton);
		layout.addMember(hLayout);

		layout.draw();

		

		this.setPane(layout);

		// Timer object used to poll server for new user notifications
		// every x seconds
		refreshTimer = new Timer() {
			public void run() {
					 updateCurrentUserNotifications();
			}
		};
		refreshTimer.scheduleRepeating(5000);
	}

	private static SectionStackSection produceNewNotification(String id,
			String fromUser, String eventName, String description,
			String startDate, String endDate, String type) {
		final String stackId = id;

		notificationContents = "<b>Event</b>: " + eventName + "<br><b>Description</b>: "
				+ description + "<br><b>Start: </b>" + startDate + "<br><b>End: </b>"
				+ endDate;

		notificationHtmlFlow = new HTMLFlow();
		// notificationHtmlFlow.setOverflow(Overflow.AUTO);
		// notificationHtmlFlow.setPadding(10);
		final String eventName2 = eventName;
		final String description2 = description;
		final Date startDate2 = new Date(startDate);
		final Date endDate2 = new Date(endDate);
		
		notificationHtmlFlow.setContents(notificationContents);
		notificationVLayout = new VLayout();
		notificationVLayout.setPadding(10);
		notificationVLayout.addMember(notificationHtmlFlow);
		HLayout hLayout = new HLayout();
		IButton cancelEventButton = new IButton("Ignore and Delete");
		cancelEventButton.setAutoFit(true);
		cancelEventButton.setPageLeft(10);
		cancelEventButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sectionStack.removeSection(stackId);
				notificationService.removeNotification(stackId,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								currentlyShownNotifications.remove(stackId);
								stackIdsRemoved.add(stackId);
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}
						});
			}
		});

		IButton addToCalendarButton = new IButton("Accept and Add to Calendar");
//		addToCalendarButton.setWidth(addToCalendarButton.getTitle().length() + 20);
		addToCalendarButton.setAutoFit(true);
		addToCalendarButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				String du;
				//System.out.println("duplicate before if statement is: "+ duplicate);
				calendarService.checkDuplicate(MainServices.account.getEmailAddress(),
						eventName2, description2, startDate2, endDate2, new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
								if(result.equals("0")){
									System.out.println("inside if statement");
								calendarService.saveEvent(
										MainServices.account.getEmailAddress(), eventName2,
										description2, startDate2, endDate2,
										new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												// TODO Auto-generated method stub

											}

											@Override
											public void onSuccess(Void result) {
												Window.alert("Notification Event Saved!");
												//TimeTableService timeTable = new TimeTableService();
												//timeTable.redrawCalendar();
												com.google.gwt.user.client.Window.Location.reload();

											}
										});
								
								}
								else{
								Window.alert("Time Table Conflict");
								}
								
							}});
				
			}
		});
		if (type.equalsIgnoreCase("invite"))
		{
			hLayout.addMember(addToCalendarButton);
			hLayout.addMember(cancelEventButton);
		}
			
//		hLayout.setPadding(10);
		notificationVLayout.addMember(hLayout);
		notificationSection = new SectionStackSection();
		
		String notificationTitle = "";
		String friendPicture = MainServices.getCurrentUserDisplayPicUrl();
		
		if (friendPicture.length() > 0)
		{
			notificationTitle = Canvas.imgHTML(friendPicture) + " ";
		}
		
		
		if (type.equalsIgnoreCase("add"))
		{
			notificationTitle += Canvas.imgHTML("http://i46.tinypic.com/1pt1e8.png") + " - " + MainServices.account.getEmailAddress();
		}
		else if (type.equalsIgnoreCase("remove"))
		{
			notificationTitle += Canvas.imgHTML("http://i48.tinypic.com/ekqjoh.png") + " " + MainServices.account.getEmailAddress();
		}
		else if (type.equalsIgnoreCase("modify"))
		{
			notificationTitle += Canvas.imgHTML("http://i47.tinypic.com/2wqe6af.png") + " " + MainServices.account.getEmailAddress();
		}
		else if (type.equalsIgnoreCase("invite"))
		{
			notificationTitle += Canvas.imgHTML("http://i48.tinypic.com/10hkriv.png") + " " + MainServices.account.getEmailAddress();
		}
		else
		{
			notificationTitle += MainServices.account.getEmailAddress();
		}
		
		notificationSection = new SectionStackSection(notificationTitle);
		
		System.out.println("The title of new notification is: " + notificationSection.getTitle());
		
		notificationSection.addItem(notificationVLayout);
		notificationSection.setExpanded(true);
		notificationSection.setID(stackId);

		removeButton = new ImgButton();
		removeButton.setSrc("[SKIN]actions/remove.png");
		removeButton.setSize(16);
		removeButton.setShowFocused(false);
		removeButton.setShowRollOver(false);
		removeButton.setShowDown(false);
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					sectionStack.removeSection(stackId);
					notificationService.removeNotification(stackId,
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub
									currentlyShownNotifications.remove(stackId);
									stackIdsRemoved.add(stackId);
								}

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
								}
							});
			}
		});

		notificationSection.setControls(removeButton);

		return notificationSection;
	}

	private void updateCurrentUserNotifications() {
		notificationService.getNotificationsByUsername(
				MainServices.account.getEmailAddress(),
				new AsyncCallback<List<ArrayList<Object>>>() {

					@Override
					public void onSuccess(List<ArrayList<Object>> result) {
						for (ArrayList<Object> notificationObj : result) {
							if (!currentlyShownNotifications
									.contains((String) notificationObj.get(0)) && !stackIdsRemoved.contains(notificationObj.get(0))) {
								sectionStack.addSection(produceNewNotification(
										(String) notificationObj.get(0),
										(String) notificationObj.get(5),
										(String) notificationObj.get(1),
										(String) notificationObj.get(2),
										(String) notificationObj.get(3),
										(String) notificationObj.get(4),
										(String) notificationObj.get(7)));
								currentlyShownNotifications
										.add((String) notificationObj.get(0));
								if (!isFirstRun)
								{
									numberOfNewNotifications++;
									refreshNotificationPopIn();
//									notificationPopInPanel.animateMove(10, 50);
									if(!notificationPopInPanel.isVisible())
									{
										notificationPopInPanel.redraw();
										notificationPopInPanel.animateShow(AnimationEffect.FLY);
									}
									else if (notificationPopInPanel.isVisible())
									{
										notificationPopInPanel.animateHide(AnimationEffect.FADE);
										notificationPopInPanel.redraw();
										notificationPopInPanel.animateShow(AnimationEffect.FADE);
									}
								}
							} 
						}
						if (isFirstRun)
						{
							isFirstRun = false;
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println(caught.getMessage());
					}
				});
	}

	public void sendOutInvites() {
		notificationService.addNotificationCalendar(
				MainServices.account.getEmailAddress(), "broadcast",
				"this is the description", "this is the name", new Date(),
				new Date(), "added", new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Calendar Event Invitation FAILED!");
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						System.out.println("Calendar Event Invitation Sent");
					}

				});
	}
	
	private void setUpPopInNotification()
	{
		popInContent = new HTMLFlow();
		popInContent.setContents("<center>You have <b>" + numberOfNewNotifications + "</b> new notifications!</center>");
//		popInContent.setLayoutAlign(VerticalAlignment.CENTER);
		rightTabLayout = MainServices.getRightTabLayout();
		notificationPopInPanel = new VLayout();
//		popInNotificationLabel = new com.smartgwt.client.widgets.Label();
//		popInNotificationLabel.addm
		
		
		
		System.out.println("New vlayout panel for pop in!");
		
		notificationPopInPanel.setShowEdges(true);
		notificationPopInPanel.setBackgroundColor("#ffffd0");
		notificationPopInPanel.setPadding(5);
		notificationPopInPanel.setHeight(100);
		notificationPopInPanel.setWidth(200);
		notificationPopInPanel.setTop(50);
//		notificationPopInPanel.setValign(VerticalAlignment.CENTER);
		notificationPopInPanel.setAlign(Alignment.CENTER);
		
//		notificationPopInPanel.setsho
		
//		notificationPopInPanel.addMember(popInNotificationLabel);
		notificationPopInPanel.addMember(popInContent);
		notificationPopInPanel.setParentElement(rightTabLayout);
		notificationPopInPanel.setTop(rightTabLayout.getHeight() - notificationPopInPanel.getHeight() - 20);	
		notificationPopInPanel.setLeft(rightTabLayout.getWidth() - notificationPopInPanel.getWidth() - 30);
		notificationPopInPanel.setAnimateTime(900); // milliseconds
		notificationPopInPanel.setVisible(false);
		
		notificationPopInPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				notificationPopInPanel.animateHide(AnimationEffect.FLY);
				numberOfNewNotifications = 0;
			}
		});
		
		rightTabLayout.redraw();
	}
	
	private void refreshNotificationPopIn()
	{
		popInContent.setContents("<center>You have <b>" + numberOfNewNotifications + "</b> new notifications!</center>");
	}

	

}
