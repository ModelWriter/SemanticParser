package synalp.commons.statistic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLEditorKit;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import synalp.commons.input.knowledgeBase.OntologyAnalysis;
import synalp.gui.BuildFrame;
import synalp.gui.Interface;
import synalp.synchronization.CreateLink;
import synalp.synchronization.RDFTriple;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class Stats implements ActionListener
{
	private Interface interf;

	public Stats (Interface interf)
	{
		this.interf=interf;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{ 
		BuildFrame frame= new BuildFrame();
		JFrame f = frame.createFrame("Statistics", 500, 300);	        
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));	
		
		JEditorPane jEditorPaneStats = new JEditorPane();
		HTMLEditorKit kit = new HTMLEditorKit();
		jEditorPaneStats.setEditorKit(kit);

		jEditorPaneStats.setBackground(new Color(233,233,233));	
		jEditorPaneStats.setBounds(10,10,450,250);
		
		if( interf.getOntoFile() != null && interf.getTextFile() !=null )
		{		
			try 
			{
				jEditorPaneStats.setText(
						"<html> <font face=\"arial\" size =3>" +
						" <br/> The used technical in matching are :" +
						" <ul> <li> Exact Matching </li>" +
						" <li> Morphology matching using Lemmatization with Stanford CoreNLP </li> " +
						"<li> Semantic matching using preflabel and altLabel of skos</li> </ul>"+
						"Some informations <ul>" + 
								"<li> Number of ontology concepts is: " +getNBConcepts () +"</li>" +
								"<li> Number of text words is: " +getNbWords()+" </li>" +
								"<li> Number of items occurences linked to KB is : "+ getNBOccurence() +"</li>" +
								"<li> Number of items linked to KB  is: " + getNBLink() +"</li>" +
								"<li> Proportion of linked words is: " + getProportionLinkWord() + "%</li>" + 
						"</ul></font></html>");
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		desktopPane.add(jEditorPaneStats);
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add(desktopPane,BorderLayout.CENTER); 			
		f.setVisible(true);
	}

	
	public int getNBConcepts () throws FileNotFoundException 
	{		
		OntologyAnalysis onto  = new OntologyAnalysis (interf.getOntoFile());
		return onto.getNBOntologyConcepts();
	}
	
	
	public int getNbWords() 
	{
		int nbWords=0;
		DocumentPreprocessor docp = new DocumentPreprocessor(interf.getTextFile()); 
		Iterator<List<HasWord>> it = docp.iterator();
		while (it.hasNext() )
		{   
			List<HasWord> listWord = it.next();
			for( int i=0; i< listWord.size(); i++)
			{
				String word =listWord.get(i).toString();
				if( word.equals(".") || word.equals(";") ||word.equals(":")||word.equals(",")
						||word.equals("!") ||word.equals("?")||word.equals("|") ||word.equals("/") 
						||word.equals("``")||word.equals("\"")
						||word.equals("...") ||word.equals("etc")||word.equals("<")||word.equals(">"))
					continue;
					
				nbWords++;
			}
		
		}
		return nbWords;
	}
	
	public int getNBLink() throws IOException
	{		
		InputStream   in1 = new FileInputStream(new File(RDFTriple.T2MFile));    	
   	 	Model model =   ModelFactory.createDefaultModel();
	    model.read(in1,null);
	    
	    return   model.listSubjectsWithProperty(model.getProperty("http://ModelWriter#hasOffset")).toList().size();
		  
	}
	
	public int getNBOccurence() throws IOException
	{
		Path path = new File(CreateLink.txtLinkFile).toPath();
	    List<String> lines= Files.readAllLines(path, Charset.defaultCharset());	
	    return lines.size();		
	}
	
	public String getProportionLinkWord() throws IOException
	{
		DecimalFormat df = new DecimalFormat("##.##");
		float prop= (getNBLink()/(float)getNbWords()) *100 ;
		return df.format(prop);
	}
}
