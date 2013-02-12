
/*
 * Author : Ruturaj Patil
 * Class Math defines simple math functions required in this package
 */
package query_Initiator;
public class Math
{
	public static int fact(int n)//.................Factorial function
	{
		int i=n-1,factorial=n;
		while(i>=2)
			factorial*=i--;
		if (n==0)
			return 1;// 0!=1
		return factorial;
	}
	public static int nCr(int n, int r)//...........Computes nCr i.e. No of combinations
	{
		if(n>r)// otherwise n-r<0
		{	
			int Nfact,Rfact,N_Rfact;
			Nfact=fact(n);
			Rfact=fact(r);
			N_Rfact=fact(n-r);
			return Nfact/Rfact/N_Rfact;
		}
		
		return n;//invalid arguments n<r
	}
}
