package pbcl.group;

import java.math.BigInteger;

public class G2 
{
	BigInteger[] x;
	BigInteger[] y;
	
	public BigInteger[] getX(){ return x; }
	public BigInteger[] getY(){ return y; }
	
	public G2(){}
	
	public G2(BigInteger x, BigInteger y)
	{
		this.x = new BigInteger[1];
		this.y = new BigInteger[1];
		
		this.x[0] = x;
		this.y[0] = y;
	}
	
	public G2(BigInteger[] x, BigInteger[] y)
	{
		this.x = x.clone();
		this.y = y.clone();
	}
	
	void init(int size)
	{
		x = new BigInteger[size];
		y = new BigInteger[size];
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(util.out(x));
		sb.append(",");
		sb.append(util.out(y));
		
		return sb.toString();
	}
	
	public void set(String data)
	{
		String[] list = data.split(",");
		x = util.in(list[0]);
		y = util.in(list[1]);
	}
	
	public boolean equals(final G2 v)
	{
		if( !util.equals(this.x, v.x) ) return false;
		if( !util.equals(this.y, v.y) ) return false;
		return true;
	}
}
