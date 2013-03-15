package query_Initiator;


import java.util.*;
import java.io.*;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;
import voldemort.client.protocol.RequestFormatType;
import voldemort.store.bdb.BdbStorageConfiguration;
import voldemort.store.memory.CacheStorageConfiguration;
import voldemort.store.memory.InMemoryStorageConfiguration;
import voldemort.store.mysql.MysqlStorageConfiguration;
import voldemort.store.readonly.ReadOnlyStorageConfiguration;
import voldemort.utils.ConfigurationException;
import voldemort.utils.Props;
import voldemort.utils.Time;
import voldemort.utils.UndefinedPropertyException;
import voldemort.utils.Utils;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;



/*
 * Author : Ruturaj Patil
 * This is a test servlet that instantiates a voldemort server
 * It then responds to HTTP GET requests by:
 * 1) creating key combinations
 * 2) Fire each keygroup on voldemort
 * 3) Print retrieved values (URLs) for now 
 * Note : Smax is defined here
 */

@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int Smax=3;

   /* Test()
    {
    	/*
    	VoldemortConfig config = VoldemortConfig.loadFromEnvironmentVariable();
    	config.setVoldemortHome("/media/Data/BE Project/voldemort-0.96/config/single_node_cluster");
    	VoldemortServer server = new VoldemortServer(config);
    	server.start();
    	
    	
    	

    }*/
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String q=request.getParameter("query");
		//testVoldemort();
		query_Initiator.KeyCombinator test=new query_Initiator.KeyCombinator(q);
		////temp initialization of store keys for test
		
		Vector<KeyGroup> ListofGroups=new Vector<>(test.combinator(Smax));
		
		PrintWriter out=response.getWriter();
		java.util.Date today=new java.util.Date();
		out.println("<html>"+" <body>"+"<h1 align=center> You searched for: "+q+"</h1>" + today + "<p>");
		queryVoldemort(ListofGroups,out);
		out.println("</p></body></html>");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void queryVoldemort(Vector<KeyGroup> ListofGroups, PrintWriter out)	// this method fires all keygroups on Voldemort and
	{							// prints corresponding values
		String bootstrapUrl = "tcp://localhost:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
        StoreClient<String, String> client = factory.getStoreClient("test");
        
        String value;
        for(int i=0;i<ListofGroups.size();i++)
        {
			value=client.getValue(ListofGroups.elementAt(i).getTagList(),"URL"+i);//temp default value= URLi
			if(value!="0")														// These are NOT present in DB
        	 out.println(value);
			else
			 out.println("key:"+ListofGroups.elementAt(i).getTagList()+" NOT present");	
        }
	}

}


//////////




