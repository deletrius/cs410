package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

public class SmarttyButlerTabService extends Service
{
	private SubscriptionServiceAsync subscriptionService=GWT.create(SubscriptionService.class);
	
	public SmarttyButlerTabService()
	{
		super("Smartty Butler", "http://i48.tinypic.com/dlgi15.png");
		
		// Define the oracle that finds suggestions
	    MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
//	    String[] words = constants.cwSuggestBoxWords();
	    List<String> friendEmails = new ArrayList<String>();
	    
	    
	    
//	    for (int i = 0; i < words.length; ++i) {
//	      oracle.add(words[i]);
//	    }
//
//	    // Create the suggest box
//	    final SuggestBox suggestBox = new SuggestBox(oracle);
//	    suggestBox.ensureDebugId("cwSuggestBox");
//	    VerticalPanel suggestPanel = new VerticalPanel();
//	    suggestPanel.add(new HTML(constants.cwSuggestBoxLabel()));
//	    suggestPanel.add(suggestBox);
	}

}
