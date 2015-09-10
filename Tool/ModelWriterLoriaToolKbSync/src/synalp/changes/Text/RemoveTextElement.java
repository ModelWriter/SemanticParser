package synalp.changes.Text;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import synalp.commons.input.texts.TextAnalysis;
import synalp.gui.BuildFrame;
import synalp.gui.Interface;
import synalp.synchronization.CreateLink;
import synalp.synchronization.RDFTriple;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class RemoveTextElement extends JFrame implements ActionListener
{
	Interface interf;
	
	public RemoveTextElement(Interface interf)
	{
		this.interf=interf;		
	}	
	
	public void actionPerformed(ActionEvent e)
	{    
		String selectedText= interf.getJtext().getSelectedText();	
		interf.getJtext().setText(interf.getJtext().getText().replace(selectedText,""));
		
		
		int txtOffset = -1;
				
		//remove the old link if exist 	
		/**RDFTriple.T2MFile*/
		InputStream in1;
		boolean found= false;
		try
		{
			TextAnalysis txt= new TextAnalysis(interf.getTextFile());		
			txtOffset= txt.getOffset(selectedText).get(0);
			in1 = new FileInputStream(new File(RDFTriple.T2MFile));			
   	 		Model model =   ModelFactory.createDefaultModel();
   	 		model.read(in1,null);
   	 		StmtIterator sts= model.listStatements();
   	 		
   	 		List <Statement> stats= new <Statement> LinkedList();
   	 		while( sts.hasNext())
   	 		{
	 			Statement stat= sts.next();
	 			if( stat.getSubject().asResource().getURI().equals("http://ModelWriter/TxtDocument/"+selectedText+txtOffset ))
	 			{
	 				stats.add(stat);
	 				found= true;
	 			}
	 		}	    
    
   	 		model.remove(stats);
	 		model.write(new FileWriter(RDFTriple.T2MFile), "RDF/XML-ABBREV");	
	 		BuildFrame frame= new BuildFrame();
   	 		frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile);
		}
	 	catch (IOException e1) 
		{
			e1.printStackTrace();			
		}	
		
		if(found)
		{
			/**RDFTriple.M2TFile*/				
			try
			{
				in1 = new FileInputStream(new File(RDFTriple.M2TFile));		   	
	   	 		Model 	model =   ModelFactory.createDefaultModel();
	   	 		model.read(in1,null);		    		  
		    
	   	 		StmtIterator sts= model.listStatements();
	   	 		List <Statement> stats= new <Statement> LinkedList();
	   	 		
	   	 		while( sts.hasNext())
	   	 		{
	   	 			Statement stat= sts.next();
	   	 			if(  stat.getObject().asLiteral().getString().equals(selectedText) )
	   	 			{
	   	 				stats.add(stat);
	   	 				String modelElt= stat.getSubject().asResource().getURI();	   	 				
	   	 			}
	   	 		}
	   	 		model.remove(stats);
				model.write(new FileWriter(RDFTriple.M2TFile), "RDF/XML-ABBREV");			
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			/** update CreateLink.txtLinkFile*/		
			Path path = new File(CreateLink.txtLinkFile).toPath();
			List<String> lines;
			try
			{
				lines = Files.readAllLines(path, Charset.defaultCharset());
				String fileContenu= "";
				for (String line : lines)
				{				
					if(line.contains(" "+selectedText+ " "))				
						continue;
					fileContenu= fileContenu.concat(line + "\n");
				}	
				Files.write(path, fileContenu.getBytes());	
			} catch (IOException e1) {e1.printStackTrace();	}
		}
		
		//save the new text
		String txtFile= interf.getTextFile();
		Path path = new File(txtFile).toPath();		
		try
		{
			Files.write(path, interf.getJtext().getText().getBytes());			
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}					
	}
}

