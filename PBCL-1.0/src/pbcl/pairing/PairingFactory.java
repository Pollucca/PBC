package pbcl.pairing;

public class PairingFactory 
{	
	//---------------------------------------
	// Pairing Factory
	//---------------------------------------
	public static Pairing create(String type)
	{
		if( type.equals("fp512ss") ){ return new SuperSingularCurveFp("512"); }
		if( type.equals("fp160od") ){ return new OrdinaryCurveFp("160"); }
		
		System.out.println("error: no such parameter " + type);
		
		return null;
	}
}
