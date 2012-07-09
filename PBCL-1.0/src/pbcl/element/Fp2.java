package pbcl.element;

import java.math.BigInteger;

public class Fp2
{
	//---------------------------------------------------------
	// constant value of irreducible polynomial  x^2 - B
	//---------------------------------------------------------
	private static Fp B = Fp.One;	// initial value is 1
	
	//-------------------------
	// initialization
	//-------------------------
	public static void init(final BigInteger p, final BigInteger b)
	{ 
		Fp.setChr(p); 
		Fp2.setB(new Fp(b)); 
	}
	
	//--------------------------
	// constant
	//--------------------------
	public static final Fp2 Zero = new Fp2();
	public static final Fp2 One = new Fp2(Fp.One, Fp.Zero);
	
	//-----------------------------------------------
	// set irreducible polynomial
	//-----------------------------------------------
	public static void setB(Fp b){ B = b; }
	
	//--------------------------
	// value
	//--------------------------
	private Fp v0;
	private Fp v1;
	
	public BigInteger[] getValue(){ return new BigInteger[]{ v0.getValue(), v1.getValue() }; }
	
	//--------------------------
	// generate random value
	//--------------------------
	public static Fp2 random()
	{
		Fp v0 = Fp.random();
		Fp v1 = Fp.random();
		return new Fp2(v0, v1);
	}

	//--------------------------
	// check whether 0 or not
	//--------------------------
	public boolean isZero(){ return v0.isZero() && v1.isZero(); }
	public boolean isOne(){ return v0.isOne() && v1.isZero(); }
	
	//--------------------------
	// To String
	//--------------------------
	public String toString(){ return v0.toString() + ", " + v1.toString(); }	
	
	//--------------------------
	// constructor
	//--------------------------
	public Fp2() { this.v0 = Fp.Zero; this.v1 = Fp.Zero; }
	public Fp2(Fp v0, Fp v1){ this.v0 = v0; this.v1 = v1; }
	
	//--------------------------
	// operation
	//--------------------------
	public final Fp2 add(final Fp y){ return new Fp2(v0.add(y), v1); }
	public final Fp2 sub(final Fp y){ return new Fp2(v0.sub(y), v1); }
	public final Fp2 mul(final Fp y){ return new Fp2(v0.mul(y), v1.mul(y)); }
	
	public final Fp2 add(final Fp2 y){ return new Fp2(v0.add(y.v0), v1.add(y.v1)); }
	public final Fp2 sub(final Fp2 y){ return new Fp2(v0.sub(y.v0), v1.sub(y.v1)); }
	public final Fp2 xi_mul(){ return new Fp2(v1.mul(B), v0); }
	
	public boolean equals(final Fp2 y){ return v0.equals(y.v0) && v1.equals(y.v1); }
	
	//--------------------------
	// multiplication
	//--------------------------
	public final Fp2 mul(final Fp2 y)
	{
		Fp s = v0.add(v1);
		Fp t = y.v0.add(y.v1);
		Fp d0 = s.mul(t);
		Fp d1 = v0.mul(y.v0);
		Fp d2 = v1.mul(y.v1);
		d0 = d0.sub(d1).sub(d2);
		return new Fp2(d1.add(d2.mul(B)),d0);
	}
	
	public final Fp2 sqr(){ return this.mul(this); }
	
	//--------------------------
	// power
	//--------------------------
	public final Fp2 pow(final BigInteger exp)
	{
		Fp2 t = this;
		int len = exp.bitLength();
		
		for(int i=len-2;i>=0;i--){
			t = t.sqr();
			if( exp.testBit(i) ) t = t.mul(this);
		}
		return t;
	}
	
	//--------------------------
	// inversion
	//--------------------------	
	public final Fp2 inv()
	{
		Fp t0 = v0.sqr();
		Fp t1 = v1.sqr();
		t0 = t0.sub(t1.mul(B));
		t1 = t0.inv();
		return new Fp2(v0.mul(t1), v1.mul(t1).neg());
	}
	
	//--------------------------
	// negation
	//--------------------------	
	public final Fp2 neg()
	{
		return new Fp2(v0.neg(), v1.neg());
	}
	
	//--------------------------
	// quadratic residue test
	//--------------------------	
	public boolean qr_test()
	{
		BigInteger q = Fp.getChr().pow(2).subtract(BigInteger.ONE);
		BigInteger s = q.shiftRight(1);
		
		Fp2 r = this.pow(s);
		
		return r.isOne();
	}
	
	//--------------------------
	// square root
	//--------------------------
	public Fp2 sqrt()
	{
		if( !this.qr_test() ){ return null; }
		
		int e = 0, s, m;
		BigInteger r, u;
		Fp2 b, c, t, x, y;
		
		BigInteger q = Fp.getChr().pow(2).subtract(BigInteger.ONE);
		
		while( !q.testBit(e) ){ e++; }
		r = q.shiftRight(e);
		
		do{ c = Fp2.random(); } while( c.qr_test() );
		
		y = c.pow(r);
		
		r = r.subtract(BigInteger.ONE).shiftRight(1);
		t = this.pow(r);
		s = e;
		x = t.mul(this);
		b = t.mul(x);
		
		while( !b.isOne() ){
			m = 0; c = b;
			do{ c = c.sqr(); m++; } while( !c.isOne() );
			u = BigInteger.ZERO.setBit(s-m-1);
			t = y.pow(u);
			y = t.sqr();
			s = m;
			x = x.mul(t);
			b = b.mul(y);
		}
		
		b = x.sqr();
		
		if( this.equals(b) ) return x;
		
		return null;
	}
	
	//------------------------------------------
	// conjugation x^p = x.conv
	//------------------------------------------
	public final Fp2 conj()
	{
		return new Fp2(v0, v1.neg());
	}
	
	//--------------------------------------------------------
	//	Frobenius map
	//--------------------------------------------------------
	public final Fp2 frob_p()
	{
		return this.conj();
	}
}
