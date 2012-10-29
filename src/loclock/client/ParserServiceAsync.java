package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParserServiceAsync {
	void  parse(String str, String userName,AsyncCallback<List<ArrayList<Object>>> async);
}
