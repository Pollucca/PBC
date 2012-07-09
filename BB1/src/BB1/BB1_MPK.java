package BB1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pbcl.group.*;

public class BB1_MPK 
{
	G1	Q1;
	G2	Q2;
	G1	R;
	G1	T;
	Gt	V;
	
	public BB1_MPK(){}
	
	public BB1_MPK(String filename) throws IOException
	{
		set_mpk(filename);
	}

	public void set_mpk(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while( (line = br.readLine()) != null ){
			String id = line.substring(0, line.indexOf(' '));
			String data = line.substring(line.indexOf(' ')+1);
			
			if( id.equals("Q1") ){ if( Q1 == null ) Q1 = new G1(); Q1.set(data); }
			if( id.equals("Q2") ){ if( Q2 == null ) Q2 = new G2(); Q2.set(data); }
			if( id.equals("R") ){ if( R == null ) R = new G1(); R.set(data); }
			if( id.equals("T") ){ if( T == null ) T = new G1(); T.set(data); }
			if( id.equals("V") ){ if( V == null ) V = new Gt(); V.set(data); }
		}
		
		br.close();
	}
	
	public void out_mpk(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		
		bw.write("Q1 " + Q1 + "\n");
		bw.write("Q2 " + Q2 + "\n");
		bw.write("R " + R + "\n");
		bw.write("T " + T + "\n");
		bw.write("V " + V + "\n");
		
		bw.close();
	}
}
