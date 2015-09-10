package synalp.changes.Text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import synalp.commons.input.knowledgeBase.IOntologyAnalysis;
import synalp.commons.input.knowledgeBase.OntologyAnalysis;
import synalp.gui.Interface;
import synalp.synchronization.CreateLink;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */

public class AddTextElement extends JFrame implements ActionListener
{
	
	Interface interf;

	List <List <String>> matching = new ArrayList();
	
	public AddTextElement(Interface interf)
	{
		this.interf=interf;
	}

	public void actionPerformed(ActionEvent e)
	{	
		//update the text
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
				
		//search and create new link if exist
		String selectedText= interf.getJtext().getSelectedText();	
				
		/** update CreateLink.txtLinkFile*/		
		path = new File(CreateLink.txtLinkFile).toPath();
		List<String> lines;
		boolean isExisit= false;
		boolean found =false;
	   	String txtLinkFile= CreateLink.txtLinkFile;
		Path pathLink = new File(txtLinkFile).toPath();
		try
		{
			lines = Files.readAllLines(path, Charset.defaultCharset());
			String fileContenu="";
			
			for (String line : lines)
			{				
				if(line.contains(selectedText))
				{					
					isExisit= true;
					continue;
				}			
				else 
					fileContenu= fileContenu.concat(line + "\n");	
			}	
			Files.write(pathLink, fileContenu.getBytes());	
			
			//search a new link				
			if(!isExisit)
			{
				IOntologyAnalysis 	onto= new OntologyAnalysis (interf.getOntoFile());	
				Iterator <Resource> listConcepts= onto.getOntoConcepts().iterator(); 
				serachElementTxt(selectedText, listConcepts);
				if(matching!=null && matching.size()!=0 )
				{
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CreateLink.txtLinkFile, true));
					for( List<String> match : matching)
					{	
						bufferedWriter.write(match.get(0)+ " " + match.get(1) + " " + match.get(2));
					}
					bufferedWriter.close();	
					found= true;											
				}
			}
			Files.write(path, interf.getJtext().getText().getBytes());
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
	}

	public void serachElementTxt(String txtElt, Iterator <Resource> listConcepts ) throws IOException
	{	
		Model model = ModelFactory.createDefaultModel();
		
		while (listConcepts.hasNext())
		{
			Resource res = listConcepts.next();  
			search(res, RDFS.label, txtElt) ;
			search(res, model.getProperty("http://www.w3.org/2004/02/skos/core#altLabel"),txtElt) ;			
			search(res, model.getProperty("http://www.w3.org/2004/02/skos/core#prefLabel"), txtElt) ;
		}
	}

	

	public void search(Resource res, Property prop,  String txtElt) throws IOException
	{
		StmtIterator iter= res.listProperties(prop); 
		String nameRes= res.getLocalName().toLowerCase();
		
		while (iter.hasNext()) 
		{
			Statement stat = iter.next() ;
			String labelRes= stat.getObject().asLiteral().getString();	
		
			if(txtElt.toLowerCase().contains(labelRes.toLowerCase()))
			{				
				int indexLabel= txtElt.toLowerCase().indexOf(labelRes.toLowerCase());
				String word = txtElt.substring(indexLabel,indexLabel+labelRes.length());
				
				List <String> match = new ArrayList<String>();
				String space= word.toLowerCase();
				space= space.replaceAll(" ", "");
					
				if(space.equals(nameRes.toLowerCase()))
				{
					match.add(word); match.add("isSameAs"); match.add(res.getURI());
					matching.add(match);
				}
				else 
				{
					match.add(word); match.add("isSynonymTo"); match.add(res.getURI());
					matching.add(match);
				}
					
			}
			
			// lemmatization
			if( prop.equals(RDFS.label))
			{
				// create an empty Annotation just with the given text			
				Annotation document = new Annotation(txtElt);

				// run all Annotators on this text        
				Properties props = new Properties();
				props.put("annotators", "tokenize, ssplit, pos, lemma");

				// StanfordCoreNLP loads a lot of models
				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				pipeline.annotate(document);

				// Iterate over all of the sentences found
				List<CoreMap> sentences = document.get(SentencesAnnotation.class);                	
            	
				for (CoreMap sentence: sentences) 
				{
					// Iterate over all tokens in a sentence                  	
					for(CoreLabel token: sentence.get(TokensAnnotation.class)) 
					{
						// Retrieve and add the lemma for each word into the list of lemmas
						if( token.lemma().equals(labelRes.toLowerCase()))
						{
							boolean found = false;
            				
							if(!found)
            				{
								List <String> match = new ArrayList<String>();
								match.add(token.word()); match.add("isMorphologySimilarTo"); match.add(res.getURI());    	
            				}
						}
					}           	
				}
			}
		}
	}
}
