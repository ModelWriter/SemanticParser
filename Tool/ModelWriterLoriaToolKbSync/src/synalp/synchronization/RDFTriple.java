package synalp.synchronization;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class RDFTriple 
{
	
	public static String M2TFile= "src/synalp/commons/output/model2TxtMatching.rdf";
	public static String T2MFile= "src/synalp/commons/output/txt2ModelMatching.rdf";
	private String txtFile;
	private List <List <String>>links ;
	
	public RDFTriple(String txtFile, List <List <String>>links) throws IOException
	{	
		this.txtFile=txtFile;
		this.links=links;
		modelToTxtMatching();
		txtToModelMatching();		
	}
	
	public void modelToTxtMatching() throws IOException
	{
		Path path = new File(txtFile).toPath();
		List<String> lines= Files.readAllLines(path, Charset.defaultCharset());			
		Model model = ModelFactory.createDefaultModel();	
		
		int k=1;		
		for (String line : lines)
		{
			for( int i=0; i< links.size(); i++)
			{
				List<String> link= links.get(i);
				if( line.contains(link.get(0)))
				{	
					k+=line.indexOf(link.get(0));
					
					Resource modelElt = model.createResource(link.get(2));					
					Property hasOffset = model.createProperty("http://ModelWriter#TxtEltOffset");
					Property matching= model.createProperty("http://ModelWriter#"+ link.get(1));			
					modelElt.addProperty(matching, link.get(0));
					modelElt.addProperty(hasOffset, String.valueOf(k));						
					k-=line.indexOf(link.get(0));
				}				
			}	
			k+=line.length();
		}
		model.write(new FileWriter(M2TFile), "RDF/XML-ABBREV");		
	}
	
	public void txtToModelMatching() throws IOException
	{
		Path path = new File(txtFile).toPath();
		List<String> lines= Files.readAllLines(path, Charset.defaultCharset());			
		Model model = ModelFactory.createDefaultModel();	
		
		int k=1;		
		for (String line : lines)
		{	
			for( int i=0; i< links.size(); i++)
			{
				List<String> link= links.get(i);
				if( line.contains(link.get(0)))
				{	
					k+=line.indexOf(link.get(0));
					
					Resource txtElt = model.createResource("http://ModelWriter/TxtDocument/"+link.get(0)+ String.valueOf(k));
					
					Property hasOffset = model.createProperty("http://ModelWriter#hasOffset");
					Property matching= model.createProperty("http://ModelWriter#"+ link.get(1));
					Property hasValue= model.createProperty("http://ModelWriter#hasValue");	
					
					txtElt.addProperty(hasValue,link.get(0));					
					txtElt.addProperty(matching, link.get(2));
					txtElt.addProperty(hasOffset, String.valueOf(k));
									
					k-=line.indexOf(link.get(0));
				}				
			}	
			k+=line.length();
		}
		model.write(new FileWriter(T2MFile), "RDF/XML-ABBREV");			
	}
}
