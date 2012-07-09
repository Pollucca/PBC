package pbcl.element;

import java.math.BigInteger;
import java.util.Random;

public class Fp 
{	
	//-------------------------
	// characteristic
	//-------------------------
	private static BigInteger p = BigInteger.TEN;
	
	//-------------------------
	// initialization
	//-------------------------
	public static void init(final BigInteger p){ setChr(p); }
	
	//-------------------------
	// accessory
	//-------------------------
	public static void setChr(final BigInteger p) { Fp.p = p; }
	public static void setChr(final String str){ Fp.p = new BigInteger(str, 16); }
	public static void setChr(final String str, int base){ Fp.p = new BigInteger(str, base); }
	public static BigInteger getChr(){ return Fp.p; }
	
	//--------------------------
	// constant
	//--------------------------
	public static final Fp Zero = new Fp();	// zero
	public static final Fp One = new Fp(1);	// one
	
	//--------------------------
	// value
	//--------------------------
	private BigInteger value;
	
	//--------------------------
	// constructor
	//--------------------------
	public Fp(){ this.value = BigInteger.ZERO; }
	public Fp(long value){ this.value = BigInteger.valueOf(value).mod(Fp.p); }
	public Fp(String str){ this.value = (new BigInteger(str)).mod(Fp.p); }
	public Fp(String str, int base){ this.value = (new BigInteger(str,base)).mod(Fp.p); }
	public Fp(final BigInteger value){ this.value = value.mod(Fp.p); }
	
	//--------------------------
	// To BigInteger
	//--------------------------
	public BigInteger getValue(){ return value; }
	
	//--------------------------
	// To String
	//--------------------------
	public String toString(){ return value.toString(16); }
	public String toString(int radix){ return value.toString(radix); }
	
	//--------------------------
	// check whether 0 or not
	//--------------------------
	public boolean isZero(){ return value.equals(BigInteger.ZERO); }
	public boolean isOne(){ return value.equals(BigInteger.ONE); }
	public boolean isRange(){ return value.compareTo(p) < 0; }
	public boolean isMinus(){ return value.signum() < 0; }
	
	public static Random rnd = null;
	
	//--------------------------
	// generate random value
	//--------------------------
	public static Fp random()
	{
		if( rnd == null ){ rnd = new Random(); }
		BigInteger r = new BigInteger(Fp.getChr().bitLength(), rnd);
		return new Fp(r);
	}

	//--------------------------
	// operation
	//--------------------------
	public final Fp add(final Fp y){ Fp x = new Fp(); x.value = value.add(y.value); if( !x.isRange() ) x.value = x.value.subtract(p); return x; }
	public final Fp sub(final Fp y){ Fp x = new Fp(); x.value = value.subtract(y.value); if( x.isMinus() ) x.value = x.value.add(p); return x; }
	public final Fp mul(final Fp y){ return new Fp(value.multiply(y.value)); }
	public final Fp sqr(){ return this.mul(this); }
	public final Fp pow(final BigInteger ex){ return new Fp(value.modPow(ex, p)); }
	public final Fp inv(){ return new Fp(value.modInverse(p)); }
	public final Fp neg(){ return new Fp(p.subtract(value)); }
	
	public boolean equals(final Fp x){ return value.equals(x.value); }
	
	//--------------------------
	// legendre symbol
	//--------------------------
	private static BigInteger legendre(final BigInteger x, final BigInteger p)
	{
		BigInteger a = p.subtract(BigInteger.ONE).shiftRight(1);
		return x.modPow(a, p);
	}

	//-----------------------------
	// check quadratic residure
	//-----------------------------
	private static boolean QRTest(final BigInteger x, final BigInteger p)
	{
		return ( legendre(x, p).equals(BigInteger.ONE) );
	}
	
	//--------------------------
	// square root
	//--------------------------
	public static Fp sqrt(final Fp _x)
	{
		BigInteger p = Fp.p;
		BigInteger x = _x.value;
		
		if( !QRTest(x, p) ){ return null; }
		
		BigInteger t, u;
		
		BigInteger _1 = BigInteger.ONE;
		BigInteger _3 = BigInteger.valueOf(3);
		BigInteger _4 = BigInteger.valueOf(4);
		BigInteger _5 = BigInteger.valueOf(5);
		BigInteger _8 = BigInteger.valueOf(8);
		
		//-------------------------------
		// p = 3 mod 4
		//-------------------------------
		if( p.mod(_4).intValue() == 3 )
		{
			t = p.add(_1).shiftRight(2);
			return new Fp(x.modPow(t, p)); 
		}
		//-------------------------------
		// p = 5 mod 8
		//-------------------------------
		if( p.mod(_8).intValue() == 5 )
		{
			t = p.subtract(_1);
			u = t.shiftRight(2);
			BigInteger f = x.modPow(u, p);
			if( f.equals(_1) )
			{
				t = p.add(_3).shiftRight(3);
				return new Fp(x.modPow(t, p));
			}
			else if( f.equals(t) )
			{
				t = p.subtract(_5).shiftRight(3);
				u = x.shiftLeft(2).modPow(t, p);
				return new Fp(u.modPow(x.shiftLeft(1), p));				
			}
		}
		//--------------------------------
		//  tonelli shanks solve
		//--------------------------------
		return sqrt_ts(_x);
	}
	
	//--------------------------------------
	// square root with tonelli shanks
	//--------------------------------------
	public static Fp sqrt_ts(final Fp a)
	{
		long s, e = 0, m;
		
		Fp n;
		BigInteger _p, r, x, y, b, t, u;
		
		//-----------------------------
		//  (p - 1) = 2^e*r
		//-----------------------------
		_p = Fp.p.subtract(BigInteger.ONE); r = _p;
		while( !r.testBit(0) ){ e++; r = r.shiftRight(1); }
		
		//------------------------------
		//   (n/p) = -1
		//------------------------------
		do{ n = Fp.random(); } while( !legendre(n.value, p).equals(_p) );
		
		//------------------------------
		//   calculation
		//------------------------------
		y = n.value.modPow(r, p);		// y = n^r mod p
		s = e;							//
		r = r.subtract(BigInteger.ONE);	//
		r = r.shiftRight(1);			//
		x = a.value.modPow(r, p);		// x = a^[(r-1)/2] mod p
		b = x.multiply(x).mod(p);		//
		b = b.multiply(a.value).mod(p);	// b = a * x^2 mod p
		x = x.multiply(a.value).mod(p);	// x = a * x mod p
		
		//------------------------------
		//   b = 1 mod p
		//------------------------------
		while( !b.equals(BigInteger.ONE) )
		{
			m = 0; r = b;
			do{ 
				r = r.multiply(r).mod(p); m++;
			} while( !r.equals(BigInteger.ONE) );
			
			u = BigInteger.valueOf(2);					// 
			u = u.modPow(BigInteger.valueOf(s-m-1), p);	// u = 2^(s-m-1)
			t = y.modPow(u, p);							// t = y^u mod p
			y = t.multiply(t).mod(p);					//
			s = m;
			x = t.multiply(x).mod(p);		// x = x * t mod p
			b = b.multiply(y).mod(p);		// b = b * y mod p
		}
		
		b = x.multiply(x).mod(p);
		
		if( b.equals(a.value) ) return new Fp(x);
		
		return null;
	}
}
