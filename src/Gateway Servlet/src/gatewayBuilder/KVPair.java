package gatewayBuilder;
/**
 * Author: Ruturaj Patil
 * Class to hold objects like [key1,key2...keyN]->{[url1,url2,...urlN][url_count][popularity][suspended]}
 */
import query_Initiator.*;
import java.util.*;
public class KVPair 
{
	public KeyGroup KG;
	public HashSet<String> URLlist=new HashSet<>();
	public int URLCount;
	public String PopularityValue;
	public KVPair(KeyGroup K,String URLs)
	{
		KG=new KeyGroup(K);
		URLs=URLs.replaceAll("\\[",",");
		URLs=URLs.replaceAll("\\]",",");
		String URL[]=URLs.split(",");
		for(int i=0;i<URL.length;i++)
		{
			if(URL[i].length()>0)
			{
				try{
				while(URL[i].charAt(0)==' ')
					URL[i]=URL[i].trim();
				URLlist.add(URL[i]);
				}
				catch( java.lang.StringIndexOutOfBoundsException s)
				{
					System.out.println(URL[i]);
				}
			}
				
		}
		URLCount=URLlist.size();
		PopularityValue=KG.getPopularity();
		
		
	}
	public void addURL(String url)
	{
		while(url.charAt(0)==' ')
		url=url.trim();
		
		if(url.isEmpty()==false)
		{
			URLlist.add(url);
			URLCount++;
		}
		
	}
	
	
	

}

