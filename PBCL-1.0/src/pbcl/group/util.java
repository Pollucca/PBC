package pbcl.group;

import java.math.BigInteger;

public class util 
{
	static String out(BigInteger[] v)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<v.length;i++){ 
			sb.append(v[i].toString(16)); 
			if( i!=(v.length-1) ) sb.append(" "); 
		}
		return sb.toString();
	}
	
	static BigInteger[] in(String data)
	{
		String[] list = data.split(" ");
		BigInteger[] v = new BigInteger[list.length];
		for(int i=0;i<list.length;i++){ v[i] = new BigInteger(list[i], 16); }
		return v;
	}

	public static Zr convert(String str)
	{
		BigInteger t = BigInteger.ZERO;
		
		for(byte s : str.getBytes())
		{
			t = t.or(BigInteger.valueOf(s));
			t = t.shiftLeft(8);
		}
		
		return new Zr(t);
	}

	public static String convert(Zr m)
	{
		BigInteger t = m.getValue();
		
		StringBuilder sb = new StringBuilder();
		
		byte mask = (byte) 0xff;
		while( !t.equals(BigInteger.ZERO) )
		{
			byte v = (byte) (t.byteValue() & mask);
			sb.insert(0, (char)v);
			t = t.shiftRight(8);
		}
		
		return sb.toString();
	}
	
	public static Zr convert(Gt g)
	{
		return new Zr(g.getValue()[0]);
	}
	
	public static Zr xor(Zr a, Zr b)
	{
		return new Zr(a.getValue().xor(b.getValue()));
	}
	
	public static boolean equals(final BigInteger[] a, final BigInteger[] b)
	{
		if( a.length != b.length ) return false;
		for(int i=0;i<a.length;i++){
			if( a[i].compareTo(b[i]) != 0 ) return false;
		}
		return true;
	}
	
}
