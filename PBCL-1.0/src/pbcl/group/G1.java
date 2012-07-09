package pbcl.group;

import java.math.BigInteger;

public class G1 
{
	BigInteger x;
	BigInteger y;
	
	public BigInteger getX(){ return x; }
	public BigInteger getY(){ return y; }
	
	public G1(){}
	
	public G1(BigInteger x, BigInteger y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return x.toString(16) + "," + y.toString(16);
	}
	
	public void set(String data)
	{
		String[] list = data.split(",");
		x = new BigInteger(list[0], 16);
		y = new BigInteger(list[1], 16);
	}
}
