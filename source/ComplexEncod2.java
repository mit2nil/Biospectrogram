/*******************************************************************************
 *	Complex Encoding 2 (E02.3) (Rao and Shepherd, 2004)
 *
 *	Here i is square root of -1
 *
 *	Mapping ...
 *	A --->   1
 *	C --->  -i
 *	G --->  -1
 *	T --->   i
 *
 *	This code is written by Nilay Chheda @ daiict and Ruchin Shah @ daiict
 *	This code was last modified on 10th of September, 2012
 *
 *******************************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class ComplexEncod2
{
	private String filename,t_str,in,out;
	private String[] str;
	private FileWriter writer;
    private FileReader reader;
	private String A=" 1.0+0.0i",C=" 0.0-1.0i",G="-1.0+0.0i",T=" 0.0+1.0i";
	private int temp,space_flag;
	private char[] c;

    public ComplexEncod2(File fasta)
	{
        str = new String[256];
		space_flag = 0;
        in = fasta.getAbsolutePath();
		if((in.contains(".genbank")) == true)
        {
			JOptionPane.showMessageDialog (null,"Encoding of GenBank files are not supported yet !");
		}
        else if((in.contains(".fasta")) == true)
		{
        	out = fasta.getName().substring (0,fasta.getName().lastIndexOf ("."))+"_E02.3.fasta";
            out = "../History/Encoded/"+out;
		}
        else
		{
        	try
            {
				throw new FileNotFoundException();
			}
            catch(FileNotFoundException e)
			{
				e.printStackTrace(System.out);
            	JOptionPane.showMessageDialog(null,"Exception occurred :( ");
			}
		}
	}

	/*******************************************************************************
	 *	Encode method uses the encoding parameter set in the constructor,
	 *	encodes dna sequence according to those parameters and write into
	 *	fasta file at appropriate location.
	 *******************************************************************************/
	protected boolean encode()
    {
		for(int i=0;i<256;i++)
        {
			str[i] = String.valueOf((char)i);
		}
        str[13] = "";
		str[10] = "\n";
        str[65] = " ".concat(this.A);
		str[67] = " ".concat(this.C);
        str[71] = " ".concat(this.G);
		str[84] = " ".concat(this.T);

        try
		{
        	reader = new FileReader(in);
            writer = new FileWriter(out);

			/*******************************************************************************
			 *	This loop traverse to the dna sequence and maps each character to
			 *	predefined comeplex character by character
			*******************************************************************************/
			while(true)
            {
				temp = reader.read();
                if(temp == -1)
					break;
				if(temp==65 || temp==67 || temp==71 || temp==84)
                		space_flag = 1;
				else
                {
					if(space_flag == 1)
                    {
						t_str = " ".concat(str[temp]);
                        writer.write(t_str);
						space_flag = 0;
						continue;
					}
                    space_flag = 0;
				}
                if(temp == 59 || temp == 62)
				{
                	t_str = "";
                    while(temp != 10)
					{
                    	t_str = t_str.concat(String.valueOf((char)temp));
                        temp = reader.read();
					}
                    t_str = t_str.concat("(Mapping: A>"+this.A+" C>"+this.C+" G>"+this.G+" T>"+this.T+")");
                    t_str = t_str.concat("\n");
					writer.write(t_str);
				}
                else
					writer.write(str[temp]);
			}
            writer.close();
			reader.close();
		}
        catch (IOException e)
		{
            e.printStackTrace (System.out);
            JOptionPane.showMessageDialog(null,"Exception occurred :( ");
            return false;
		}
		return true;
	}
}