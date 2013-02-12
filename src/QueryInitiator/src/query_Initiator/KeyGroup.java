/**
 * Author : Ruturaj Patil
 * This class extends class Key. It sounds complicated but it is actually nothing but
 * class Key with value=key1,key2,...keyN. TreeSet KeyList is just for maintaining a sorted
 * list of UNIQUE keys. Also no. of keys is less than Smax
 */
package query_Initiator;
import java.util.*;
class KeyGroup extends Key
{
	TreeSet<String> KeyList;//=new TreeSet<>();
	int s=0;
	//String value;
	BitSet popularity=new BitSet();
	KeyGroup()
	{
		//this.value=super.value;
	}
	KeyGroup(Key x)
	{
		//this.value=super.value;
		super();
		KeyList=new TreeSet<>();
		
		if((KeyList.contains(x.getValue())==false))
		{
			
			KeyList.add(x.getValue());
			
			s=1;
		}
	}
	//Copy Constructor
	KeyGroup(KeyGroup k)
	{
		super();
		
		this.KeyList=new TreeSet<>(k.KeyList);
		this.s=k.s;
		this.popularity=k.copyPopularity();
		//this.value=k.value;
	}
	/*KeyGroup(Key[] list, int from, int to,int Smax) //constructor to create KeyGroup from list of keys List
	{
		super();
		KeyList=new TreeSet();
		for(int i=from;i<to;i++)
		{
			if((KeyList.contains(list[i].getValue())==false)&&KeyList.size()< Smax)
			{
				KeyList.add(list[i].getValue());
				value+=","+list[i].getValue();
			}
			
		}		
		size=KeyList.size();
		
	} */
	public int addKey(Key x,int Smax)
	{
		if(KeyList.contains(x.getValue())==false)
			if(KeyList.size()<=Smax)
			{
				KeyList.add((String)x.getValue());
				/*if(size==0)
					this.value=x.getValue();
				else
				{
					this.value=value.concat(",");
					this.value=value.concat(x.getValue());	
				}*/
				s++;
			}
		else if(Smax>=s)
		{
			System.out.println("Smax reached");
			return 2;	//Smax reached
		}
		else
			return 1;	//Key already present
		return 0;		//Key added
	}
	public String getValue()
	{
		return KeyList.toString();
	}
	public String getPopularity()
	{
		return this.popularity.toString();
	}
	public BitSet copyPopularity()
	{
		return (BitSet)popularity.clone();
	}
	/*boolean contains(String str)
	{
		for(int i=0;i<KeyList.size();i++)
			if(str==KeyList.)
	}*/
	
}