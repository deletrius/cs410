package loclock.client;



import java.text.ParseException;
import com.google.gwt.i18n.client.DateTimeFormat; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.Label;

import com.smartgwt.client.widgets.calendar.CalendarEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.data.DSRequest;



public class FileUploadService extends Service{
	private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "upload";
	private VLayout fileUploadForm;
	private long dateTimeStart;
	private long dateTimeEnd;
	private TimeTableService timeTableService;
	final long WEEKS_IN_MILLIS = 1000 * 60 * 60 * 24*7;
	private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyyMMddHHmmss");
	//new SimpleDateFormat("yyyyMMddHHmmss");
	public FileUploadService()
	{
		super();
		this.setTitle("File Upload");
		fileUploadForm = new VLayout();	
		fileUploadForm.setSize("100%", "100%");
		//fileUploadForm.setNumCols(4);  		
		//fileUploadForm.setAutoFocus(false);


		Label lblUbcStudents = new Label("UBC Students:\r\nDownload your schedule from UBC courses as a .ics file and then upload it here.");


		final FormPanel form = new FormPanel();
		form.setAction(UPLOAD_ACTION_URL);

//		System.out.println(UPLOAD_ACTION_URL);

		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		//final TextBox tb = new TextBox();
		//tb.setName("textBoxFormElement");
		//panel.add(tb);

		//ListBox lb = new ListBox();
		//		lb.setName("listBoxFormElement");
		//		lb.addItem("foo", "fooValue");
		//		lb.addItem("bar", "barValue");
		//		lb.addItem("baz", "bazValue");
		//		panel.add(lb);

		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		panel.add(new Button("Submit Schedule", new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take this opportunity to perform validation.
				//				if (tb.getText().length() == 0) {
				//					Window.alert("The text box must not be empty");
				//					event.cancel();
				//				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response of
				// type
				// text/html, we can get the result text here (see the FormPanel
				// documentation for further explanation).

				//Window.alert("THE RESULT IS: " + event.getResults());
				//Window.alert(event.getResults().toString());
				String str = event.getResults().toString();
				ParserServiceAsync parser = GWT.create(ParserService.class);


				parser.parse(str,MainServices.account.getEmailAddress(),new AsyncCallback<List<ArrayList<Object>>>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Failed");
					}



					@Override
					public void onSuccess(List<ArrayList<Object>> result) {
						System.out.println("parserService succeed");
						CalendarEvent[] calEvents = TimeTableService.events;

						CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
						for(int i=0;i<result.size();i++){
							//dateTimeStart = (Long) Long.parseLong(result.get(i).get(2).toString());
							//dateTimeEnd = (Long) Long.parseLong(result.get(i).get(3).toString());

							String str1 = result.get(i).get(2).toString();
							String str2 = result.get(i).get(3).toString();
							String str3 = result.get(i).get(4).toString();
							//System.out.println("str3 is: "+ str3);
							Date d1 = new Date();
							Date d2 = new Date();
							Date dateTermEnd = new Date();
							d1 = dateTimeFormat.parse(str1);
							d2 = dateTimeFormat.parse(str2);
							dateTermEnd  = dateTimeFormat.parse(str3+"235959");

							int weekDiff =(int) ((dateTermEnd.getTime()-d1.getTime())/WEEKS_IN_MILLIS);

							//System.out.println("WEEKS_IN_MILLIS is:"+ weekDiff);

							for(int j=0; j<weekDiff+1;j++){

							boolean contains = false;
							//if(d1.before(dateTermEnd)){
							//System.out.println("Date end is: "+ dateTermEnd);
							//System.out.println("d1 is: " + d1.toString());
							if(!(d1.after(dateTermEnd))){
								//System.out.println("Date end is: "+ dateTermEnd);
								//System.out.println("d1 is: " + d1.toString());
								if(calEvents.length!=0){
									//System.out.println("Size of calEvent is: "+ calEvents.length);
								for(int k = 0; k<calEvents.length;k++){
									//check duplication if event consider to be duplicated if this event has same startDate, endDate, name, and description
									//else add the calendar
									//System.out.println(calEvents[k].getName());

									if(calEvents[k].getStartDate().equals(d1)){

										//System.out.println("different startDate");
										if(calEvents[k].getEndDate().equals(d2)){
											
											contains = true;
											
											//System.out.println("different endDate");
											if(calEvents[k].getName() == result.get(i).get(0).toString()){
												//System.out.println("same name");
												//System.out.println("different eventName");
											if(calEvents[k].getDescription() == result.get(i).get(1).toString()){
												//System.out.println("different description");



											}

											}
										}
									}
								}			
								}
								if(contains == false){
									System.out.println("contains == false");
									System.out.println("Event "+ result.get(i).get(2).toString());

									calendarService.saveEvent(MainServices.account.getEmailAddress(), result.get(i).get(0).toString(), result.get(i).get(1).toString(), d1,d2, new AsyncCallback<Void>(){

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											Window.alert("Upload Failed");
										}

										@Override
										public void onSuccess(Void result) {
											// TODO Auto-generated method stub
											Window.alert("Event Saved");
											//timeTableService.calendarDraw();



										}});
								}

						}
							CalendarUtil.addDaysToDate(d1, 7);
							CalendarUtil.addDaysToDate(d2, 7);


							}
							//TimeTableService.calendar.redraw();
						}


					}


				});
				TimeTableService.calendar.redraw();

}
		});
		//FormItem lol;
		//fileUploadForm.add(form);
		VerticalPanel newV=new VerticalPanel();
		newV.add(lblUbcStudents);
		newV.add(form);

		fileUploadForm.addChild(newV);
		//fileUploadForm.addChild(form);
		this.setPane(fileUploadForm);
	}

}