package synalp.synchronization;

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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import synalp.gui.BuildFrame;
import synalp.gui.Interface;
import synalp.gui.RemoveLinkGui;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */

public class RemoveLink implements ActionListener
{
	Interface interf;
	RemoveLinkGui rLink;
	
	public RemoveLink(RemoveLinkGui rLink, Interface interf)
	{
		this.interf= interf;
		this.rLink=rLink;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		int idSelectItem= rLink.getComboBox().getSelectedIndex();
		String link = rLink.getComboBox().getItemAt(idSelectItem);
		String txtValue= link.substring(link.indexOf("(")+1, link.indexOf(","));
		String txtOffset= link.substring(link.indexOf(",")+2, link.indexOf(")"));
		String eltModel= link.substring(link.indexOf("Model")+7, link.length()-1);
		
		//update the files 	
		/**RDFTriple.T2MFile*/
		InputStream in1;
		try
		{
			in1 = new FileInputStream(new File(RDFTriple.T2MFile));			
   	 		Model model =   ModelFactory.createDefaultModel();
   	 		model.read(in1,null);
	    
   	 		StmtIterator sts= model.listStatements();
   	 		List <Statement> stats= new <Statement> LinkedList();
   	 		while( sts.hasNext())
   	 		{
   	 			Statement stat= sts.next();
   	 			if( stat.getSubject().asResource().getURI().equals("http://ModelWriter/TxtDocument/"+txtValue+String.valueOf(txtOffset)))
   	 			{
   	 				stats.add(stat);
   	 			}
   	 		}
   	 		model.remove(stats);
   	 		rLink.getComboBox().removeItemAt(idSelectItem);	
   	 		rLink.getDesktopPane().validate();
   	 		model.write(new FileWriter(RDFTriple.T2MFile), "RDF/XML-ABBREV");			
   	 		BuildFrame frame= new BuildFrame();
   	 		frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile);
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
	  
 	   	
		/**RDFTriple.M2TFile*/				
		try
		{
			in1 = new FileInputStream(new File(RDFTriple.M2TFile));		   	
   	 		Model 	model =   ModelFactory.createDefaultModel();
   	 		model.read(in1,null);	    		  
	    
   	 		List <Statement> stats= new <Statement> LinkedList();
   	 		StmtIterator sts= model.listStatements();
   	 		while( sts.hasNext())
   	 		{
   	 			Statement stat= sts.next();
   	 			if( stat.getSubject().asResource().getURI().equals(eltModel) )
   	 			{
   	 				stats.add(stat);
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
				if(line.contains(txtValue) && line.contains(eltModel))				
					continue;
				fileContenu= fileContenu.concat(line + "\n");
			}	
			Files.write(path, fileContenu.getBytes());	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}	
}
