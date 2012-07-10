import java.io.IOException;

import BB1.BB1;
import BB1.BB1_Ciphertext;
import BB1.BB1_Client_Key;
import BB1.BB1_MPK;
import BB1.BB1_MSK;
import BB1.BB1_Plaintext;

public class BB1Sample 
{	
	public static void main(String[] args) throws IOException 
	{
		BB1Sample bb1sample = new BB1Sample();
		
		bb1sample.test_gen_msk_mpk();
		bb1sample.test_gen_client_key();
		
		bb1sample.test_encrypt();
		bb1sample.test_decrypt();
		
		bb1sample.test_speed();
	}
	
	//=============================
	// Generate Master Key
	//=============================
	public void test_gen_msk_mpk() throws IOException 
	{
		System.out.println("---BB1 generate master key---");
		
		BB1 bb1 = new BB1("fp160od");
		
		BB1_MSK msk = bb1.gen_msk();
		BB1_MPK mpk = bb1.gen_mpk(msk);
		
		msk.out_msk("bb1/msk.param");
		mpk.out_mpk("bb1/mpk.param");
	}
	
	//=============================
	// Generate Client Key
	//=============================
	public void test_gen_client_key() throws IOException 
	{
		System.out.println("---BB1 generate client key---");
		
		BB1 bb1 = new BB1("fp160od");

		BB1_MSK msk = new BB1_MSK("bb1/msk.param");
		BB1_MPK mpk = new BB1_MPK("bb1/mpk.param");
		
		BB1_Client_Key ckey = bb1.gen_client_key("hoge", mpk, msk);
		
		ckey.out_client_key("bb1/client.key");
	}
	
	//=============================
	// ENCRYPT
	//=============================
	public void test_encrypt() throws IOException 
	{
		System.out.println("---BB1 encrypt---");
		
		BB1 bb1 = new BB1("fp160od");
		
		BB1_MPK mpk = new BB1_MPK("bb1/mpk.param");
		
		String plaintext = "sample string";
		
		System.out.println("plain text : " + plaintext);
			
		BB1_Plaintext P = new BB1_Plaintext(plaintext);
			
		BB1_Ciphertext C = bb1.encrypt(mpk, "hoge", P);
			
		C.out_ciphertext("bb1/cipher.txt");
	}
	
	//=============================
	// DECRYPT
	//=============================
	public void test_decrypt() throws IOException
	{
		System.out.println("---BB1 decrypt---");
		
		BB1 bb1 = new BB1("fp160od");

		BB1_MPK mpk = new BB1_MPK("bb1/mpk.param");

		BB1_Client_Key ckey = new BB1_Client_Key("bb1/client.key");

		BB1_Ciphertext C = new BB1_Ciphertext("bb1/cipher.txt");

		BB1_Plaintext P = bb1.decrypt(mpk, ckey, C);

		P.out_plaintext("bb1/plain.txt");

		System.out.println("decrypted text : " + P.get_plaintext());
	}
	
	//=============================
	// Measurement of calculation
	//=============================
	public void test_speed() throws IOException
	{
		System.out.println("---BB1 calculation speed---");
		
		long t0, t1, loop = 10;
		
		BB1 bb1 = new BB1("fp160od");
			
		BB1_MSK msk = new BB1_MSK("bb1/msk.param");
		BB1_MPK mpk = new BB1_MPK("bb1/mpk.param");
		
		BB1_Client_Key ckey = new BB1_Client_Key("bb1/client.key");
		
		BB1_Plaintext P = new BB1_Plaintext();
		BB1_Ciphertext C = new BB1_Ciphertext("bb1/cipher.txt");
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ bb1.gen_msk(); bb1.gen_mpk(msk); }
		t1 = System.nanoTime();
		System.out.println("gen msk mpk time " + (double)(t1-t0)/(1000*1000*loop) + "ms");
					
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ bb1.gen_client_key("hoge", mpk, msk); }
		t1 = System.nanoTime();
		System.out.println("gen client key time " + (double)(t1-t0)/(1000*1000*loop) + "ms");
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ bb1.encrypt(mpk, "hoge", P); }
		t1 = System.nanoTime();
		System.out.println("encrypt time " + (double)(t1-t0)/(1000*1000*loop) + "ms");
		
		t0 = System.nanoTime();
		for(int i=0;i<loop;i++){ bb1.decrypt(mpk, ckey, C); }
		t1 = System.nanoTime();
		System.out.println("decrypt time " + (double)(t1-t0)/(1000*1000*loop) + "ms");
	}
}
