package gatewayBuilder;
/**
 * @Author: Ruturaj Patil
 * This class builds inverted index for gateway node by
 * 1. Retrieving all URLs and their attributes from backend node
 * 2. Get all words from <title> of URL and conflate them
 * 3. Generate combinations
 * 4. Add URL to existing tag combinations
 * 5. Create new keys with required tag lists and add URL as value
 */
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

import java.util.*;
import query_Initiator.*;
import org.json.JSONException;


import org.json.JSONObject;


import voldemort.utils.ByteArray;
import java.util.concurrent.TimeUnit;


import voldemort.client.protocol.admin.*;
import voldemort.client.protocol.admin.filter.DefaultVoldemortFilter;

public class GatewayBuilder 
{
	Vector<String> URLkeys=new Vector<>();// array of URL strings
	Vector<URL> URLset=new Vector<>();	//	collection of URL objects
	
	 StoreClient<String, String> client;
	public class URL
	{
		String URL,title;
		URL(String u,String t)
		{
			URL=u;
			title=t;
		}
		public String toString()
		{
			return "["+URL+"]";
		}
	}
	public class URLset
	{
		
		
	}
	GatewayBuilder()// fetch all keys from store(backend)
	{
		//connect to voldemort store as AdminClient to fetch all keys
		String bootStrapUrl = "tcp://localhost:6666";
	    String storeName = "test";

	    int maxThreads = 300;
	    ClientConfig clientConfig = new ClientConfig();
	    clientConfig.setMaxThreads(maxThreads);
	    clientConfig.setMaxConnectionsPerNode(maxThreads);
	    clientConfig.setConnectionTimeout(500, TimeUnit.MILLISECONDS);
	    clientConfig.setBootstrapUrls(bootStrapUrl);

	    StoreClientFactory factory = new SocketStoreClientFactory(clientConfig);
	   client= factory.getStoreClient(storeName);

	    int nodeId = 0;
	    List<Integer> partitionList = new ArrayList<Integer>();
	    partitionList.add(0);
	    partitionList.add(1);
	    AdminClient adminClient = new AdminClient(bootStrapUrl, new AdminClientConfig());
	    
	    Iterator<ByteArray> iterator = adminClient.fetchKeys(nodeId, storeName, partitionList,new DefaultVoldemortFilter(), false);

	    /*String bootstrapUrl = "tcp://localhost:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
        StoreClient<String, String> client = factory.getStoreClient("test");*/
        
	    while(iterator.hasNext())
	    {
	    	URLkeys.add(new String(iterator.next().get()));
	    }
	    
	    
	}
	
	public static void main(String[] args) 
	{
		GatewayBuilder gb=new GatewayBuilder();
		int totalURLs=gb.getTitles();
		//conflate title
		gb.conflateTitles();
		// insert title and URLs into store
		gb.insertRows();
			
		

	}
	public void insertRows()
	{
		for(int i=0;i<URLset.size();i++)
		{
			URL current=URLset.elementAt(i);
			KeyCombinator KC=new KeyCombinator(current.title);
			Vector<KeyGroup> KeyGroupList=KC.combinator(3);//Actually Smax needs to be fetched Test.getSmax()
			updateStore(KeyGroupList,current);
		}
	}
	int getTitles()
	{
		
		//connect to voldemort store as client
		/*String bootstrapUrl = "tcp://localhost:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
        StoreClient<String, String> client = factory.getStoreClient("test");
        */
        String value,title=new String();
        int from,to;
        for(int i=0;i<URLkeys.size();i++)
        {
        	value=client.getValue(URLkeys.elementAt(i));
        	//code to be upgraded to use JSON
        	/*	from=value.indexOf("\"title\":");
        		from+=9;
        		to=value.lastIndexOf('\"');
        		if(from>1&&to>1)
        		{
        			title=value.substring(from,to);
        			//System.out.println(title);
        	*/
        	if(value.contains("title\":\""))
        	{
        		try
        		{
    				JSONObject json=new JSONObject(value);
    				title=json.getString("title");
    			} catch (JSONException e) 
    			{
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	
        	//create URL objects
        	URLset.add(new URL(URLkeys.elementAt(i),title));
        		
        		
        	
        }
		return URLset.size();
	}
	public void conflateTitles()
	{
		WordConflation w=new WordConflation();
		for(int i=0;i<URLset.size();i++)//gb.URLset.size()
		{
			URL current=URLset.elementAt(i);
			current.title=current.title.replaceAll("/", " ");
			current.title=current.title.replaceAll("_", " ");
			current.title=current.title.trim();
			String words[]=current.title.split(" ");
			//System.out.println("title: "+current.title);
			for(int j=0;j<words.length;j++)
			{
				
				//System.out.println("\nBefore: "+words[j]);
				words[j]=w.stripAffixes(words[j]);
				//System.out.println("After: "+words[j]);
				if(words[j].isEmpty()==false)
					if(j==0)
						current.title=words[j];
					else
						current.title+=words[j]+" ";
			}
			//System.out.println("Conflated title: "+current.title);
			URLset.remove(i);// replace with conflated title
			URLset.add(i,current);
		}
	}
	public  void updateStore(Vector<KeyGroup> KGList,URL current)// overloaded method that checks if KG is present	
	{
		
        
        String value;
        for(int i=0;i<KGList.size();i++)
        {
        	KeyGroup KG=(KeyGroup)KGList.get(i);
			value=client.getValue(KG.getTagList(),"URL");//temp default value= URLi
			if(value!="URL")														// These are NOT present in DB
        	 {
				System.out.println(value);
				String setofURL=new String();
				
				try 
				{
					JSONObject js=new JSONObject(value);
					setofURL = js.getString("setofURL");
					//System.out.println("Aadhi:"+setofURL);
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				KVPair kv=new KVPair(KG,setofURL);
				kv.addURL(current.URL);
				try 
				{
					JSONObject js=new JSONObject(value);
					js.put("setofURL", kv.URLlist.toString());
					js.put("URLCount", kv.URLCount);
					js.put("popularity", kv.PopularityValue);
					js.put("suspended", kv.KG.suspended);
					value=new String(js.toString());
					System.out.println("for title: "+current.title+" Updated to :"+value);
		   	 	}
				catch(JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.put(KG.getTagList(), value);// put into Voldemort
        	 }
			else
			{
				System.out.println("key:"+KG.getTagList()+" NOT present");
				value=new String();
				JSONObject js=new JSONObject();
				KVPair kv=new KVPair(KG,current.URL);
				try {
					js.put("setofURL", kv.URLlist.toString());
					js.put("URLCount", kv.URLCount);
					js.put("popularity", kv.PopularityValue);
					js.put("suspended", kv.KG.suspended);
					value=new String(js.toString());
					System.out.println("Put new :"+value);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.put(KG.getTagList(), value);// put into voldemort
				
		//		js.put(key, value)
				//System.out.println("toObject: "+new String(Row.toBytes(KG)));
				
				
				
			}
			
        }
        
       
	}
	/*
	
	public String toString(Vector<URL> u)
	{
		String str=new String("[");
		for(int i=0;i<u.size();i++)
		{
			str+=u.elementAt(i)+",";
		}
		return str.replace(str.charAt(str.lastIndexOf(',')), ']');
		
		
	}
	*/
}


