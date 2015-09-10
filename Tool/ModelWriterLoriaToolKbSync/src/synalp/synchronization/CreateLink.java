package synalp.synchronization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import synalp.commons.input.knowledgeBase.IOntologyAnalysis;
import synalp.commons.input.knowledgeBase.OntologyAnalysis;

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

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class CreateLink 
{
	IOntologyAnalysis  onto;
	private String txtFile;

	public static String txtLinkFile= "src/synalp/commons/output/resultLink.txt";
	
	private List <List <String>>matching;
	public CreateLink(String txtFile, String KBFile) throws IOException
	{  
 		onto= new OntologyAnalysis (KBFile);		
 		this.txtFile=txtFile;		
		linkKBTxt() ; 
		createlinkFile();
	}
		
	public void linkKBTxt() 
	{
		//execution time
		long start = System.currentTimeMillis() ;	
 	
 		Path path = new File(txtFile).toPath();
 		
 		matching = new ArrayList <List <String>>();
 		HashMap <String, String> 	dict = new HashMap<String, String>();
	  
		try
		{
			List<String> lines = Files.readAllLines(path, Charset.defaultCharset());	
			Iterator <Resource> listConcepts= onto.getOntoConcepts().iterator(); 
			
			Model model = ModelFactory.createDefaultModel();
			
			while (listConcepts.hasNext())
			{
				Resource res = listConcepts.next();  
				search(res, RDFS.label, dict, lines) ;
				search(res, model.getProperty("http://www.w3.org/2004/02/skos/core#altLabel"), dict, lines) ;			
				search(res, model.getProperty("http://www.w3.org/2004/02/skos/core#prefLabel"), dict, lines) ;
			} 
    	  
			long time = System.currentTimeMillis()- start;
			System.out.println((time/ 1000 / 60 / 60) % 24 + ":" + (time / 1000 / 60) % 60 + ":" + (time / 1000) % 60);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public void search(Resource res, Property prop, HashMap <String, String> dict,  List<String> lines) throws IOException
	{
		StmtIterator iter= res.listProperties(prop); 
		String nameRes= res.getLocalName().toLowerCase();
			
		while (iter.hasNext()) 
		{
			Statement stat = iter.next() ;
			String labelRes= stat.getObject().asLiteral().getString();	
			
			for (int i= 0; i< lines.size(); i++)
			{
				String line = lines.get(i);
			
				if(line.toLowerCase().contains(labelRes.toLowerCase()))
				{				
					int indexLabel= line.toLowerCase().indexOf(labelRes.toLowerCase());
					String word = line.substring(indexLabel,indexLabel+labelRes.length());
					boolean found= false;
					
					Iterator <String> keySetIterator = dict.keySet().iterator();
					while (keySetIterator.hasNext())
					{ 
						String label=  keySetIterator.next();
						if(label.toLowerCase().equals(labelRes.toLowerCase())
								&& dict.get(label).equals(res.getURI()))
            				found = true ;		            		
            		}
				
					if(!found)
					{
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
						dict.put(word, res.getURI());
					}
				}
				
				// lemmatization
				if( prop.equals(RDFS.label))
				{
				// create an empty Annotation just with the given text
				
        		Annotation document = new Annotation(line);

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
                				
                			Iterator <String> keySetIterator = dict.keySet().iterator();
                			while (keySetIterator.hasNext())
            				{ 
            					String label=  keySetIterator.next();
            					if(label.toLowerCase().equals(token.word().toLowerCase())
            							&&   dict.get(token.word()).equals(res.getURI()))
            						
                        			found =true;	
                        	}
                				
                			if(!found)
                			{
                				List <String> match = new ArrayList<String>();
                				match.add(token.word()); match.add("isMorphologySimilarTo"); match.add(res.getURI());
        						matching.add(match);
        						dict.put(token.word(), res.getURI());
                			}
                		}
                	}
                }  
			}	
			}
		}
	}

	public List<List<String>> getMatching() {
		return matching;
	}	
	
	
	public void createlinkFile() throws IOException
	{
		BufferedWriter file = new BufferedWriter( new FileWriter(txtLinkFile));
		
		for( List<String> match : matching)
		{			
			file.write(match.get(0)+ " " + match.get(1) + " " + match.get(2));			
			file.newLine();
		}
		file.close();
	}	
}
