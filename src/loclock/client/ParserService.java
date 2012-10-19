package loclock.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parserServlet")
public interface ParserService extends RemoteService {
	
	public ArrayList<ArrayList<String>> parse(String str);
}
