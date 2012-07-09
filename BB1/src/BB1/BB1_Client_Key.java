package BB1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pbcl.group.*;

public class BB1_Client_Key 
{	
	Zr	M;	// public key
	
	G2	K0;	// secret key 1
	G2	K1;	// secret key 2
	
	public BB1_Client_Key(){}
	
	public BB1_Client_Key(String filename) throws IOException
	{
		set_client_key(filename);
	}
	
	public void set_client_key(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while( (line = br.readLine()) != null ){
			String id = line.substring(0, line.indexOf(' '));
			String data = line.substring(line.indexOf(' ')+1);
			
			if( id.equals("M") ){ if( M == null ) M = new Zr(); M.set(data); }
			if( id.equals("K0") ){ if( K0 == null ) K0 = new G2(); K0.set(data); }
			if( id.equals("K1") ){ if( K1 == null ) K1 = new G2(); K1.set(data); }
		}
		
		br.close();
	}
	
	public void out_client_key(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		
		bw.write("M " + M + "\n");
		bw.write("K0 " + K0 + "\n");
		bw.write("K1 " + K1 + "\n");
		
		bw.close();
	}
}
