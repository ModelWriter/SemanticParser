package synalp.changes.Text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


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
import synalp.commons.input.texts.TextAnalysis;
import synalp.gui.BuildFrame;
import synalp.gui.Interface;
import synalp.gui.RenameTextGui;
import synalp.synchronization.CreateLink;
import synalp.synchronization.RDFTriple;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class RenameTextElement  implements ActionListener
{
	RenameTextGui rw;

	List <List <String>> matching = new ArrayList();
	
	Interface interf;
	public RenameTextElement(RenameTextGui rw, Interface interf)
	{
		this.interf=interf;
		this.rw=rw;
	}

	public void actionPerformed(ActionEvent e)
	{
		String selectedText= interf.getJtext().getSelectedText();
		String newTxtElt =rw.getNewWord().getText();
		interf.getJtext().setText(interf.getJtext().getText().replace(selectedText,newTxtElt));		
 
		// remove the old link if exist and update the files of mapping
	    //txtLinkFile
	   	String txtLinkFile= CreateLink.txtLinkFile;
		boolean foundOld = false;
		boolean foundNew = false;
		boolean isExisit= false;
		Path pathLink = new File(txtLinkFile).toPath();
		List<String> lines;
		
		try 
		{
			lines = Files.readAllLines(pathLink, Charset.defaultCharset());
			String fileContenu="";
					
			for (String line : lines)
			{
				if( line.contains(newTxtElt.toLowerCase()))
					isExisit= true;
				if( line.contains(selectedText))
				{					
					foundOld = true;		
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
				serachElementTxt(newTxtElt, listConcepts);
				if(matching!=null && matching.size()!=0 )
				{
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CreateLink.txtLinkFile, true));
					for( List<String> match : matching)
					{	
						bufferedWriter.write(match.get(0)+ " " + match.get(1) + " " + match.get(2));
					}
					bufferedWriter.close();	
					foundNew= true;											
				}
			}
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		//update textFile
		String txtFile= interf.getTextFile();
		Path path = new File(txtFile).toPath();	  
		TextAnalysis  txt = new TextAnalysis (interf.getTextFile());
		List <Integer> offestSelectText= new LinkedList();
		
		try
		{
			Files.write(path, interf.getJtext().getText().getBytes());	
			InputStream in = new FileInputStream(new File(RDFTriple.T2MFile));			
			Model model =   ModelFactory.createDefaultModel();
			model.read(in,null);
		
			if(foundOld)
			{	    
				/**  update RDFTriple.T2MFile*/				
   	 			StmtIterator sts= model.listStatements();
				List <Statement> stats= new <Statement> LinkedList();	
				
				while( sts.hasNext())
				{
					Statement stat= sts.next();
					for(int offset: offestSelectText )   
					{
						if( stat.getSubject().asResource().getURI().
						equals("http://ModelWriter/TxtDocument/"+selectedText+String.valueOf(offset)))
						{
							stats.add(stat);
						}
					}
				}
				model.remove(stats);
				model.write(new FileWriter(RDFTriple.T2MFile), "RDF/XML-ABBREV");
				BuildFrame frame= new BuildFrame();
				frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile);	
			}
			
			if(foundNew)
	 		{
				Property hasOffset = model.getProperty("http://ModelWriter#hasOffset");
				Property hasValue= model.getProperty("http://ModelWriter#hasValue");		
				
	 			for( List<String> match : matching)
	 			{
	 				Property matching= model.createProperty(match.get(1));
	 		
	 				for(int offset: offestSelectText )   
					{	
	 					Resource txtElt = model.createResource("http://ModelWriter/TxtDocument/"+match.get(0)+ String.valueOf(offset));

	 					txtElt.addProperty(hasValue,match.get(0));					
	 					txtElt.addProperty(matching, match.get(2));	 		 			
	 					txtElt.addProperty(hasOffset, String.valueOf(offestSelectText)); 	 				
					}
	 			}
	 			model.write(new FileWriter(RDFTriple.T2MFile), "RDF/XML-ABBREV");				
				BuildFrame frame= new BuildFrame();
		 	   	frame.readFileCheck(interf.getJLink(),RDFTriple.T2MFile);
		 	   	
		 		/** update M2T file*/
					
	 		}			
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
