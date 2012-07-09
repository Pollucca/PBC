package pbcl.curve;

import pbcl.element.Fp;

public class ECPointFp
{
	//--------------------------
	// class variable
	//--------------------------
	private Fp x;
	private Fp y;
	private Fp z;
	
	//--------------------------
	// constructor
	//--------------------------
	public ECPointFp()
	{
		this.x = Fp.One;
		this.y = Fp.One;
		this.z = Fp.Zero;
	}
	
	public ECPointFp(String x, String y)
	{ 
		this.x = new Fp(x,16);
		this.y = new Fp(y,16);
		this.z = Fp.One;
	}
	
	public ECPointFp(Fp x, Fp y)
	{
		this.x = x;
		this.y = y;
		this.z = Fp.One;
	}
	
	public ECPointFp(Fp x, Fp y, Fp z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//--------------------------
	// construct
	//--------------------------
	public static final ECPointFp Infinity = new ECPointFp();
	
	//--------------------------
	// operation
	//--------------------------
	public boolean equals(ECPointFp P)
	{
		return this.x.equals(P.x) && this.y.equals(P.y);
	}
	
	public void assign(ECPointFp P)
	{
		this.x = P.x;
		this.y = P.y;
		this.z = P.z;
	}
	
	public void clear()
	{
		this.x = Fp.One;
		this.y = Fp.One;
		this.z = Fp.Zero;
	}
	
	public ECPointFp affine()
	{
		if( isInfinity() ) return this;
		
		Fp iz = this.z.inv();
		Fp tx = iz.sqr();
		Fp ty = iz.mul(tx);
	
		return new ECPointFp(this.x.mul(tx), this.y.mul(ty));
	}
	
	public ECPointFp neg()
	{
		return new ECPointFp(this.x, this.y.neg());
	}
	
	//--------------------------
	// accessory
	//--------------------------
	public void set(Fp x, Fp y)
	{
		this.x = x;
		this.y = y;
		this.z = Fp.One;
	}
	
	public final Fp getX(){ return this.x; }
	public final Fp getY(){ return this.y; }
	public final Fp getZ(){ return this.z; }
	
	public String toString()
	{
		if( isInfinity() ) return "infinity";
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}
	
	public final boolean isInfinity(){ return this.z.isZero(); }
}
