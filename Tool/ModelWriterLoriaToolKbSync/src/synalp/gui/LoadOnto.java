package synalp.gui;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import synalp.commons.input.knowledgeBase.OntologyAnalysis;

import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;

import java.util.Iterator;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class  LoadOnto  extends JFrame implements ActionListener
{

	private  Interface interf;

	
	public LoadOnto(Interface interf)
	{
		this.interf=interf;	
	}	
	
	public void actionPerformed(ActionEvent e)
	{      
		JFileChooser fc = new JFileChooser();
		File file;

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
		 	String nFic =  file.getPath();
		 	try
		 	{
		 		File unFic = new File(nFic);
		 	    String newplace="src/synalp/commons/input/corpora/"+file.getName();
		 
		 	    interf.setOntoFile(newplace);
		 	    
		 	    int taille = (int)unFic.length();
		 	    FileInputStream fol = new FileInputStream(unFic);
		 	    FileOutputStream foe = new FileOutputStream(newplace);
		 	    byte contenux[]= new byte[taille];
		 	    fol.read(contenux);
		 	    fol.close();
		 	    foe.write(contenux);
		 	    foe.close();		 	                
		 	  
		 	    // load a tree as a text 
		 	    BuildFrame frame= new BuildFrame();		
		 	    interf.getScrolModel().getViewport().setView(frame.readFileCheck(interf.getJModel(),newplace));		 	   	
				interf.getDesktopPane().validate();		 	    
		 	}
	 	    catch (IOException  a)
	 	    {
	 	    	System.out.println("Onto is not created " + a.getCause());
	 	    } 
		}
	}
}


