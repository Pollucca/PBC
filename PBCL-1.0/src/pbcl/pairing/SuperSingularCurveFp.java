package pbcl.pairing;

import java.math.BigInteger;
import java.util.Random;

import pbcl.curve.*;
import pbcl.element.*;
import pbcl.group.*;

public class SuperSingularCurveFp implements Pairing 
{	
	BigInteger q;		// field characteristic
	BigInteger r;		// group order
	BigInteger h;		// cofactor (p+1) = rh
	
	BigInteger a;		// elliptic curve 
	BigInteger b;		// y^2 = x^3 + ax + b
	
	BigInteger B;		// Irreducible polynomial z^2 - B
	
	EllipticCurveFp curve;
	
	public SuperSingularCurveFp(String size)
	{		
		if( size.equals("512") )
		{
			q = new BigInteger("800000000000000004000000000000000000000020000000000000000000000000000000000000000000000000100000000000000000800000000000000000000003",16);
			r = new BigInteger("40000000000000000200000000000000000000001",16);
			h = new BigInteger("20000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004",16);
			a = new BigInteger("1",16);
			b = new BigInteger("0",16);
			B = new BigInteger("1",16);
		}
		
		Fp2.init(q, B.negate());
		
		curve = new EllipticCurveFp(new Fp(a), new Fp(b), r, BigInteger.ZERO);
	}
	
	public Zr get_order(){ return new Zr(r); }
	
	//-------------------------------------------
	// generate random point
	//-------------------------------------------
	public G1 random_G1() 
	{
		ECPointFp P = curve.torsionPoint();
		return new G1(P.getX().getValue(), P.getY().getValue());
	}

	//-------------------------------------------
	// generate random point
	//-------------------------------------------
	public G2 random_G2() 
	{
		ECPointFp P = curve.torsionPoint();		
		return new G2(P.getX().getValue(), P.getY().getValue());
	}

	//---------------------------------------------------
	// generate random integer 0 ~ r
	//---------------------------------------------------
	public Zr random_Zr() 
	{
		BigInteger t = new BigInteger(this.r.bitLength(), new Random());
		return new Zr(t.mod(r));
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
		ECPointFp R = curve.mul(s.getValue(), Q);
		return new G1(R.getX().getValue(), R.getY().getValue());
	}

	//---------------------------------------------------
	// multiply G2 with Zr
	//---------------------------------------------------
	public G2 mul(Zr s, G2 P)
	{
		ECPointFp Q = new ECPointFp(new Fp(P.getX()[0]), new Fp(P.getY()[0]));
		ECPointFp R = curve.mul(s.getValue(), Q);
		return new G2(R.getX().getValue(), R.getY().getValue());
	}

	//---------------------------------------------------
	// pairing apply
	//---------------------------------------------------
	public Gt pairing(G1 P, G2 Q) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp _Q = new ECPointFp(new Fp(Q.getX()[0]), new Fp(Q.getY()[0]));
		
		Fp2 t = curve.pairing(_P, _Q);
		
		return new Gt(t.getValue());
	}

	//---------------------------------------------------
	// ec point add
	//---------------------------------------------------
	public G1 add(G1 P, G1 Q) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp _Q = new ECPointFp(new Fp(Q.getX()), new Fp(Q.getY()));
		ECPointFp R = curve.add(_P, _Q);
		return new G1(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// ec point dob
	//---------------------------------------------------
	public G1 dob(G1 P) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()), new Fp(P.getY()));
		ECPointFp R = curve.dob(_P);
		return new G1(R.getX().getValue(),R.getY().getValue());
	}
	
	//---------------------------------------------------
	// ec point add
	//---------------------------------------------------
	public G2 add(G2 P, G2 Q) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()[0]), new Fp(P.getY()[0]));
		ECPointFp _Q = new ECPointFp(new Fp(Q.getX()[0]), new Fp(Q.getY()[0]));
		ECPointFp R = curve.add(_P, _Q);
		return new G2(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// ec point dob
	//---------------------------------------------------
	public G2 dob(G2 P) 
	{
		ECPointFp _P = new ECPointFp(new Fp(P.getX()[0]), new Fp(P.getY()[0]));
		ECPointFp R = curve.dob(_P);
		return new G2(R.getX().getValue(),R.getY().getValue());
	}

	//---------------------------------------------------
	// multiplication in Gt
	//---------------------------------------------------
	public Gt mul(Gt a, Gt b) 
	{
		Fp2 _a = new Fp2(new Fp(a.getValue()[0]),new Fp(a.getValue()[1]));
		Fp2 _b = new Fp2(new Fp(b.getValue()[0]),new Fp(b.getValue()[1]));
		Fp2 c = _a.mul(_b);
		return new Gt(c.getValue());
	}
	
	//---------------------------------------------------
	// exponent in Gt
	//---------------------------------------------------
	public Gt pow(Zr s, Gt b) 
	{
		Fp2 a = new Fp2(new Fp(b.getValue()[0]),new Fp(b.getValue()[1]));
		Fp2 c = a.pow(s.getValue());
		return new Gt(c.getValue());
	}
	
	//---------------------------------------------------
	// division in Gt
	//---------------------------------------------------
	public Gt div(Gt a, Gt b) 
	{
		Fp2 _a = new Fp2(new Fp(a.getValue()[0]),new Fp(a.getValue()[1]));
		Fp2 _b = new Fp2(new Fp(b.getValue()[0]),new Fp(b.getValue()[1]));
		_b = _b.inv();
		Fp2 c = _a.mul(_b);
		return new Gt(c.getValue());
	}
}
