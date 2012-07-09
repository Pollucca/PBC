package pbcl.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import pbcl.pairing.*;
import pbcl.group.*;

public class PairingTest 
{
	@Test 
	public void testFpSS()
	{
		String type = "fpss512";
		Pairing pairing = PairingFactory.create(type);
		testPairing(pairing, type);
	}
	
	@Test
	public void testFpOD()
	{
		String type = "fpod160";
		testPairing(PairingFactory.create(type), type);
	}
	
	public void testPairing(Pairing e, String title)
	{
		System.out.println("---" + title + "---");
		
		G1 P = e.random_G1();
		G2 Q = e.random_G2();
		
		Zr w = e.random_Zr();
		G1 R = e.mul(w, P);
		G2 V = e.mul(w, Q);
		Gt f = e.pairing(P, Q);
		Gt g = e.pairing(R, Q);
		Gt h = e.pairing(P, V);
		Gt k = e.pow(w, f);
		
		System.out.println("P " + P);
		System.out.println("Q " + Q);
		System.out.println("R " + R);
		System.out.println("e(P,Q) " + f);
		System.out.println("e(R,Q) " + g);
		System.out.println("e(P,V) " + h);
		System.out.println("e^w " + k);
		
		if( !k.equals(h) ){ fail("error"); }
		if( !k.equals(g) ){ fail("error"); }

		long t0, t1, loop = 100;

		Zr s = e.random_Zr();
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ e.mul(s, P); }
		t1 = System.nanoTime();
		
		System.out.println("multiply time G1 " + ((double)t1-t0)/(loop*1000*1000) + "ms");
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ e.mul(s, Q); }
		t1 = System.nanoTime();
		
		System.out.println("multiply time G2 " + ((double)t1-t0)/(loop*1000*1000) + "ms");
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ e.pairing(R, Q); }
		t1 = System.nanoTime();
		
		System.out.println("pairing time " + ((double)t1-t0)/(loop*1000*1000) + "ms");
		
		System.out.println("---fin---");
	}
}
