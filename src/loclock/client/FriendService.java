package loclock.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Button;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
//import com.smartgwt.client.widgets.drawing.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class FriendService extends Service{
	private ChatPanelManager chatManager;
	private VLayout friendsPanel;
	private DynamicForm requestForm;
	private String user;
	private LocationServiceAsync locationService = GWT.create(LocationService.class);
	
	TextAreaItem searchBox = new TextAreaItem();
	ButtonItem searchButton = new ButtonItem("Search");
	
//	private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
//			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
	public FriendService(String user)
	{
		this.setTitle("Friends");
		this.setIcon("http://www.fabgb.com/images/friends_icon.jpg");
		this.user=user;
		friendsPanel=new VLayout();
		friendsPanel.setSize("100%", "100%");
		chatManager=new ChatPanelManager(user);
		
		buildFriendList();
		buildRequest();
		friendsPanel.addMember(chatManager);
		this.setPane(friendsPanel);
		
	}
	
	public void buildFriendList(){
		TileGrid tileGrid = new TileGrid();  
		tileGrid.setWidth("100%");  
		tileGrid.setHeight("70%");
		tileGrid.setTileHeight(150);
		tileGrid.setTileWidth(100);
		tileGrid.setCanReorderTiles(true);  
		tileGrid.setShowAllRecords(true); 
		//  Record rec = new StudentRecord("name",picture,"Profile");
		Record[] record = new StudentRecord[]{new StudentRecord("ubc Student", "https://dotabuff.com/assets/heroes/drow-ranger-757bb2a5ae36ee4f138803062ac9a1d2.png","Profile")};
		tileGrid.setData(record); 

		DetailViewerField pictureField = new DetailViewerField("picture"); 

		pictureField.setType("image");  
		pictureField.setImageWidth(100);  
		pictureField.setImageHeight(100);  
		//pictureField.setImageURLPrefix("war/images/");
		DetailViewerField nameField = new DetailViewerField("name");  
		tileGrid.setFields(pictureField, nameField);  
		tileGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler(){

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				
				String to=event.getRecord().getAttribute("name").toString();
				//TODO chatManager.openChat(user,to);
				chatManager.openChat(user,"yunyuntester@gmail.com");
			}});
		tileGrid.draw(); 
		friendsPanel.addMember(tileGrid);
	} 
	
	public void buildRequest(){
		requestForm=new DynamicForm();
		requestForm.setSize("100%", "20%");
		requestForm.setItems(searchBox,searchButton);
		//friendsPanel.addMember(searchButton);
		final Label label0 = new Label();
		final Label label = new Label();
		
		searchButton.addClickHandler(new ClickHandler(){
		
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			
				String userName = searchBox.getValueAsString();
				locationService.getUserByID(userName, new AsyncCallback<String>(){

					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(String result) {
						label0.setText("The Searching Result: ");
						if(result==null)
							Window.alert("Player does not exist in ");
						else
							System.out.println(result);
							//label.setText(result);
					}
					
					});
			}
			
		});
		friendsPanel.addMember(requestForm);
	}

//	public void buildFriends() {
//
//		// Create a cell to render each value in the list.
//		TextCell textCell = new TextCell();
//
//		// Create a CellList that uses the cell.
//		CellList<String> cellList = new CellList<String>(textCell);
//
//		cellList.addStyleName("cellList");
//
//		// Set the total row count.
//		cellList.setRowCount(DAYS.size(), true);
//
//		final SingleSelectionModel<String> singleSelectionModel=new SingleSelectionModel<String>();
//
//		singleSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			public void onSelectionChange(SelectionChangeEvent event) {
//
//				String selected = singleSelectionModel.getSelectedObject();
//				if (selected != null) {
//					// @@@ TODO @@@ profile window update
//					Window.alert("You selected: " + singleSelectionModel.getSelectedObject().toString());
//				}
//			}
//		});
//		cellList.setSelectionModel(singleSelectionModel);
//
//		cellList.addDomHandler(new DoubleClickHandler(){
//			@Override
//			public void onDoubleClick(DoubleClickEvent event) {
//
//				// @@@ TODO @@@ chat window open!
//				Window.alert("You selected: lol " + singleSelectionModel.getSelectedObject().toString());
//
//			}},DoubleClickEvent.getType());
//
//		cellList.addDomHandler(new MouseOverHandler(){
//			@Override
//			public void onMouseOver(MouseOverEvent event) {
//				Timer t = new Timer() {
//					public void run() {
//						Window.alert("Hello");	
//					}
//				};
//				t.schedule(2000);
//
//			}},MouseOverEvent.getType());
//
//		// Push the data into the widget.
//		cellList.setRowData(0, DAYS);
//
//		// Add it to the root panel.
//		friendsPanel.add(cellList);
//
//	}
}
