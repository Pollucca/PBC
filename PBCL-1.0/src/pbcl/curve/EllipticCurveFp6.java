package pbcl.curve;

import java.math.BigInteger;

import pbcl.element.Fp;
import pbcl.element.Fp6;

public class EllipticCurveFp6 
{
	private Fp a; 		// coefficient a
	private Fp b;		// coefficient b
	
	private BigInteger r;		// order of group
	private BigInteger t;		// trace of frobenius
	private BigInteger h;		// cofactor rh = #E(Fp6)
	
	private BigInteger l;		// for final exponentiation
	
	private final Fp _3 = new Fp(3);
	
	//--------------------------
	// constructor
	//--------------------------
	public EllipticCurveFp6(Fp a, Fp b, BigInteger r, BigInteger t)
	{
		this.a = a;
		this.b = b;
		this.r = r;
		this.t = t;
		this.h = cofactor(Fp.getChr(), r, t, 6);
		this.l = Fp.getChr().multiply(Fp.getChr()).subtract(Fp.getChr()).add(BigInteger.ONE).divide(r);
	}
	
	//--------------------------
	// calculation of trace 
	//--------------------------
	private static final BigInteger trace(BigInteger p, BigInteger t, int n)
	{
		BigInteger[] _t = new BigInteger[n+1];
		_t[0] = BigInteger.valueOf(2); _t[1] = t;
		for(int i=1;i<n;i++){ _t[i+1] = t.multiply(_t[i]).subtract(p.multiply(_t[i-1])); }
		BigInteger tn = _t[n]; _t = null;
		return tn;
	}
	
	//--------------------------
	// calculation of cofactor
	//   : h = ( p + 1 - t ) / r
	//--------------------------	
	private static final BigInteger cofactor(BigInteger p, BigInteger r, BigInteger t, int n)
	{
		BigInteger tn = trace(p, t, n);
		BigInteger g = p.pow(n).add(BigInteger.ONE).subtract(tn);
		return g.divide(r).divide(r);
	}
	
	//--------------------------
	// generate random point
	//--------------------------
	public final ECPointFp6 randomPoint()
	{
		Fp6 x;
		ECPointFp6 P = new ECPointFp6();
		
		do{	x = Fp6.random(); }
		while( !genPoint(P, x) );

		return P;
	}
	
	//---------------------------------
	// generate random torsion point
	//---------------------------------
	public final ECPointFp6 torsionPoint()
	{
		ECPointFp6 G, H;
		
		do{
			H = this.randomPoint();
			G = this.mul(h, H);
		} while( G.isInfinity() );
		
		H = this.mul(r, G);
		
		if( H.isInfinity() ) return G;
		
		System.out.println("error to make torsion point");
		
		return null;
	}
	
	//--------------------------
	// generate point with x
	//--------------------------
	public final boolean genPoint(ECPointFp6 P, Fp6 x)
	{
		Fp6 t1 = x.mul(x.sqr());
		Fp6 t2 = x.mul(a);
		Fp6 y = t1.add(t2).add(b).sqrt();
		if ( y == null ) return false;
		P.set(x, y);
		return true;
	}
	
	//--------------------------
	// add point
	//  ( jacobian + affine )
	//--------------------------
	public final ECPointFp6 addj(final ECPointFp6 P, final ECPointFp6 Q)
	{
		Fp6 x1 = P.getX();
		Fp6 y1 = P.getY();
		Fp6 z1 = P.getZ();
		Fp6 x2 = Q.getX();
		Fp6 y2 = Q.getY();
		
		Fp6 t1 = z1.sqr();
		Fp6 t2 = z1.mul(t1);
		Fp6 t3 = t1.mul(x2);
		t2 = t2.mul(y2);
		t3 = t3.sub(x1);
		t2 = t2.sub(y1);
		
		if( t3.isZero() )
		{
			if( t2.isZero() ){ return dobj(P); }
			return ECPointFp6.Infinity;
		}
		
		Fp6 z3 = z1.mul(t3);
		t1 = t3.sqr();
		Fp6 t4 = x1.mul(t1);
		Fp6 t5 = t1.mul(t3);
		
		Fp6 x3 = t2.sqr();
		x3 = x3.sub(t5);
		t1 = t4.add(t4);
		x3 = x3.sub(t1);
		t4 = t4.sub(x3);
		t1 = t5.mul(y1);
		
		Fp6 y3 = t2.mul(t4);
		y3 = y3.sub(t1);
		
		return new ECPointFp6(x3, y3, z3);
	}
	
	//--------------------------
	// doubling point
	//  ( jacobian )
	//--------------------------	
	public final ECPointFp6 dobj(final ECPointFp6 P)
	{
		Fp6 x1 = P.getX();
		Fp6 y1 = P.getY();
		Fp6 z1 = P.getZ();
		
		Fp6 t1 = y1.sqr();
		Fp6 t2 = x1.add(x1);
		t2 = t2.add(t2);
		t2 = t2.mul(t1);
		t1 = t1.sqr();
		t1 = t1.add(t1);
		t1 = t1.add(t1);
		t1 = t1.add(t1);
		Fp6 t3 = z1.sqr();
		t3 = t3.sqr();
		t3 = t3.mul(a);
		Fp6 t4 = x1.sqr();
		t4 = t4.add(t4).add(t4);
		t3 = t3.add(t4);
		
		Fp6 x3 = t3.sqr();
		t4 = t2.add(t2);
		x3 = x3.sub(t4);
		
		Fp6 z3 = y1.mul(z1);
		z3 = z3.add(z3);
		
		Fp6 y3 = t2.sub(x3);
		y3 = y3.mul(t3);
		y3 = y3.sub(t1);
		
		return new ECPointFp6(x3, y3, z3);
	}
	
	//--------------------------
	// add point
	//--------------------------	
	public final ECPointFp add(final ECPointFp P, final ECPointFp Q)
	{
		if( P.isInfinity() ){ ECPointFp x = new ECPointFp(); x.assign(Q); return x; }
		if( Q.isInfinity() ){ ECPointFp x = new ECPointFp(); x.assign(P); return x; }
		
		Fp x1 = P.getX();
		Fp y1 = P.getY();
		Fp x2 = Q.getX();
		Fp y2 = Q.getY();
		
		Fp lambda, gamma, delta, x3, y3;
		
		lambda = y2.sub(y1);
		gamma  = x2.sub(x1);
		
		if( gamma.isZero() ){
			if( lambda.isZero() ){ return dob(P); }
			return ECPointFp.Infinity;
		}
		
		lambda = lambda.mul(gamma.inv());
		
		gamma = lambda.sqr();
		x3 = gamma.sub(x1).sub(x2);
		
		delta = x1.sub(x3);
		y3 = lambda.mul(delta).sub(y1);
		
		return new ECPointFp(x3,y3);
	}
	
	//--------------------------
	// doubling point
	//--------------------------	
	public final ECPointFp dob(final ECPointFp P)
	{
		if( P.isInfinity() ){ return ECPointFp.Infinity; }
		
		Fp x1 = P.getX();
		Fp y1 = P.getY();
		
		Fp lambda, gamma, delta, x3, y3;
		
		lambda = x1.sqr().mul(_3).add(a);
		gamma  = y1.add(y1);
		
		if( y1.isZero() ){ return ECPointFp.Infinity; }
		
		lambda  = lambda.mul(gamma.inv());
		
		gamma = lambda.sqr();
		x3 = gamma.sub(x1).sub(x1);
		
		delta = x1.sub(x3);
		y3 = lambda.mul(delta).sub(y1);
		
		return new ECPointFp(x3, y3);
	}
		
	//--------------------------
	// add point
	//--------------------------	
	public final ECPointFp6 add(final ECPointFp6 P, final ECPointFp6 Q)
	{
		if( P.isInfinity() ){ return Q; }
		if( Q.isInfinity() ){ return P; }
		
		Fp6 x1 = P.getX();
		Fp6 y1 = P.getY();
		Fp6 x2 = Q.getX();
		Fp6 y2 = Q.getY();
		
		Fp6 lambda, gamma, delta, x3, y3;
		
		lambda = y2.sub(y1);
		gamma  = x2.sub(x1);
		
		if( gamma.isZero() ){
			if( lambda.isZero() ){ return dob(P); }
			return ECPointFp6.Infinity;
		}
		
		lambda = lambda.mul(gamma.inv());
		
		gamma = lambda.sqr();
		x3 = gamma.sub(x1).sub(x2);
		
		delta = x1.sub(x3);
		y3 = lambda.mul(delta).sub(y1);
		
		return new ECPointFp6(x3,y3);
	}
	
	//--------------------------
	// doubling point
	//--------------------------	
	public final ECPointFp6 dob(final ECPointFp6 P)
	{
		if( P.isInfinity() ){ return ECPointFp6.Infinity; }
		
		Fp6 x1 = P.getX();
		Fp6 y1 = P.getY();
		
		Fp6 lambda, gamma, delta, x3, y3;
		
		lambda = x1.sqr().mul(_3).add(a);
		gamma  = y1.add(y1);
		
		if( y1.isZero() ){ return ECPointFp6.Infinity; }
		
		lambda  = lambda.mul(gamma.inv());
		
		gamma = lambda.sqr();
		x3 = gamma.sub(x1).sub(x1);
		
		delta = x1.sub(x3);
		y3 = lambda.mul(delta).sub(y1);
		
		return new ECPointFp6(x3, y3);
	}

	//--------------------------
	// scalar multiplication
	//--------------------------
	public final ECPointFp6 mul(final BigInteger n, final ECPointFp6 P)
	{
		ECPointFp6 Q = P;
		int l = n.bitLength();
		for(int i=l-2;i>=0;i--){
			Q = dob(Q);
			if( n.testBit(i) ){
				Q = add(Q, P);
			}
		}
		return Q.affine();
	}

	//--------------------------
	// vertical line
	//--------------------------	
	public final Fp6 v(final ECPointFp V, final ECPointFp6 Q)
	{
		if( V.isInfinity() ){ return Fp6.One; }
		return Q.getX().sub(V.getX());
	}
	
	//--------------------------
	// line V and P
	//--------------------------	
	public final Fp6 g(final ECPointFp V, final ECPointFp P, final ECPointFp6 Q)
	{
		Fp m = P.getY().sub(V.getY());
		Fp n = P.getX().sub(V.getX());
		
		Fp6 t = Q.getY().sub(V.getY()).mul(n);
		Fp6 u = Q.getX().sub(V.getX()).mul(m);
		
		return t.sub(u);
	}
	
	//--------------------------
	// tangent line
	//--------------------------	
	public final Fp6 g(final ECPointFp V, final ECPointFp6 Q)
	{		
		Fp m = a.add(V.getX().sqr().mul(_3));	// m = 3*x^2+a
		Fp n = V.getY().add(V.getY());			// n = 2y
		
		Fp6 t = Q.getY().sub(V.getY()).mul(n);
		Fp6 u = Q.getX().sub(V.getX()).mul(m);
		
		return t.sub(u);
	}
	
	//--------------------------
	// pairing
	//--------------------------
	public final Fp6 pairing(final ECPointFp P, final ECPointFp6 Q)
	{
		Fp6 f = Fp6.One;
		ECPointFp V = P;
		
		for(int i=r.bitLength()-2;i>=0;i--)
		{
			f = f.sqr().mul(g(V, Q)); 
			V = dob(V);
			f = f.mul(v(V, Q).conj());
			if( r.testBit(i) )
			{
				f = f.mul(g(V, P, Q));
				V = add(V,P);
				f = f.mul(v(V, Q).conj());
			}
		}
		return fexp(f);
	}
	
	//--------------------------
	// final exponentiation
	//--------------------------
	public final Fp6 fexp(final Fp6 f)
	{
		Fp6 u = f.conj().mul(f.inv());	// p^3 - 1
		Fp6 v = u.frob_p().mul(u);		// p + 1
		return v.pow(l);				// (p^2 - p + 1) / r
	}
}
