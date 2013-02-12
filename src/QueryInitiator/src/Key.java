/**
 * Author : Ruturaj Patil
 * This class represents Key object with String Value and bit vector Popularoty as attributes
 * it provides 4 methods to update popularity requestKey() and periodicShift() and getValue and getPopularity to
 * access attributes
 * 
 */
package query_Initiator;

import java.util.BitSet;

public class Key
{
	String value;
	BitSet popularity;
	Key()
	{
		popularity=new BitSet(8);
		popularity.set(4,7);
		
	}
	
	Key(String str)
	{
		value=str;
		popularity=new BitSet(8);
		popularity.set(4,7);
	}
	public String requestKey()
	{
						
		System.out.println("Current Popularity: "+popularity.toString());		
		for(int i=1;i<popularity.size();i++)//right shift
			popularity.set(i,popularity.get(i-1));
		popularity.set(0);		
		System.out.println("New Popularity: "+popularity.toString());
		return value;
	}
	public void perodicShift()
	{
						
		System.out.println("Current Popularity: "+popularity.toString());		
		for(int i=1;i<popularity.size();i++)//right shift
			popularity.set(i,popularity.get(i-1));
		popularity.clear(0);		
		System.out.println("New Popularity: "+popularity.toString());
				

	}
	public String getValue()
	{
		return this.value;
	}
	public String getPopularity()
	{
		return this.popularity.toString();
	}
	
}
