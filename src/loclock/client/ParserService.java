package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parserServlet")
public interface ParserService extends RemoteService {
	
	public List<ArrayList<Object>> parse(String str, String userName);
}
