package loclock.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class SettingTabService extends Service{

	private final UserLocationServiceAsync userLocationService = GWT.create(UserLocationService.class);
//	private HTMLFlow htmlFlow2;
	private String publicSentence = "Your profile is <u>public</u>. Other users of this website besides your friends can discover you and see your location on the map when nearby.";  
    private String privateSentence = "Your profile is <u>private</u>. <b>Only</b> your friends can see your location on the map.";
	
  private HTMLFlow htmlFlow2 = new HTMLFlow();  
 
    
    public SettingTabService()
	{
		super("Settings", "http://i46.tinypic.com/10x5wg4.png");
		
		//final DynamicForm form = new DynamicForm();  
//        form.setWidth(250);  
        
//        Page.setAppImgDir("[APP]/loclock/images/");
        
		final VLayout layout = new VLayout();  
        layout.setMembersMargin(10); 
        
        final HLayout horizLayout = new HLayout();
        
        htmlFlow2 = new HTMLFlow();  
        
        htmlFlow2.setOverflow(Overflow.AUTO);  
        htmlFlow2.setPadding(10);
  
          
  
        
        HTMLFlow htmlFlow = new HTMLFlow();  
        //htmlFlow.setOverflow(Overflow.AUTO);  
        htmlFlow.setPadding(10);  
  
        String contents = "<b>Privacy Setting</b><br> Decide how you would like to share your location.";  
  
        htmlFlow.setContents(contents); 
//        htmlFlow2.setContents("");
        horizLayout.addMember(htmlFlow);
        
        final ListBox dropBox = new ListBox(false);
        dropBox.addItem("Private", "private");
        dropBox.addItem("Public", "public");
        
        
        
        ComboBoxItem cbItem = new ComboBoxItem();  
        cbItem.setTitle("Select");  
        cbItem.setHint("<nobr>A simple ComboBoxItem</nobr>");  
        cbItem.setType("comboBox");  
        cbItem.setValueMap("Private", "Public");
        
        SelectItem selectItem = new SelectItem();  
        selectItem.setTitle("Select");  
        selectItem.setHint("<nobr>A SelectItem with icons</nobr>");  
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  
        valueMap.put("US", "<b>United States</b>");  
        valueMap.put("CH", "China");  
//        valueMap.put("JA", "<b>Japan</b>");  
//        valueMap.put("IN", "India");  
//        valueMap.put("GM", "Germany");  
//        valueMap.put("FR", "France");  
//        valueMap.put("IT", "Italy");  
//        valueMap.put("RS", "Russia");  
//        valueMap.put("BR", "<b>Brazil</b>");  
//        valueMap.put("CA", "Canada");  
//        valueMap.put("MX", "Mexico");  
//        valueMap.put("SP", "Spain");  
        selectItem.setValueMap(valueMap);  
        //selectItem.setImageURLPrefix("flags/16/");  
//        selectItem.setImageURLSuffix(".png"); 
        
        LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();  
        valueIcons.put("US", "http://icons.iconarchive.com/icons/custom-icon-design/flag-2/16/China-Flag-icon.png");  
        valueIcons.put("CH", "http://icons.iconarchive.com/icons/custom-icon-design/flag-2/16/China-Flag-icon.png");  
//        valueIcons.put("JA", "JA");  
//        valueIcons.put("IN", "IN");  
//        valueIcons.put("GM", "GM");  
//        valueIcons.put("FR", "FR");  
//        valueIcons.put("IT", "IT");  
//        valueIcons.put("RS", "RS");  
//        valueIcons.put("BR", "BR");  
//        valueIcons.put("CA", "CA");  
//        valueIcons.put("MX", "MX");  
//        valueIcons.put("SP", "SP");  
        selectItem.setValueIcons(valueIcons);
        
//        selectItem.setse
        
        Image upImage = new Image("http://www.veryicon.com/icon/preview/Application/3D%20Cartoon%20Icons%20Pack%20III/Windows%20Turn%20Off%20Icon.jpg");
        Image downImage = new Image("http://www.deeptrawl.com/help/images/on.png");
        ToggleButton normalToggleButton = new ToggleButton(upImage, downImage);
        normalToggleButton.ensureDebugId("cwCustomButton-toggle-normal");
        
//        form.setFields(cbItem, selectItem);
//        form.draw();
//        horizLayout.addMember(normalToggleButton);
        //horizLayout.addMember(form);
        
        horizLayout.addMember(dropBox);
        horizLayout.addMember(htmlFlow2);
        
        layout.addMember(horizLayout);
        //layout.addMember(htmlFlow2);
        layout.draw();  
        
        this.setPane(layout);
        
        userLocationService.getUsersAsArrayList(MainServices.account.getEmailAddress(), new AsyncCallback<List<ArrayList<Object>>>() {

        	@Override
        	public void onSuccess(List<ArrayList<Object>> result) {
        		// TODO Auto-generated method stub
        		String privacy = (String) result.get(0).get(3);
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
        					// TODO Auto-generated method stub
        					System.out.println("fail changing user");
        				}
        			});
        		}
        	}
        });
	}
}
