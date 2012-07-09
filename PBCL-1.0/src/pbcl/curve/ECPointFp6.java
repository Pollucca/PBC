package pbcl.curve;

import pbcl.element.Fp6;

public class ECPointFp6 
{
	//--------------------------
	// class variable
	//--------------------------
	private Fp6 x;
	private Fp6 y;
	private Fp6 z;
	
	private boolean infinity = false;
	
	//--------------------------
	// constructor
	//--------------------------
	public ECPointFp6(){
		this.x = Fp6.One;
		this.y = Fp6.One;
		this.z = Fp6.Zero;
		this.infinity = true;
	}
	
	public ECPointFp6(Fp6 x, Fp6 y)
	{
		this.x = x;
		this.y = y;
		this.z = Fp6.One;
		this.infinity = false;
	}
	
	public ECPointFp6(Fp6 x, Fp6 y, Fp6 z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.infinity = false;
	}
	
	//--------------------------
	// constant
	//--------------------------
	public static final ECPointFp6 Infinity = new ECPointFp6();
	
	public void set(Fp6 x, Fp6 y)
	{
		this.x = x;
		this.y = y;
		this.z = Fp6.One;
		this.infinity = false;
	}
	
	//--------------------------
	// operation
	//--------------------------
	public boolean equals(ECPointFp6 P)
	{
		return this.x.equals(P.x) && this.y.equals(P.y) && this.z.equals(P.z);
	}
	
	public void assign(ECPointFp6 P)
	{
		this.x = P.x;
		this.y = P.y;
		this.z = P.z;
		this.infinity = P.infinity;
	}
	
	public void clear()
	{
		this.x = Fp6.One;
		this.y = Fp6.One;
		this.z = Fp6.Zero;
		this.infinity = true;
	}
	
	public ECPointFp6 affine()
	{
		if( isInfinity() ) return this;
		
		Fp6 iz = this.z.inv();
		Fp6 tx = iz.sqr();
		Fp6 ty = iz.mul(tx);
	
		return new ECPointFp6(this.x.mul(tx), this.y.mul(ty));
	}
	
	//--------------------------
	// accessory
	//--------------------------
	public Fp6 getX(){ return this.x; }
	public Fp6 getY(){ return this.y; }
	public Fp6 getZ(){ return this.z; }
	
	public String toString()
	{
		if( isInfinity() ) return "infinity";
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}
	
	public boolean isInfinity(){ return this.z.equals(Fp6.Zero); }
}
