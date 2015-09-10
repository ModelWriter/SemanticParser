package synalp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

import synalp.synchronization.RDFTriple;
import synalp.synchronization.RemoveLink;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class RemoveLinkGui implements ActionListener
{

	private Interface interf ;
	private JFrame f;
	private JComboBox   <String>comboBox;
	private JDesktopPane desktopPane ;
	
	public RemoveLinkGui(Interface interf)
	{
		this.interf=interf;
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		BuildFrame frame= new BuildFrame();
		f = frame.createFrame("Remove link", 500, 200);
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));
			
		frame.addLabel(desktopPane, "Links", 20, 30, 100,30);		
	
		comboBox = new <String> JComboBox();
		comboBox.setBounds(110, 30, 330, 30);	
		
		////
		InputStream in1;
		try 
		{		
			in1 = new FileInputStream(new File(RDFTriple.T2MFile));		
	 		Model model =   ModelFactory.createDefaultModel();
	 		model.read(in1,null);
    	
    		String queryString= " SELECT ?txtValue ?txtOffset ?modelElt " +
    			"WHERE {?txtElt <http://ModelWriter#hasValue> ?txtValue ." +
    			" ?txtElt <http://ModelWriter#hasOffset> ?txtOffset ." +
    			"{?txtElt <http://ModelWriter#isMorphologySimilarTo>  ?modelElt }" +
    			"union {?txtElt <http://ModelWriter#isSameAs>  ?modelElt }" +
    			"union {?txtElt <http://ModelWriter#isSynonymTo>  ?modelElt}}";	 
    		
    		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);	
    		QueryExecution quex=QueryExecutionFactory.create(query,model);
    
    		try
    		{
    			ResultSet results = quex.execSelect();      			
    			//ResultSetFormatter.out(results) ;
	    		String link;	    		
	    		
    			while( results.hasNext())
    			{	    				
    				QuerySolution row= results.nextSolution();   
    				String txtValue= row.get("txtValue").asLiteral().getValue().toString();
    				String txtOffset= row.get("txtOffset").asLiteral().getValue().toString();
    				String modelElt= row.get("modelElt").asLiteral().getValue().toString();
    				
    				link= "Text (" + txtValue+ ", "+ txtOffset+ "), Model ("+ modelElt +")";    			
    				comboBox.addItem(link);
    			}		
    		}
	    	finally{quex.close(); }
		}
		catch (FileNotFoundException e1) {e1.printStackTrace();	}   
		
		desktopPane.add(comboBox);
    	
		frame.addButton(desktopPane, "Remove", "Remove Link ", 110, 100, 120, 30, new RemoveLink(this, interf));
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add( desktopPane,BorderLayout.CENTER); 
		f.setVisible(true);		
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}
	
	
	
	
}
