package synalp.commons.input.texts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class TextPreProcessing 

{
	private String fileName;
	
	public TextPreProcessing (String fileName)
	{
		this.fileName=fileName;
		
	}
		
	//method that removes the stoplist and lowercase the word
	//Method to use when we would like apply the lenvenshtein similarity search
	public void removeStopList(String fileOutput) throws IOException
	{
		// Text file
		DocumentPreprocessor docpTxt = new DocumentPreprocessor(fileName);
	    docpTxt.setSentenceDelimiter("\n");
	    Iterator<List<HasWord>> itTxt = docpTxt.iterator();    	 	 
	 	
	 	// New file to store the txt after removing the stop words				  
		BufferedWriter parsingFile = new BufferedWriter(new FileWriter(fileOutput));
      	 	
	 	// Removing stop word from the file txt
	 	while(itTxt.hasNext())
	 	{
	 		List<HasWord> txtWord= itTxt.next();
	 		for(int i=0; i< txtWord.size(); i++)
	 		{
	 			HasWord wordTxt= txtWord.get(i);
	 			
	 			boolean isStopWord= false;
	 			// List stop Word
	 		    DocumentPreprocessor docpStopWord = new DocumentPreprocessor("src/synalp/commons/input/stop-word-list.txt");
	 		    docpStopWord.setSentenceDelimiter("\n");	
	 			Iterator<List<HasWord>> itStopWord = docpStopWord.iterator();
	 			while(itStopWord.hasNext())
	 		 	{
	 		 		List<HasWord> stopWord= itStopWord.next();	 		 	
	 		 	
	 		 		if( wordTxt.toString().toLowerCase().equals(stopWord.get(0).toString().toLowerCase()))
	 		 		{
	 		 			isStopWord= true;
	 		 			break;	 		 			
	 		 		}
	 		 	}
	 			if(isStopWord == false)
	 			{
	 				if( i==txtWord.size()-1)	 		 	
	 					parsingFile.write(wordTxt.toString().toLowerCase());
	 		 		else
	 		 			parsingFile.write(wordTxt.toString().toLowerCase()+ " ");	 
	 			}
	 		 }
	 		parsingFile.newLine();	 		
	 	}
	 	parsingFile.close();
	}
	
	
	public void cleanText(String fileOutput) throws IOException
	{
		// Text file
		DocumentPreprocessor docpTxt = new DocumentPreprocessor(fileName);
	    docpTxt.setSentenceDelimiter("\n");
	    Iterator<List<HasWord>> itTxt = docpTxt.iterator();    	 	 
	 	
	 	// New file to store the txt after removing the stop words				  
		BufferedWriter parsingFile = new BufferedWriter(new FileWriter(fileOutput));

	 	while(itTxt.hasNext())
	 	{
	 		List<HasWord> txtWord= itTxt.next();
	 		for(int i=0; i< txtWord.size(); i++)
	 		{
	 			HasWord wordTxt= txtWord.get(i);
	 				if( i==txtWord.size()-1)	 		 	
	 					parsingFile.write(wordTxt.toString());
	 		 		else
	 		 			parsingFile.write(wordTxt.toString()+ " ");	 			
	 		 }
	 			parsingFile.newLine();	 		
	 	}
	 	parsingFile.close();
	}
	
	
	//
    public void lemmatize() throws IOException
    { 
	 	// New file to store the txt after removing the stop words		
    	String parsingFile ="src/synalp/commons/input/corpora/lemmFile.txt";
	 	BufferedWriter lemmFile = new BufferedWriter(new FileWriter(parsingFile));

	 	InputStream ips=new FileInputStream(fileName); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String ligne;
		while ((ligne=br.readLine())!=null)
		{		 	
        	// create an empty Annotation just with the given text
			Annotation document = new Annotation(ligne);

			// run all Annotators on this text        
			Properties props = new Properties();
			props.put("annotators", "tokenize, ssplit, pos, lemma");

			// StanfordCoreNLP loads a lot of models, so you probably
			// only want to do this once per execution
        	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        	pipeline.annotate(document);

        	// Iterate over all of the sentences found
        	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        	for(CoreMap sentence: sentences)
        	{
        		// Iterate over all tokens in a sentence
        		for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
        		{
        			// Retrieve and add the lemma for each word into the list of lemmas
        			System.out.println( token.get(LemmaAnnotation.class)+ "***");
        			lemmFile.write( token.get(LemmaAnnotation.class)+ " ");	 
        		}
            	lemmFile.newLine();	 
        	}
        	System.out.println("fin");
		}
		
		lemmFile.close();
    }
}
