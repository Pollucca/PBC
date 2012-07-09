package pbcl.group;

import java.math.BigInteger;

public class Zr 
{
	BigInteger value;
	
	public Zr(){}
	
	public Zr(BigInteger v){
		value = v;
	}
	
	public BigInteger getValue(){ return value; }
	
	public String toString(){
		return value.toString(16);
	}
	
	public void set(String data){
		value = new BigInteger(data,16);
	}
}