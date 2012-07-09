package BB1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pbcl.group.*;

public class BB1_MSK 
{	
	public Zr s1;
	public Zr s2;
	public Zr s3;
	
	public BB1_MSK(){}
	
	public BB1_MSK(String filename) throws IOException
	{
		set_msk(filename);
	}
	
	public void set_msk(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		
		while( (line = br.readLine()) != null ){
			String id = line.substring(0, line.indexOf(' '));
			String data = line.substring(line.indexOf(' ')+1);
			
			if( id.equals("s1") ){ if( s1 == null ) s1 = new Zr(); s1.set(data); }
			if( id.equals("s2") ){ if( s2 == null ) s2 = new Zr(); s2.set(data); }
			if( id.equals("s3") ){ if( s3 == null ) s3 = new Zr(); s3.set(data); }
		}
		
		br.close();
	}
	
	public void out_msk(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		
		bw.write("s1 " + s1 + "\n");
		bw.write("s2 " + s2 + "\n");
		bw.write("s3 " + s3 + "\n");
		
		bw.close();
	}
}
