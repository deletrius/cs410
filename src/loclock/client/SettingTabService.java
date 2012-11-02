package loclock.client;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class SettingTabService extends Service{

	public SettingTabService()
	{
		super("Settings", "http://icons.iconarchive.com/icons/walrick/openphone/48/Settings-icon.png");
		
		VLayout layout = new VLayout();  
        layout.setMembersMargin(10); 
        
        HLayout horizLayout = new HLayout();
        
        HTMLFlow htmlFlow = new HTMLFlow();  
        //htmlFlow.setOverflow(Overflow.AUTO);  
        htmlFlow.setPadding(10);  
  
        String contents = "<b>Privacy Setting</b><br> Share my location with everyone";  
  
        htmlFlow.setContents(contents); 
        horizLayout.addMember(htmlFlow);
        
        Image upImage = new Image("http://www.veryicon.com/icon/preview/Application/3D%20Cartoon%20Icons%20Pack%20III/Windows%20Turn%20Off%20Icon.jpg");
        Image downImage = new Image("http://www.deeptrawl.com/help/images/on.png");
        ToggleButton normalToggleButton = new ToggleButton(upImage, downImage);
        normalToggleButton.ensureDebugId("cwCustomButton-toggle-normal");
        
        horizLayout.addMember(normalToggleButton);
        
        layout.addMember(horizLayout);
        layout.draw();  
        
        this.setPane(layout);
	}
}
