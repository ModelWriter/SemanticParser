package synalp.synchronization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

import synalp.gui.BuildFrame;
import synalp.gui.Interface;
import synalp.gui.SearchLinkGui;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class SearchLink implements ActionListener
{
	Interface interf;
	SearchLinkGui sLink;
	
	public SearchLink(SearchLinkGui sLink, Interface interf)
	{
		this.interf= interf;
		this.sLink=sLink;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		String txtElt = sLink.getTxtElt().getText();
		
		//		
		InputStream in1;
		try 
		{
			in1 = new FileInputStream(new File(RDFTriple.T2MFile));
		
   	 		Model model =   ModelFactory.createDefaultModel();
	    	model.read(in1,null);
	    	
	    	String queryString= " SELECT distinct ?modelElt " +
	    			"WHERE {?txtElt <http://ModelWriter#hasValue> \""+txtElt+ "\"." +
	    			"{?txtElt <http://ModelWriter#isMorphologySimilarTo>  ?modelElt }" +
	    			"union {?txtElt <http://ModelWriter#isSameAs>  ?modelElt }" +
	    			"union {?txtElt <http://ModelWriter#isSynonymTo>  ?modelElt}}";	    	
	    		    	
	    	Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);	
	    	QueryExecution quex=QueryExecutionFactory.create(query,model);
	    
	    	try
	    	{
	    		ResultSet results = quex.execSelect();  	   
	    		
	    		if(results.hasNext())
	    		{
		    		BuildFrame frame= new BuildFrame();
		    		frame.addLabel(sLink.getDesktopPane(), "Model Element", 20, 100, 130,30);
		    		
		    		JComboBox   <String>comboBox = new <String> JComboBox();
		    		comboBox.setBounds(140, 100, 330, 30);	
		    		
	    			while( results.hasNext())
	    			{	    				
	    				QuerySolution row= results.nextSolution();
	    				RDFNode modelElt= row.get("modelElt");
	    				comboBox.addItem(modelElt.asLiteral().getValue().toString());
	    			}
	    			
	    			sLink.getDesktopPane().add(comboBox);
	    	 		sLink.getDesktopPane().validate();
	    		}
	    		else
	    		{
					JOptionPane.showMessageDialog(null, "No link is found");
	    		}
	    	}
	    	finally{quex.close(); }
		}
		catch (FileNotFoundException e1) {e1.printStackTrace();	}    	  
	}
}
