/**
 * Author : Ruturaj Patil
 * This class extends class Key. It sounds complicated but it is actually nothing but
 * class Key with value=key1,key2,...keyN. TreeSet KeyList is just for maintaining a sorted
 * list of UNIQUE keys. Also no. of keys is less than Smax
 */
package query_Initiator;
import java.util.*;
public class KeyGroup extends Key
{
	TreeSet<String> TagList;//=new TreeSet<>();
	int s=0;// size of KeyGroup
	public boolean suspended=false;
	BitSet popularity=new BitSet(8);
	KeyGroup()
	{
		
	}
	KeyGroup(Key x)
	{
		
		super();
		TagList=new TreeSet<>();
		
		if((TagList.contains(x.getTagList())==false))
		{
			
			TagList.add(x.getTagList());
			
			s=1;
		}
		popularity.set(4,7);
	}
	//Copy Constructor
	public KeyGroup(KeyGroup k)
	{
		super();
		
		this.TagList=new TreeSet<>(k.TagList);
		this.s=k.s;
		this.popularity=k.copyPopularity();
		
	}
	
	
	public int addKey(Key x,int Smax)
	{
		
		if(TagList.contains(x.getTagList())==false)
			if(TagList.size()<=Smax)
			{
				TagList.add(x.getTagList());
				
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
	@Override
	public String getTagList()
	{
		return TagList.toString();
	}
	@Override
	public String getPopularity()
	{
		return this.popularity.toString();
	}
	public BitSet copyPopularity()
	{
		return (BitSet)popularity.clone();
	}
	
	
}