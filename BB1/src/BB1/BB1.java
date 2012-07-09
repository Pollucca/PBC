package BB1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pbcl.pairing.*;
import pbcl.group.*;

public class BB1 
{
	Pairing e;
	
	//----------------------------------
	//	Constructor
	//----------------------------------
	public BB1(String filename)
	{
		e = PairingFactory.create(filename);
	}
	
	//----------------------------------
	//	Generate Master Public Key
	//----------------------------------
	public BB1_MSK gen_msk()
	{
		BB1_MSK msk = new BB1_MSK();
		msk.s1 = e.random_Zr();
		msk.s2 = e.random_Zr();
		msk.s3 = e.random_Zr();
		return msk;
	}
	
	//----------------------------------
	//	Generate Master Secret Key
	//----------------------------------
	public BB1_MPK gen_mpk(BB1_MSK msk)
	{
		BB1_MPK mpk = new BB1_MPK();
		mpk.Q1 = e.random_G1();
		mpk.Q2 = e.random_G2();
		mpk.R = e.mul(msk.s1, mpk.Q1);
		mpk.T = e.mul(msk.s3, mpk.Q1);
		G2 _V = e.mul(msk.s2, mpk.Q2);
		mpk.V = e.pairing(mpk.R, _V);
		return mpk;
	}
	
	//----------------------------------
	//	to Hex String
	//----------------------------------
	private String toHexString(byte[] digest)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<digest.length;i++){
			int d = digest[i];
			if( d < 0 ) d += 256; 
			if( d < 16 ) sb.append("0");
			sb.append(Integer.toHexString(d));
		}
		return sb.toString();
	}
	
	//----------------------------------
	//	Generate Public Key
	//----------------------------------
	public Zr gen_client_pubkey(String id)
	{
		try {
			Zr M = new Zr();
			
			MessageDigest md = MessageDigest.getInstance("SHA1");
		    md.update(id.getBytes());
		    M.set(toHexString(md.digest()));
		    
		    return M;
		    		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//----------------------------------
	//	Generate Client Secret Key
	//----------------------------------
	public BB1_Client_Key gen_client_seckey(BB1_MPK mpk, BB1_MSK msk, Zr M)
	{
		BB1_Client_Key ckey = new BB1_Client_Key();
		
		ckey.M = M;
		
		Zr i, j, r;
		
		r = e.random_Zr();
		i = e.mul(msk.s1, M);
		i = e.add(i, msk.s3);
		i = e.mul(i, r);
		j = e.mul(msk.s1, msk.s2);
		i = e.add(i, j);
		ckey.K0 = e.mul(i, mpk.Q2);
		ckey.K1 = e.mul(r, mpk.Q2);
		
		return ckey;
	}
	
	//----------------------------------
	//	Generate Client Key
	//----------------------------------
	public BB1_Client_Key gen_client_key(String id, BB1_MPK mpk, BB1_MSK msk)
	{
		Zr M = gen_client_pubkey(id);
		BB1_Client_Key ckey = gen_client_seckey(mpk, msk, M);
		return ckey;
	}
	
	//----------------------------------
	//	Encrypt
	//----------------------------------
	public BB1_Ciphertext encrypt(BB1_MPK mpk, String id, BB1_Plaintext P)
	{
		BB1_Ciphertext C = new BB1_Ciphertext();
		
		Zr M = gen_client_pubkey(id);
		Zr r = e.random_Zr();
		C.E0 = e.mul(r, mpk.Q1);
		Zr x1 = e.mul(r, M);
		G1 x2 = e.mul(x1, mpk.R);
		G1 x3 = e.mul(r, mpk.T);
		C.E1 = e.add(x2, x3);
		Gt B = e.pow(r, mpk.V);
		C.B = util.xor(util.convert(B), P.m);
		
		return C;
	}
	
	//----------------------------------
	//	Decrypt
	//----------------------------------
	public BB1_Plaintext decrypt(BB1_MPK mpk, BB1_Client_Key ckey, BB1_Ciphertext C)
	{
		BB1_Plaintext P = new BB1_Plaintext();
		
		Gt e1 = e.pairing(C.E0, ckey.K0);
		Gt e2 = e.pairing(C.E1, ckey.K1);
		Gt B = e.div(e1, e2);
		P.m = util.xor(util.convert(B), C.B);
		
		return P;
	}
}
