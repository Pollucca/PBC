package pbcl.group;

import java.math.BigInteger;

public class Gt 
{
	BigInteger[] value;
	
	public Gt(){}
	
	public Gt(BigInteger[] v)
	{
		//value = new BigInteger[v.length];
		//for(int i=0;i<v.length;i++){ value[i] = v[i]; }
		value = v.clone();
	}
	
	public BigInteger[] getValue(){ return value; }
	
	public String toString()
	{
		return util.out(value);
	}
	
	public void set(String data)
	{
		value = util.in(data);
	}
	
	public boolean equals(Gt x)
	{
		if( this.value.length != x.value.length ) return false;
		
		for(int i=0;i<this.value.length;i++){
			if( !value[i].equals(x.value[i]) ) return false;
		}
		return true;
	}
}
