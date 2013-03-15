/**
 * Author : Ruturaj Patil
 * class KeyCombinator receives string of the form "key1 key2 ..keyN" and value of Smax
 * It separates and sorts these keys. And then creates all possible combinations of keys
 * with Smax as the upper limit for no. of keys in each combination
 */
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class KeyCombinator 
{
	
	public String KeyStrings[];//keys contains all the keys in string format; 
	public Vector<Key> Keys=new Vector<>();
	public Vector<KeyGroup> ListofGroups=new Vector<>();//keygroups contains all the combinations of keys
	//public KeyGroup KG;//temp KeyGroup to be added to ListGroups
	public int Nkeys, Nkeygroups,flag;// denotes number of keys. Made public in case required outside this class
		
	public KeyCombinator(String query)
	// Constructor splits query into keys or individual words
	{
		
		query=query.trim();	//...........................remove leading & trailing whitespace
		KeyStrings=query.split(" ");
		System.out.println("Generating combinations...");
		System.out.println("#Words:"+KeyStrings.length);
		Nkeys=KeyStrings.length;		//.......................Update Nkeys
		for(int i=0;i<Nkeys;i++)
			KeyStrings[i]=KeyStrings[i].toLowerCase();//.........Convert each key to lower case
		Arrays.sort(KeyStrings);
		//Generate Key Objects
		Key t;
		for(int i=0;i<Nkeys;i++)
			{
				t=new Key(KeyStrings[i]);
				if(Keys.contains(t)==false)//....................Discard duplicates
					Keys.addElement(t);
			}
		
		
		
		
	}
	public Vector<KeyGroup> combinator(int Smax)
	{
		
		//Initiate list of KeyGroup containing only 1 key in each group
		for(int i=0;i<Keys.size();i++)
		{
			KeyGroup KG=new KeyGroup(Keys.elementAt(i));
			ListofGroups.addElement(KG);
		}
		int FROM=0;
		int TO=ListofGroups.size();
	
		//Generate combinations of width i <= Smax on every iteration
				for(int i=2;i<=Smax;i++)
				{
					//flag=0;//flag is set iff new group is added
					for(int j=FROM,l=1;j<TO;j++,l++)//for all NEW KeyGroups
					{
						for(int k=l;k<Keys.size();k++)
						{
							KeyGroup KG=new KeyGroup(ListofGroups.get(j));//add Key(k) to the current group
							if(KG.addKey((Keys.elementAt(k)), i)==0)//if new Key was added to the group
							{
								if(repeatedKG(KG)==false)
								{
									
									ListofGroups.addElement(KG);			//add keyroup to the listofGroups
									//System.out.println("added new group:"+KG.getValue());
									//flag=1;
									
									
									//System.out.println(".");
									
								}
								/*else
									System.out.println("KG already present");*/
								
							}
							/*else
							 System.out.println("KG not added");*/
							
						}	
						if (l>=Keys.size())
							l=0;
					}
					FROM=TO;
					TO=ListofGroups.size();
					//if(flag==1)
					//{
						
					//}
					
					
				}
				
		//print ListofGroups
		/*for(int i=0;i<ListofGroups.size();i++)
			//System.out.println(((KeyGroup)ListofGroups.elementAt(i)).printValue());
			System.out.println(ListofGroups.elementAt(i).getTagList());
		*/	
		/*VoldemortClient KVpair = new VoldemortClient();
		KVpair.execQuery("hello");*/
		
		System.out.println("#KeyGroups:"+ListofGroups.size());
		
		
		/*// OLD LOGIC
		 * int width,i,j,k,round_count=Nkeys,beginat=0,nextkey;
		 
		width=1;//no. of keys in combination e.g. for k1,k2 width = 2
		i=j=0;//counters
		String test;
		
		if (Smax > Nkeys)	// if No. of keys itself is less than Smax then reset Smax=Nkeys 
			Smax=Nkeys;
		
			while (width <= Smax)
			{
				if(keygroups.isEmpty()==true)
				{
					// 	generate combination of two keys
					for(i=0;i<round_count;i++)
						for(j=i+1;j<Nkeys;j++)
							keygroups.add((keys[i].concat(",")).concat(keys[j]));
					
					round_count=keygroups.size();
				}
				else
				//	generate combinations of more than two keys
				{
					/*on every iteration
					 * append keys from j to Nkeys to keygroups[i]
					 * for ex. keys: k1 k2 k3 k4 k5..k(Nkeys)
					 * on ith iteration:
					 * append key kj to keygroup: k1,k2,k3..ki-1
					 * => keygroup[i]=k1,k2,k3..ki-1 + kj
					 * 
					 * Here round_count is no. of iterations that equals no. of keygroups added 
					 * in each round; For first round it is Nkeys
					 *
					System.out.println("Else case");
					k=width;
					nextkey=width;
					for(i=beginat;i<round_count;i++)
					{
						
						boolean flag=false;///glag changes when innerloop executes
						for(j=nextkey;j<Nkeys;j++)
						{
							test=keygroups.get(i);
							if(test.contains(keys[j])==false)
							{
								keygroups.add(test+","+keys[j]);
								flag=true;
							}
						}
						if(flag==true)
							nextkey++;
						if(nextkey>5)
							nextkey=++k;
					}
					beginat=round_count;
					round_count=keygroups.size();
					
				}
				
				width++;
				
			}
			System.out.println("Smax="+Smax);
			System.out.println("#Keygroups="+keygroups.size());
			System.out.println("Keygroup list-");
			for(i=0;i<keygroups.size();i++)
				System.out.println("{"+keygroups.get(i)+"}");
		*/
		return ListofGroups;
	}
	
	
	/*public static void main(String[] args)
	{
		KeyCombinator test=new KeyCombinator(args[0]);
		test.combinator(Integer.parseInt(args[1]));
		
	}*/
	public boolean repeatedKG(KeyGroup KG)
	{
		
		String str=KG.getTagList();
		for(int i=0;i<ListofGroups.size();i++)
			if(str.equalsIgnoreCase((ListofGroups.get(i)).getTagList()))
				return true;
			
		return false;
		
	}
	
}