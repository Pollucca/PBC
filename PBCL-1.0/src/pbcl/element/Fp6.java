package pbcl.element;

import java.math.BigInteger;

public class Fp6 
{
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
	public static final Fp6 Zero = new Fp6();
	public static final Fp6 One = new Fp6(Fp2.One, Fp2.Zero, Fp2.Zero);
	
	//--------------------------
	// value
	//--------------------------
	private Fp2 v0;
	private Fp2 v1;
	private Fp2 v2;
	
	//--------------------------
	// get value
	//--------------------------
	public BigInteger[] getValue()
	{
		BigInteger[] t0 = v0.getValue();
		BigInteger[] t1 = v1.getValue();
		BigInteger[] t2 = v2.getValue();
		
		BigInteger[] t = { t0[0], t0[1], t1[0], t1[1], t2[0], t2[1] };

		return t;
	}
	
	//--------------------------
	// generate random value
	//--------------------------
	public static Fp6 random()
	{
		Fp2 v0 = Fp2.random();
		Fp2 v1 = Fp2.random();
		Fp2 v2 = Fp2.random();
		return new Fp6(v0, v1, v2);
	}

	//--------------------------
	// check value
	//--------------------------
	public boolean isZero(){ return v0.isZero() && v1.isZero() && v2.isZero(); }
	public boolean isOne(){ return v0.isOne() && v1.isZero() && v2.isZero(); }
	
	//--------------------------
	// To String
	//--------------------------
	public String toString(){ return v0.toString() + ", " + v1.toString() + ", " + v2.toString(); }	
	
	//--------------------------
	// constructor
	//--------------------------
	public Fp6() { this.v0 = Fp2.Zero; this.v1 = Fp2.Zero; this.v2 = Fp2.Zero; }
	
	public Fp6(Fp2 v0, Fp2 v1, Fp2 v2){ this.v0 = v0; this.v1 = v1; this.v2 = v2; }
	
	public Fp6(BigInteger[] data)
	{ 
		if( data.length != 6 ) return;
		
		v0 = new Fp2(new Fp(data[0]), new Fp(data[1]));
		v1 = new Fp2(new Fp(data[2]), new Fp(data[3]));
		v2 = new Fp2(new Fp(data[4]), new Fp(data[5]));
	}
	
	//--------------------------
	// operation
	//--------------------------
	public final Fp6 add(final Fp y){ return new Fp6(v0.add(y), v1, v2); }
	public final Fp6 sub(final Fp y){ return new Fp6(v0.sub(y), v1, v2); }
	public final Fp6 mul(final Fp y){ return new Fp6(v0.mul(y), v1.mul(y), v2.mul(y)); }
	
	public final Fp6 add(final Fp2 y){ return new Fp6(v0.add(y), v1, v2); }
	public final Fp6 sub(final Fp2 y){ return new Fp6(v0.sub(y), v1, v2); }
	public final Fp6 mul(final Fp2 y){ return new Fp6(v0.mul(y), v1.mul(y), v2.mul(y)); }
	
	public final Fp6 add(final Fp6 y){ return new Fp6(v0.add(y.v0), v1.add(y.v1), v2.add(y.v2)); }
	public final Fp6 sub(final Fp6 y){ return new Fp6(v0.sub(y.v0), v1.sub(y.v1), v2.sub(y.v2)); }
	
	public boolean equals(final Fp6 y){ return v0.equals(y.v0) && v1.equals(y.v1) && v2.equals(y.v2); }
	
	//--------------------------
	// multiplication
	//--------------------------
	public final Fp6 mul(final Fp6 y)
	{
		Fp2 t0 = v0.mul(y.v0);
		Fp2 t1 = v1.mul(y.v1);
		Fp2 t2 = v2.mul(y.v2);
		
		Fp2 m0 = v1.add(v2);
		Fp2 n0 = y.v1.add(y.v2);
		Fp2 c0 = m0.mul(n0).sub(t1).sub(t2);
		
		c0 = c0.xi_mul().add(t0);
		
		Fp2 m1 = v0.add(v1);
		Fp2 n1 = y.v0.add(y.v1);
		Fp2 c1 = m1.mul(n1).sub(t0).sub(t1);
		c1 = c1.add(t2.xi_mul());
		
		Fp2 m2 = v0.add(v2);
		Fp2 n2 = y.v0.add(y.v2);
		Fp2 c2 = m2.mul(n2).sub(t0).sub(t2).add(t1);
	
		return new Fp6(c0, c1, c2);
	}
	
	public final Fp6 sqr(){ return this.mul(this); }
	
	//--------------------------
	// power
	//--------------------------
	public final Fp6 pow(final BigInteger exp)
	{
		Fp6 t = this;
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
	public final Fp6 inv()
	{
		Fp2 t0 = v0.sqr();
		Fp2 t1 = v1.sqr();
		Fp2 t2 = v2.sqr();
		
		Fp2 t4 = v0.mul(v1);
		Fp2 t5 = v0.mul(v2);
		Fp2 t6 = v2.mul(v1);
		
		Fp2 c0 = t0.sub(t6.xi_mul());
		Fp2 c1 = t2.xi_mul().sub(t4);
		Fp2 c2 = t1.sub(t5);
		Fp2 t7 = v0.mul(c0);
		t7 = t7.add(v2.mul(c1).xi_mul());
		t7 = t7.add(v1.mul(c2).xi_mul());
		t7 = t7.inv();
		c0 = c0.mul(t7);
		c1 = c1.mul(t7);
		c2 = c2.mul(t7);
		return new Fp6(c0, c1, c2);
	}
	
	//--------------------------
	// negation
	//--------------------------
	public final Fp6 neg()
	{
		return new Fp6(v0.neg(), v1.neg(), v2.neg());
	}
	
	//--------------------------
	// quadratic residue test
	//--------------------------	
	public boolean qr_test()
	{
		BigInteger q = Fp.getChr().pow(6).subtract(BigInteger.ONE);
		BigInteger s = q.shiftRight(1);
		
		Fp6 r = this.pow(s);

		return r.isOne();
	}
	
	//--------------------------
	// square root
	//--------------------------
	public Fp6 sqrt()
	{
		if( !this.qr_test() ){ return null; }
		
		int e = 0, s, m;
		BigInteger r, u;
		Fp6 b, c, t, x, y;
		
		BigInteger q = Fp.getChr().pow(6).subtract(BigInteger.ONE);
		
		while( !q.testBit(e) ){ e++; }
		r = q.shiftRight(e);
		
		do{ c = Fp6.random(); } while( c.qr_test() );
		
		y = c.pow(r);
		
		r = r.subtract(BigInteger.ONE).shiftRight(1);
		t = this.pow(r);
		s = e;
		x = t.mul(this);
		b = t.mul(x);
		
		while( !b.isOne() ){
			m = 0; c = b;
			do{ c = c.sqr(); m++; } while( !c.isOne() );
			u = (BigInteger.ZERO).setBit(s-m-1);
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
	
	//-------------------------------------
	// conjugation  (x^(p^3) = x.conj)
	//-------------------------------------
	public final Fp6 conj()
	{
		return new Fp6(v0.conj(), v1.conj().neg(), v2.conj());
	}
	
	//--------------------------------------------------------
	//	Frobenius map
	//--------------------------------------------------------
	public final Fp6 frob_p()
	{
		return this.pow(Fp.getChr());
	}
}
