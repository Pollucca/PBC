package pbcl.curve;

import java.math.BigInteger;
import pbcl.element.Fp;
import pbcl.element.Fp2;

public class EllipticCurveFp
{
	private Fp a; 	// coefficient a
	private Fp b;	// coefficient b
	
	private BigInteger r;		// order of group
	private BigInteger t;		// trace of frobenius
	private BigInteger h;		// cofactor rh = #E(Fp)
	
	private BigInteger l;		// for final exponentiation
	
	private final Fp _3 = new Fp(3);
	
	//--------------------------
	// constructor
	//--------------------------
	public EllipticCurveFp(Fp a, Fp b, BigInteger r, BigInteger t)
	{
		this.a = a;
		this.b = b;
		this.r = r;
		this.t = t;
		this.h = cofactor(Fp.getChr(), r, t);
		this.l = Fp.getChr().add(BigInteger.ONE).divide(r);
	}
	
	//--------------------------
	// calculation of cofactor
	//   : h = ( p + 1 - t ) / r
	//--------------------------
	public BigInteger cofactor(BigInteger p, BigInteger r, BigInteger t)
	{
		BigInteger g = p.add(BigInteger.ONE).subtract(t);
		return g.divide(r);
	}
	
	//--------------------------
	// generate random point
	//--------------------------
	public ECPointFp randomPoint()
	{
		Fp x;
		ECPointFp P = new ECPointFp();
		
		do{	x = Fp.random(); }
		while( !genPoint(P, x) );
		
		return P;
	}
	
	//--------------------------------
	// generate random torsion point
	//--------------------------------
	public ECPointFp torsionPoint()
	{		
		ECPointFp G, H;

		do{
			H = this.randomPoint();
			G = this.mul(h, H);
		} while( G.isInfinity() );
		
		H = this.mul(r, G);
		
		if( H.isInfinity() ) return G;
		
		System.err.println("error to make torsion point");
		
		return null;
	}
	
	//--------------------------
	// generate point with x
	//--------------------------
	public boolean genPoint(ECPointFp P, Fp x)
	{
		Fp t1 = x.mul(x.sqr());
		Fp t2 = a.mul(x);
		Fp y2 = t1.add(t2).add(b);
		Fp y = Fp.sqrt(y2);
		if ( y == null ) return false;
		P.set(x, y);
		return true;
	}

	//--------------------------
	// add point
	//  ( jacobian + affine )
	//--------------------------
	public ECPointFp addj(final ECPointFp P, final ECPointFp Q)
	{
		Fp x1 = P.getX();
		Fp y1 = P.getY();
		Fp z1 = P.getZ();
		Fp x2 = Q.getX();
		Fp y2 = Q.getY();
		
		Fp t1 = z1.sqr();
		Fp t2 = z1.mul(t1);
		Fp t3 = t1.mul(x2).sub(x1);
		t2 = t2.mul(y2).sub(y1);
		
		if( t3.isZero() )
		{
			if( t2.isZero() ){ return dobj(P); }
			return ECPointFp.Infinity;
		}
		
		Fp z3 = z1.mul(t3);
		t1 = t3.sqr();
		Fp t4 = x1.mul(t1);
		Fp t5 = t1.mul(t3);
		
		Fp x3 = t2.sqr();
		x3 = x3.sub(t5);
		t1 = t4.add(t4);
		x3 = x3.sub(t1);
		t4 = t4.sub(x3);
		t1 = t5.mul(y1);
		
		Fp y3 = t2.mul(t4);
		y3 = y3.sub(t1);
		
		return new ECPointFp(x3, y3, z3);
	}
	
	//--------------------------
	// doubling point
	//  ( jacobian )
	//--------------------------	
	public ECPointFp dobj(final ECPointFp P)
	{
		Fp x1 = P.getX();
		Fp y1 = P.getY();
		Fp z1 = P.getZ();
		
		Fp t1 = y1.sqr();
		Fp t2 = x1.add(x1);
		t2 = t2.add(t2);
		t2 = t2.mul(t1);
		t1 = t1.sqr();
		t1 = t1.add(t1);
		t1 = t1.add(t1);
		t1 = t1.add(t1);
		Fp t3 = z1.sqr();
		t3 = t3.sqr();
		t3 = t3.mul(a);
		Fp t4 = x1.sqr();
		t4 = t4.add(t4).add(t4);
		t3 = t3.add(t4);
		
		Fp x3 = t3.sqr();
		t4 = t2.add(t2);
		x3 = x3.sub(t4);
		
		Fp z3 = y1.mul(z1);
		z3 = z3.add(z3);
		
		Fp y3 = t2.sub(x3);
		y3 = y3.mul(t3);
		y3 = y3.sub(t1);
		
		return new ECPointFp(x3, y3, z3);
	}
	
	//--------------------------
	// add point
	//--------------------------	
	public ECPointFp add(final ECPointFp P, final ECPointFp Q)
	{
		if( P.isInfinity() ){ return Q; }
		if( Q.isInfinity() ){ return P; }
		
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
	public ECPointFp dob(final ECPointFp P)
	{
		if( P.isInfinity() ){ return P; }
		
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
	// scalar multiplication
	//--------------------------
	public ECPointFp mul(final BigInteger n, final ECPointFp P)
	{
		ECPointFp Q = new ECPointFp();
		Q.assign(P);
		int l = n.bitLength();
		for(int i=l-2;i>=0;i--)
		{
			Q = dobj(Q);
			if( n.testBit(i) ){
				Q = addj(Q, P);
			}
		}
		return Q.affine();
	}

	//--------------------------
	// scalar multiplication
	//--------------------------
	public ECPointFp affine_mul(final BigInteger n, final ECPointFp P)
	{
		ECPointFp Q = new ECPointFp();
		Q.assign(P);
		int l = n.bitLength();
		for(int i=l-2;i>=0;i--)
		{
			Q = dob(Q);
			if( n.testBit(i) ){
				Q = add(Q, P);
			}
		}
		return Q;
	}
	
	//--------------------------
	// distortion map
	//--------------------------
	public ECPointFp2 DistortionMap(final ECPointFp Q)
	{
		Fp2 x = new Fp2(Q.getX().neg(),Fp.Zero);
		Fp2 y = (new Fp2(Q.getY(),Fp.Zero)).xi_mul();
		return new ECPointFp2(x, y);
	}

	//--------------------------
	// vertical line
	//--------------------------	
	public Fp2 v(final ECPointFp V, final ECPointFp2 Q)
	{
		if( V.isInfinity() ){ return Fp2.One; }
		return Q.getX().sub(V.getX());
	}
	
	//--------------------------
	// line V and P
	//--------------------------	
	public Fp2 g(final ECPointFp V, final ECPointFp P, final ECPointFp2 Q)
	{	
		Fp m = P.getY().sub(V.getY());
		Fp n = P.getX().sub(V.getX());
		
		Fp2 t = Q.getY().sub(V.getY()).mul(n);
		Fp2 u = Q.getX().sub(V.getX()).mul(m);
		
		return t.sub(u);
	}
	
	//--------------------------
	// tangent line
	//--------------------------	
	public Fp2 g(final ECPointFp V, final ECPointFp2 Q)
	{
		Fp m = a.add(V.getX().sqr().mul(_3));	// m = 3*x^2+a
		Fp n = V.getY().add(V.getY());			// n = 2y
		
		Fp2 t = Q.getY().sub(V.getY()).mul(n);
		Fp2 u = Q.getX().sub(V.getX()).mul(m);
		
		return t.sub(u);
	}
	
	//--------------------------
	// pairing
	//--------------------------
	public Fp2 pairing(final ECPointFp P, final ECPointFp Q)
	{
		Fp2 f = Fp2.One;
		ECPointFp V = P;
		ECPointFp2 _Q = DistortionMap(Q);
		
		for(int i=r.bitLength()-2;i>=0;i--)
		{
			f = f.sqr().mul(g(V,_Q)); 
			V = dob(V);
			if( r.testBit(i) ){
				f = f.mul(g(V,P,_Q));
				V = add(V,P);
			}
		}
		return fexp(f);
	}
	
	//--------------------------
	// final exponentiation
	//   f^g : g = (p^2-1)/r = (p-1)(p+1)/r
	//--------------------------
	public Fp2 fexp(final Fp2 f)
	{		
		Fp2 u = f.frob_p().mul(f.inv());
		return u.pow(l);
	}
	
	public String toString()
	{
		return "E/Fq: Y^2=X^3+" + a + "x+" + b;	
	}
}
