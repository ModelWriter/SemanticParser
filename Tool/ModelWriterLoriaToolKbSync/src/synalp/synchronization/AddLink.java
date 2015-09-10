package synalp.synchronization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import synalp.commons.input.texts.TextAnalysis;
import synalp.consistencyChecking.CheckingRules;
import synalp.gui.AddLinkGui;
import synalp.gui.BuildFrame;
import synalp.gui.Interface;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class AddLink implements ActionListener
{

	Interface interf;
	AddLinkGui  addLGui;
	
	public AddLink(AddLinkGui addLGui, Interface interf)
	{
		this.interf=interf;
		this.addLGui= addLGui;		
	}
	
	
	
	public void actionPerformed (ActionEvent e)
	{
		
		String word =addLGui.getWord().getText();		
		String concept = addLGui.getConcept().getText();		
		
		try 
		{
			CheckingRules rules =  new CheckingRules (interf.getOntoFile());
			if(rules.disjointCheck(word, concept)) 
				JOptionPane.showMessageDialog(null, "These two element cannot be linked. There are disjoint concepts.");
			else	
			{
				//update the files 	
				/**RDFTriple.T2MFile*/
				InputStream   in1 = new FileInputStream(new File(RDFTriple.T2MFile));    	
		   	 	Model model =   ModelFactory.createDefaultModel();
			    model.read(in1,null);
			    
			    TextAnalysis txt= new TextAnalysis(interf.getTextFile());
			    
				Resource txtElt = model.createResource("http://ModelWriter/TxtDocument/"+word+ String.valueOf(txt.getOffset(word)));
							    
				Property hasOffset = model.getProperty("http://ModelWriter#hasOffset");
				Property matching= model.createProperty("http://ModelWriter# userLink");
				Property hasValue= model.createProperty("http://ModelWriter#hasValue");	
				
				txtElt.addProperty(hasValue,word);					
				txtElt.addProperty(matching, concept);
				txtElt.addProperty(hasOffset, String.valueOf(txt.getOffset(word)));
				
				model.write(new FileWriter(RDFTriple.T2MFile), "RDF/XML-ABBREV");	
				
				BuildFrame frame= new BuildFrame();
		 	   	frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile);
		 	   	
				/**RDFTriple.M2TFile*/				
				in1 = new FileInputStream(new File(RDFTriple.M2TFile));    	
		   	 	model =   ModelFactory.createDefaultModel();
			    model.read(in1,null);		    		  
			    
			    Resource modelElt = model.createResource(concept);					
				Property offset = model.createProperty("http://ModelWriter#TxtEltOffset");		
				modelElt.addProperty(matching, word);
				modelElt.addProperty(offset, String.valueOf(txt.getOffset(word)));
				
				model.write(new FileWriter(RDFTriple.M2TFile), "RDF/XML-ABBREV");				
				
				/** update CreateLink.txtLinkFile*/				
				String tuplet= word + " relatedByLinkedUserTo " + concept;				
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CreateLink.txtLinkFile, true));
				bufferedWriter.write(tuplet);
				bufferedWriter.close();		
			}						
		} 
		catch ( IOException e1)
		{
			e1.printStackTrace();
		}		
	}	
}
