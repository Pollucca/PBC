package BB1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import pbcl.group.*;

public class BB1_Plaintext 
{
	public Zr m;
	
	public BB1_Plaintext()
	{
		m = new Zr(BigInteger.ONE);
	}
	
	public BB1_Plaintext(String str)
	{
		set_plaintext(str);
	}
	
	public void out_plaintext(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		
		bw.write("m " + m + "\n");
		
		bw.close();
	}
	
	public void set_plaintext(String str)
	{
		m = util.convert(str);
	}

	public String get_plaintext() 
	{
		return util.convert(m);
	}
}
