package pbcl.curve;

import pbcl.element.Fp;
import pbcl.element.Fp2;

public class ECPointFp2 
{
	//--------------------------
	// class variable
	//--------------------------
	private Fp2 x;
	private Fp2 y;
	
	private boolean infinity = false;
	
	//--------------------------
	// constructor
	//--------------------------
	public ECPointFp2()
	{
		this.x = Fp2.Zero;
		this.y = Fp2.Zero;
		this.infinity = true;
	}
	
	public ECPointFp2(String x, String y)
	{ 
		this.x = new Fp2(new Fp(x,16), Fp.Zero);
		this.y = new Fp2(new Fp(y,16), Fp.Zero);
		this.infinity = false;
	}
	
	public ECPointFp2(Fp2 x, Fp2 y)
	{
		this.x = x;
		this.y = y;
		this.infinity = false;
	}
	
	//--------------------------
	// constant
	//--------------------------
	public static final ECPointFp2 Infinity = new ECPointFp2();
	
	//--------------------------
	// operation
	//--------------------------
	public boolean equals(ECPointFp2 P)
	{
		return this.x.equals(P.x) && this.y.equals(P.y);
	}
	
	public void assign(ECPointFp2 P)
	{
		this.x = P.x;
		this.y = P.y;
		this.infinity = P.infinity;
	}
	
	public void clear()
	{
		this.x = Fp2.Zero;
		this.y = Fp2.Zero;
		this.infinity = true;
	}
	
	//--------------------------
	// accessory
	//--------------------------
	public void set(Fp2 x, Fp2 y)
	{
		this.x = x;
		this.y = y;
		this.infinity = false;
	}
	
	public Fp2 getX(){ return this.x; }
	public Fp2 getY(){ return this.y; }
	
	public String toString()
	{
		if( infinity ) return "infinity";
		return "[" + this.x + ", " + this.y + "]";
	}
	
	public boolean isInfinity(){ return infinity; }
	
	public void setInfinity(boolean flg){ this.infinity = flg;}
}
