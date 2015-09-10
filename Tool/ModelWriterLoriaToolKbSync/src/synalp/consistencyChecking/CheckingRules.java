package synalp.consistencyChecking;

import java.io.FileNotFoundException;
import java.util.Set;

import synalp.commons.input.knowledgeBase.OntologyAnalysis;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class CheckingRules 
{
	OntologyAnalysis onto;
	
	public  CheckingRules (String ontoFile) throws FileNotFoundException
	{
		 onto = new OntologyAnalysis(ontoFile);
	}	
	
	// a text word can not be synchronized with a disjoint classes	
	//ex: rigid Component, http://airbus-group.installsys/component#FlexibleComponent
	public boolean disjointCheck(String word, String concept)
	{		
		OntClass resWord= onto.getResource(word);	
		OntClass  cls = onto.getClsOnto(concept);
		
		if(resWord !=null)			
			if( onto.isDisjoint(resWord, cls))				
				return true;
		return false;
	}
	
	// For the individual	
	//ex: rigid Component, http://airbus-group.installsys/component#FlexibleComponent
	public boolean disjointIndividualCheck(String word, String concept)
	{		
		OntClass resWord= onto.getClsOnto(word);	
		OntClass  cls = onto.getClsOnto(concept);
			
		if(resWord !=null)			
			if( onto.isDisjoint(resWord, cls))				
				return true;
			return false;
	}	
}
