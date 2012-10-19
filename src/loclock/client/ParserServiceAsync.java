package loclock.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParserServiceAsync {
	void  parse(String str, AsyncCallback<ArrayList<ArrayList<String>>> async);
}
