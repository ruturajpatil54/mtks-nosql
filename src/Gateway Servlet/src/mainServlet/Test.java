package mainServlet;


import java.util.*;
import java.io.*;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.client.HttpStoreClientFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.json.JSONException;
import org.json.JSONObject;

import query_Initiator.*;
import gatewayBuilder.*;



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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String q=request.getParameter("query");
		Conflation c=new Conflation();
		String inp=new String(q);
		q=new String(c.applyConflation(q));	// Apply conflation
		System.out.println(q);
		query_Initiator.KeyCombinator kc=new query_Initiator.KeyCombinator(q);
		
		
		Vector<KeyGroup> ListofGroups=new Vector<>(kc.combinator(Smax));
		// the KeyGroups obtained here
		PrintWriter out=response.getWriter();
		java.util.Date today=new java.util.Date();
		int totalURL=0;
		//generate result page
		out.println("<html>"+" <body>"+"<h2 align=center> You searched for: "+inp+"</h2>" + today + "<p>");
		//query voldemort for each keygroup
		Vector<KVPair> KVPList=queryVoldemort(ListofGroups,out);
		KVPList=sortList(KVPList);
		Iterator i=KVPList.iterator();
		while(i.hasNext())
		{
			KVPair kv=(KVPair)i.next();
			Iterator j=kv.URLlist.iterator();
			out.println("<br>For KG:"+ kv.KG.getTagList()+"<br>");
			out.println("<br>URL Count="+kv.URLCount+"<br>");
			totalURL+=kv.URLCount;
			while(j.hasNext())
				out.print("&nbsp"+j.next()+",<br>");
			out.println(";<br>");
		}
			
		out.println("</p>#URLs returned="+totalURL+"</body></html>");
	}
	public Vector<KVPair> sortList(Vector<KVPair> list)
	{
		//sort in descending S (no. of tags)
		Vector<KVPair> listOne=new Vector<>(),listTwo,listThree;
		System.out.println("Original>>>");
		for(int i=Smax;i>=1;i--)// for decreasing value of S
		{
			Iterator j=list.iterator();//add all KVPairs to newlist with S=i
			while(j.hasNext())		
			{
				KVPair current=(KVPair)j.next();
				if(current.KG.s==i)
				{
					listOne.add(current);
					System.out.println("s="+current.KG.s+" popularity="+current.PopularityValue+" URLCount="+current.URLCount);
				}
				
			}
			
		}
		Iterator tmp=listOne.iterator();
		System.out.println("listOne>>>");
		while(tmp.hasNext())
		{
			KVPair current=(KVPair)tmp.next();
			System.out.println("s="+current.KG.s+" popularity="+current.PopularityValue+" URLCount="+current.URLCount);
		}
		//sort in descending order of popularity for given S
		
		listTwo=new Vector<>();
		for(int i=Smax;i>=1;i--)// for decreasing value of S
		{
			int j=0;
			while((i!=listOne.get(j).KG.s)&&j<=listOne.size())
				j++;
			int k=j;
			while(listOne.get(j).KG.s>i-1)
				k++;
			for(;j<=k;j++)
			{
				
				KVPair current=(KVPair)listOne.get(j);
				if(j<listOne.size()-1)
				{
					KVPair next=(KVPair)listOne.get(j+1);
					if(next.KG.toDecimal(next.PopularityValue)>current.KG.toDecimal(current.PopularityValue))
					{
						listOne.remove(current);
						listOne.add(j+1,current);
					}
				}
				else
					break;
				
			}
		}
		Iterator temp=listTwo.iterator();
		System.out.println("listTwo>>>");
		while(temp.hasNext())
		{
			KVPair current=(KVPair)temp.next();
			System.out.println("s="+current.KG.s+" popularity="+current.PopularityValue+" URLCount="+current.URLCount);
		}
		
		
		return listOne;
	}
	public Vector<KVPair> queryVoldemort(Vector<KeyGroup> ListofGroups, PrintWriter out)	// this method fires all keygroups on Voldemort and
	{							// prints corresponding values
		String bootstrapUrl = "tcp://localhost:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
        StoreClient<String, String> client = factory.getStoreClient("test");
        
        String value;
        KeyGroup KG;
        Vector<KVPair> KVPList=new Vector<>();
        for(int i=0;i<ListofGroups.size();i++)
        {
        	KG=(KeyGroup)ListofGroups.elementAt(i);
			value=client.getValue(KG.getTagList(),"0");//default value="0" indicates key not present 
			if(value!="0")/* if Key is present................................*/
        	 {																 
				
				try																
        		{
					JSONObject json=new JSONObject(value);
					KG.suspended=json.getBoolean("suspended");
					KG.setPopularity(json.getString("popularity"));
					KG.requestKey();								// update key popularity since it is accessed
					String setofURL=json.getString("setofURL");
					if(KG.suspended!=true)// check suspended flag
					{											// if active create KVPair object & add to list
						
						//	create KVPair objects
						KVPList.add(new KVPair(KG,setofURL));
					}
					
													// upgrade to multithreading
					
					JSONObject newjs=new JSONObject();
					newjs.put("setofURL", setofURL);
					newjs.put("URLCount", json.get("URLCount"));
					newjs.put("popularity", KG.getPopularity());
					newjs.put("suspended", KG.suspended);
					value=new String(newjs.toString());
					client.put(KG.getTagList(), value);
    				
    			} catch (JSONException e) 
    			{
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
				//out.println(value);
        	 }
			else
			 {
				out.println("key:"+KG.getTagList()+" NOT present");
				ListofGroups.remove(i);
			 }
        }
        return KVPList;
	}
	

}


//////////




