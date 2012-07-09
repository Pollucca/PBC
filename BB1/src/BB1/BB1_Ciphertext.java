package BB1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pbcl.group.*;

public class BB1_Ciphertext 
{
	G1	E0;
	G1	E1;
	Zr  B;
	
	public BB1_Ciphertext(){}
	
	public BB1_Ciphertext(String filename) throws IOException
	{
		set_ciphertext(filename);
	}
	
	public void set_ciphertext(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while( (line = br.readLine()) != null ){
			String id = line.substring(0, line.indexOf(' '));
			String data = line.substring(line.indexOf(' ')+1);
			
			if( id.equals("E0") ){ if( E0 == null ) E0 = new G1(); E0.set(data); }
			if( id.equals("E1") ){ if( E1 == null ) E1 = new G1(); E1.set(data); }
			if( id.equals("B") ){ if( B == null ) B = new Zr(); B.set(data); }
		}
		
		br.close();
	}
	
	public void out_ciphertext(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		
		bw.write("E0 " + E0 + "\n");
		bw.write("E1 " + E1 + "\n");
		bw.write("B " + B + "\n");
		
		bw.close();
	}
}
