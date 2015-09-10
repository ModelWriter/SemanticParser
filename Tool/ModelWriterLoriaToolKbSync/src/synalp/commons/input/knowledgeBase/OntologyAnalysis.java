
package synalp.commons.input.knowledgeBase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;


import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
/**
 * 
 * @author Mariem Mahfoudh
 * 
 * Class that analayses an ontology and extract its concepts 
 */


public class OntologyAnalysis implements IOntologyAnalysis 
{

	OntModel model ;

    public OntologyAnalysis (String ontoFile) throws FileNotFoundException
    {
    	InputStream   in1 = new FileInputStream(new File(ontoFile));    	
    	model = ModelFactory.createOntologyModel();
 	    model.read(in1,null);	
 		model.prepare();
    }

    // Method that provides the list of the ontology's classes
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getClasses()
	 */
	@Override
	public Set <OntClass>  getClasses()
    {       
       ExtendedIterator<OntClass> listConcept= model.listClasses();
       Set <OntClass>  concepts =new HashSet<OntClass> ();
     
        while(listConcept.hasNext())
        {  
        	OntClass concept =listConcept.next();           	
        	
        	if(concept.getLocalName()!=null)
        		concepts.add(concept);           	
        }
        return concepts;
    }
 
	//Method that creates a text from the label skos definition
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#CreateTextFromDefinition(java.lang.String)
	 */
	@Override
	public void CreateTextFromDefinition(String  fileTextFromKB) throws  IOException
	{
		ExtendedIterator<OntClass> listConcept= model.listClasses();
		BufferedWriter plainTxtFile = new BufferedWriter(new FileWriter(fileTextFromKB));
          
	    while(listConcept.hasNext())
	    {  
	    	OntClass concept =listConcept.next();           	
	        	
	        if(concept.getLocalName()!=null)
	        {
	        	Statement statCls= concept.getProperty(model.getProperty("http://www.w3.org/2004/02/skos/core#definition"));
	       	  	if(statCls!=null )
	       	  	{ 
	       	  		plainTxtFile.write( statCls.getObject().asLiteral().getString());	       	  
	       	  		plainTxtFile.newLine();
	       	  		
	       	  	}
	        }
	    }
	    
	    plainTxtFile.close();        
	}
	
	  
    // Method that returns a class 
    public OntClass getClsOnto (String s)
    {       
        return model.getOntClass(s);      
    }
   
    
    // Method that provides the list of the ontology's datatypesPoperties
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getDatatypeProperties()
	 */
	@Override
	public ExtendedIterator <DatatypeProperty> getDatatypeProperties()
    {     	
	   return model.listDatatypeProperties();
	   
    }
    
  
	// Method that provides the list of the ontology's objectPoperties
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getObjectProperties()
	 */
	@Override
	public ExtendedIterator <ObjectProperty> getObjectProperties()
    { 
    	
    	return model.listObjectProperties();
    }
    
	// Method that provides the list of the ontology's individuals
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getIndividuals()
	 */
	@Override
	public Set <Individual> getIndividuals()
    { 
    	Iterator <OntClass> clsList= getClasses().iterator();
    	Set <Individual> indList = new   HashSet <Individual> ();
    	while( clsList.hasNext())
    	{
    		OntClass cls= clsList.next();
    		
    		if( model.listIndividuals(cls)!= null && model.listIndividuals(cls).toSet().size()!=0)
    		{
    			ExtendedIterator <Individual> ind = model.listIndividuals(cls);
    		
    			while( ind.hasNext())
    			{
    				indList.add(ind.next());
    			}
    		}
    	}
	   return indList; 
    }
    
   // Method that provides the list of all ontology's concepts
   /* (non-Javadoc)
 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getOntoConcepts()
 */
@Override
public Set<Resource> getOntoConcepts()
   {
	   Set <Resource> concepts = new HashSet<Resource> ();	 
	   concepts.addAll(getClasses());
	   concepts.addAll(model.listDatatypeProperties().toSet());
	   concepts.addAll(model.listObjectProperties().toSet());
	   //concepts.addAll(getIndividuals());
	   return concepts;
   }
    
   
  // Method that provides the resources corresponding to a word  
  /* (non-Javadoc)
 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#getResource(java.lang.String)
 */
@Override
public  OntClass getResource (String word)
  {
	  Set <OntClass> ontoElts= getClasses ();

		for(OntClass res :ontoElts)
		{
			if( res.getLocalName().contains(word))	
				return res;
			  
			StmtIterator  iter= res.listProperties(RDFS.label);
			while (iter.hasNext()) 
			{
				Statement stat = iter.next() ;
				String labelRes= stat.getObject().asLiteral().getString().toLowerCase();
				if( labelRes.toLowerCase().contains(word.toLowerCase()))
					return res;
			}
		 }
	  return null;
  }
   
   
    // Method that checks if two classes are disjoint or not 
	/* (non-Javadoc)
	 * @see synalp.commons.input.knowledgeBase.IOntologyAnalysis#isDisjoint(com.hp.hpl.jena.ontology.OntClass, com.hp.hpl.jena.ontology.OntClass)
	 */
	@Override
	public boolean isDisjoint(OntClass c1, OntClass c2)
    {
    	List <OntClass> disjointCls =c1.listDisjointWith().toList();
    	if(disjointCls.contains(c2))
    		return true;
    	else 
    		return false;
    }
   
    
    
    // concept ontology number
    
    public int getNBOntologyConcepts()
    {
    	int nb=0;
    	if(getOntoConcepts() !=null)
    		nb= getOntoConcepts().size();
    	
    	return nb;
    }
}