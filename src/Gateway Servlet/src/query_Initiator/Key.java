/**
 * Author : Ruturaj Patil
 * This class represents Key object with taglist, Value and bit vector Popularoty as attributes
 * it provides 4 methods to update popularity requestKey() and periodicShift() and getValue and getPopularity to
 * access attributes
 * 
 */
package query_Initiator;

import java.util.BitSet;
//import java.util.*;

public class Key
{
	String TagList;
	public BitSet popularity;
	public Key()
	{
		popularity=new BitSet(8);
		popularity.clear(0,8);
		popularity.set(4,8);
		
	}
	
	public Key(String tl)
	{
		TagList=tl;
		//value="[][00001111]";
		popularity=new BitSet(8);
		popularity.clear(0,8);
		popularity.set(4,8);
	}
	public void requestKey()
	{
						
		System.out.println("Current Popularity: "+popularity.toString());
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
		for(int i=1;i<popularity.length();i++)//right shift
		{
			popularity.set(i,tmp.get(i-1));
			System.out.println(getPopularity());
		}
		popularity.clear(0);		
		System.out.println("New Popularity: "+popularity.toString());
		
				

	}
	public String getTagList()
	{
		return this.TagList;
	}
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
