package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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

	private static Timer refreshTimer;
	//private static Timer refreshTimer2;

	//private static String currentStackId;

	//private static int lock;

	//private static int refreshTimerIsRunning = 0;

	private static List<String> stackIdsRemoved;

	// private static final Domain DOMAIN =
	// DomainFactory.getDomain("my_domain");

	public NotificationTabService() {
		super(
				"Notifications",
				"http://cdn1.iconfinder.com/data/icons/Project_Icons___Version_1_1_9_by_bogo_d/PNG/Notification.png");

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
		String title = Canvas
				.imgHTML("http://www1.usaid.gov/images/icons/obama_icon.jpg")
				+ " Obama";
		SectionStackSection section1 = new SectionStackSection(title);
		// section1.setTitle("Obama");
		// section1.setItems(listGrid);
		// section1.setControls(addButton, removeButton);
		section1.addItem(obamaNotifications);
		section1.setExpanded(true);

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

		// sectionStack.setSections(section1, section2);
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

				notificationService.addNotification(
						MainServices.account.getEmailAddress(),
						MainServices.account.getEmailAddress(), "I see you.",
						"New friend", new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								System.out
										.println("Successful notification added.");
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out
										.println("Failed failed failed notification added.");
								System.out.println(caught.getMessage());
							}
						});

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

		HLayout hLayout = new HLayout();
		hLayout.setMembersMargin(10);
		hLayout.addMember(moveInButton);
		hLayout.addMember(moveOutButton);
		layout.addMember(hLayout);

		layout.draw();

		// ====================Code used to show notifications to user with
		// animated 'fly onscreen'====================

		this.setPane(layout);

		// Timer object used to poll server for new user notifications
		// every x seconds
		refreshTimer = new Timer() {
			public void run() {
//				 if (lock == 0)
//				 {
//					 lock = 1;
					 updateCurrentUserNotifications();
//				 }
//				 else
//				 {
//					 // do nothing and wait for next refresh timer call
//				 }
			}
		};
		refreshTimer.scheduleRepeating(5000);
	}

	private static SectionStackSection produceNewNotification(String id,
			String fromUser, String eventName, String description,
			String startDate, String endDate) {
		final String stackId = id;

		notificationContents = "Event Name: " + eventName + "<br>Description: "
				+ description + "<br>StartTime:" + startDate + "<br>EndTime: "
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
		notificationVLayout.addMember(notificationHtmlFlow);
		HLayout hLayout = new HLayout();
		IButton cancelEventButton = new IButton("Cancel Event");
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

		IButton addToCalendarButton = new IButton("Add to Calendar");
		addToCalendarButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
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

							}
						});
			}
		});
		hLayout.addMember(addToCalendarButton);
		hLayout.addMember(cancelEventButton);
		notificationVLayout.addMember(hLayout);
		notificationSection = new SectionStackSection(
				MainServices.account.getEmailAddress());
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
										(String) notificationObj.get(4)));
								currentlyShownNotifications
										.add((String) notificationObj.get(0));
							} else {
							}
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
				new Date(), new AsyncCallback<Void>() {

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
}