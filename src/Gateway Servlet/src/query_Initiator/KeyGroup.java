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
	public int s=0;// size of KeyGroup
	public boolean suspended=false;
	public BitSet popularity=new BitSet(8);
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
		popularity.clear(0,8);
		popularity.set(4,8);
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
		char pop[]={'0','0','0','0','0','0','0','0'};
		for(int i=0;i<popularity.length();i++)
			if(popularity.get(i))
				pop[i]='1';
			else
				pop[i]='0';
		return new String(pop);
	}
	public BitSet copyPopularity()
	{
		return (BitSet)popularity.clone();
	}
	public void setPopularity(String val)
	{
		char pop[]=val.toCharArray();
		popularity=new BitSet(8);
		popularity.set(0,8);
		for(int i=0;i<pop.length;i++)
		{
			if(pop[i]=='1')
				popularity.set(i);
			else
				popularity.clear(i);
		}
	}
	public void requestKey()
	{
						
		System.out.println("Current Popularity: "+popularity.toString());
		System.out.println(getPopularity());
		BitSet tmp=(BitSet)popularity.clone();
		for(int i=1;i<popularity.length();i++)//right shift
		{
			popularity.set(i,tmp.get(i-1));
			//System.out.println(popularity.toString());
			//System.out.println(getPopularity());
		}
		popularity.set(0,true);	
		System.out.println("New Popularity: "+popularity.toString());
		System.out.println(getPopularity());
		//return TagList;
	}
	public void perodicShift()
	{
		BitSet tmp=(BitSet)popularity.clone();				
		System.out.println("Current Popularity: "+popularity.toString());		
		for(int i=1;i<popularity.size();i++)//right shift
		{
			popularity.set(i,tmp.get(i-1));
			System.out.println(getPopularity());
		}
		popularity.clear(0);		
		System.out.println("New Popularity: "+popularity.toString());
				

	}
	public long toDecimal(String val)
	{
		long p=0;
		char pop[]=val.toCharArray();
		popularity=new BitSet(8);
		popularity.set(0,8);
		for(int i=pop.length-1;i>0;i--)
		{
			if(pop[i]=='1')
				p+=java.lang.Math.pow(2, i);
			
		}
		return p;
	}
	
	
}