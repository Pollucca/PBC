package pbcl.pairing;

import java.math.BigInteger;
import java.util.Random;

import pbcl.curve.*;
import pbcl.group.*;
import pbcl.element.*;

public class OrdinaryCurveFp implements Pairing
{
	BigInteger q;		// field characteristic
	BigInteger r;		// group order
	BigInteger h;		// cofactor (p+1) = rh
	BigInteger t;		// trace of frobenius
	
	BigInteger a;		// elliptic curve 
	BigInteger b;		// y^2 = x^3 + ax + b
	BigInteger B;		// Irreducible polynomial z^6 - B
	
	EllipticCurveFp  curve1;
	EllipticCurveFp6 curve2;
	
	public OrdinaryCurveFp(String size)
	{
		if( size.equals("160") )
		{
			q = new BigInteger("1222965701665972809446759943409454109976443779851",10);
			r = new BigInteger("305741425416493202361689487439975889605713608671",10);
			t = new BigInteger("1993649550551553589345168",10);
			a = new BigInteger("-3",10);
			b = new BigInteger("115419023237406278170081633675601601871533058985",10);
			B = new BigInteger("1222965701665972809446759943409454109976443779849",10);
		}
		
		Fp6.init(q, B.negate());
		
		curve1 = new EllipticCurveFp(new Fp(a), new Fp(b), r, t);
		curve2 = new EllipticCurveFp6(new Fp(a), new Fp(b), r, t);
	}

	public Zr get_order(){ return new Zr(r); }

	//---------------------------------------------------
	// generate random integer 0 ~ r
	//---------------------------------------------------
	public Zr random_Zr() 
	{
		BigInteger t = new BigInteger(this.r.bitLength(), new Random());
		return new Zr(t.mod(r));
	}
	
	//-------------------------------------------
	// generate random point
	//-------------------------------------------
	public G1 random_G1() 
	{
		ECPointFp P = curve1.torsionPoint();
		return new G1(P.getX().getValue(), P.getY().getValue());
	}
	
	//-------------------------------------------
	// generate random point
	//-------------------------------------------
	public G2 random_G2() {
		ECPointFp6 P = curve2.torsionPoint();
		return new G2(P.getX().getValue(), P.getY().getValue());
	}
	
	//---------------------------------------------------
	// multiply Zr
	//---------------------------------------------------
	public Zr mul(Zr a, Zr b) 
	{
		return new Zr(a.getValue().multiply(b.getValue()).mod(r));
	}

	//---------------------------------------------------
	// addition Zr
	//---------------------------------------------------
	public Zr add(Zr a, Zr b) 
	{
		return new Zr(a.getValue().add(b.getValue()).mod(r));
	}
	
	//---------------------------------------------------
	// subtraction Zr
	//---------------------------------------------------
	public Zr sub(Zr a, Zr b) 
	{
		return new Zr(a.getValue().subtract(b.getValue()).mod(r));
	}
	
	//---------------------------------------------------
	// multiply G1 with Zr
	//---------------------------------------------------
	public G1 mul(Zr s, G1 P) 
	{
		ECPointFp Q = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp R = curve1.mul(s.getValue(), Q);
		return new G1(R.getX().getValue(), R.getY().getValue());
	}

	//---------------------------------------------------
	// multiply G2 with Zr
	//---------------------------------------------------
	public G2 mul(Zr s, G2 P)
	{
		ECPointFp6 Q = new ECPointFp6(new Fp6(P.getX()), new Fp6(P.getY()));
		ECPointFp6 R = curve2.mul(s.getValue(), Q);
		return new G2(R.getX().getValue(), R.getY().getValue());
	}

	//---------------------------------------------------
	// ec point add
	//---------------------------------------------------
	public G1 add(G1 P, G1 Q) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp _Q = new ECPointFp(new Fp(Q.getX()), new Fp(Q.getY()));
		ECPointFp R = curve1.add(_P, _Q);
		return new G1(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// ec point dob
	//---------------------------------------------------
	public G1 dob(G1 P) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp R = curve1.dob(_P);
		return new G1(R.getX().getValue(),R.getY().getValue());
	}
	
	//---------------------------------------------------
	// ec point add
	//---------------------------------------------------
	public G2 add(G2 P, G2 Q) 
	{
		ECPointFp6 _P = new ECPointFp6(new Fp6(P.getX()), new Fp6(P.getY()));
		ECPointFp6 _Q = new ECPointFp6(new Fp6(Q.getX()), new Fp6(Q.getY()));
		ECPointFp6 R = curve2.add(_P, _Q);
		return new G2(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// ec point dob
	//---------------------------------------------------
	public G2 dob(G2 P) 
	{
		ECPointFp6 _P = new ECPointFp6(new Fp6(P.getX()), new Fp6(P.getY()));
		ECPointFp6 R = curve2.dob(_P);
		return new G2(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// pairing apply
	//---------------------------------------------------
	public Gt pairing(G1 P, G2 Q) 
	{
		ECPointFp  _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp6 _Q = new ECPointFp6(new Fp6(Q.getX()), new Fp6(Q.getY()));
		Fp6 t = curve2.pairing(_P, _Q);
		return new Gt(t.getValue());
	}
	
	//---------------------------------------------------
	// multiplication in Gt
	//---------------------------------------------------
	public Gt mul(Gt a, Gt b) 
	{
		Fp6 _a = new Fp6(a.getValue());
		Fp6 _b = new Fp6(b.getValue());
		Fp6 c = _a.mul(_b);
		return new Gt(c.getValue());
	}
	
	//---------------------------------------------------
	// exponent in Gt
	//---------------------------------------------------
	public Gt pow(Zr s, Gt b) 
	{
		Fp6 a = new Fp6(b.getValue());
		Fp6 c = a.pow(s.getValue());
		return new Gt(c.getValue());
	}
	
	//---------------------------------------------------
	// division in Gt
	//---------------------------------------------------
	public Gt div(Gt a, Gt b) 
	{
		Fp6 _a = new Fp6(a.getValue());
		Fp6 _b = new Fp6(b.getValue());
		_b = _b.inv();
		Fp6 c = _a.mul(_b);
		return new Gt(c.getValue());
	}
}

