package synalp.commons.input.texts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */

public class TextAnalysis 
{
	String txtFile;
	

	public TextAnalysis  (String txtFile )
	{
		this.txtFile= txtFile;
	}
	
	
	public  List<Integer> getOffset(String word) throws IOException
	{

		Path path = new File(txtFile).toPath();
		List<String> lines= Files.readAllLines(path, Charset.defaultCharset());	
		List <Integer> offset = new LinkedList();
		
		int k=1;		
		for (String line : lines)
		{			
			if( line.contains(word))
			{
				k+=line.indexOf(word)	;
				offset.add(k);	
				k-=line.indexOf(word)-1;
			}
				
			k+=line.length();							
		}	
		return offset;
	}
	
//	public int getNBWord()
//	{
//		
//	}
}
