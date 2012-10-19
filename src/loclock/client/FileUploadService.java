package loclock.client;

import java.util.ArrayList;

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
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.Label;

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
				Window.alert(event.getResults().toString());
				String str = event.getResults().toString();
				ParserServiceAsync parser = GWT.create(ParserService.class);
				
				
				parser.parse(str,new AsyncCallback<ArrayList<ArrayList<String>>>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("Failed");
					}

					@Override
					public void onSuccess(ArrayList<ArrayList<String>> result) {
						// TODO Auto-generated method stub
						
						//System.out.println(event.getResults().toString());
					}});
				
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
