package pbcl.pairing;

import pbcl.group.*;

public interface Pairing {

	//----------------------------
	//	Random 
	//----------------------------
	Zr random_Zr();		// random integer [0 ~ r] 
	
	G1 random_G1();		// random point in G1
	G2 random_G2();		// random point in G2
	
	Zr get_order();		// get order of G1, G2, Gt
	
	//----------------------------
	//	Zr 
	//----------------------------
	Zr mul(final Zr a, final Zr b);
	Zr add(final Zr a, final Zr b);
	Zr sub(final Zr a, final Zr b);

	//----------------------------
	//	G1
	//----------------------------
	G1 add(final G1 P, final G1 Q);
	G1 dob(final G1 P);
	G1 mul(final Zr s, final G1 P);
	
	//----------------------------
	//	G2
	//----------------------------
	G2 add(final G2 P, final G2 Q);
	G2 dob(final G2 P);
	G2 mul(final Zr s, final G2 Q);
	
	//----------------------------
	//	pairing
	//----------------------------
	Gt pairing(final G1 P, final G2 Q);
	
	//----------------------------
	//	Gt
	//----------------------------
	Gt mul(final Gt a, final Gt b);
	Gt pow(final Zr s, final Gt b);
	Gt div(final Gt a, final Gt b);
}
