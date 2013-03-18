package gatewayBuilder;
/**
 * Author: Ruturaj Patil
 */
import query_Initiator.*;
import java.util.*;
public class KVPair 
{
	KeyGroup KG;
	ArrayList<String> URLlist=new ArrayList<>();
	int URLCount;
	String PopularityValue;
	public KVPair(KeyGroup K,String URLs)
	{
		KG=new KeyGroup(K);
		URLs=URLs.replaceAll("\\[",",");
		URLs=URLs.replaceAll("\\]",",");
		String URL[]=URLs.split(",");
		for(int i=0;i<URL.length;i++)
		{
			if(URL[i].isEmpty()==false)
			{
				while(URL[i].charAt(0)==' ')
					URL[i]=URL[i].trim();
				URLlist.add(URL[i]);
			}
				
		}
		URLCount=URLlist.size();
		PopularityValue=KG.getPopularity();
		
		
	}
	public void addURL(String url)
	{
		while(url.charAt(0)==' ')
		url=url.trim();
		url=url.replaceAll(" ", "_");
		if(url.isEmpty()==false)
		{
			URLlist.add(url);
			URLCount++;
		}
		
	}
	
	
	

}
