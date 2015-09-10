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

import synalp.commons.input.knowledgeBase.IOntologyAnalysis;
import synalp.commons.input.knowledgeBase.OntologyAnalysis;

import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;

import java.util.Iterator;
	
/**
 * 
 * @author Mariem Mahfoudh
 *
 */
public class  OntoView  extends JFrame implements ActionListener
{

	private  Interface interf;
	int type;
	
	public OntoView (Interface interf, int type)
	{
		this.interf=interf;
		this.type= type;
	}	
	
	public void actionPerformed(ActionEvent e)
	{  
			 	
		if( type==0)  // load a tree as a text
		{
		 	BuildFrame frame= new BuildFrame();
		 	interf.getScrolModel().getViewport().setView(frame.readFileCheck(interf.getJModel(),interf.getOntoFile()));
		 	interf.getDesktopPane().validate();	
		 }
	
		else // Load ontology as a tree
		{		 	
			try 
			{
				IOntologyAnalysis onto = new OntologyAnalysis(interf.getOntoFile());
 	            
	 	        DefaultMutableTreeNode root, node, nodeCls, nodeObjectProperty, nodeDataProperty, nodeIndividual;
	 	        
	 	        root = new DefaultMutableTreeNode("Ontology", true);
	 	       
	 	        nodeCls= new DefaultMutableTreeNode("Classes", true);
	 	        nodeObjectProperty= new DefaultMutableTreeNode("ObjectProperties", true);
	 	        nodeDataProperty= new DefaultMutableTreeNode("DataProperties", true);
	 	        nodeIndividual= new DefaultMutableTreeNode("Individuals", true);
	 	        
	 	        root.add(nodeCls);
	 	        root.add(nodeObjectProperty);
	 	        root.add(nodeDataProperty);
	 	       	root.add(nodeIndividual);

                UIManager.put("Tree.hash", new ColorUIResource(Color.PINK));
	 	        
	 	       	// Adds classes
	 	       	Iterator <OntClass> clsList= onto.getClasses().iterator();
	 	       	while(clsList.hasNext())	 	        
	 	        {
	 	       		OntClass cls= clsList.next();
	 	            node = new DefaultMutableTreeNode(cls.getLocalName(), true);
	 	            nodeCls.add(node);
	 	        }

	 	       	// Adds objectProperty
	 	        ExtendedIterator <ObjectProperty> objPropList = onto.getObjectProperties();
	 	        while(objPropList.hasNext())
	 	        {
	 	        	ObjectProperty op=  objPropList.next();
	 	            node = new DefaultMutableTreeNode(op.getLocalName(), true);
	 	            nodeObjectProperty.add(node);
	 	        }

	 	        // Adds DataProperties
	 	        ExtendedIterator <DatatypeProperty> dataPropList = onto.getDatatypeProperties();
	 	        while(dataPropList.hasNext())
	 	        {
	 	        	DatatypeProperty dp = dataPropList.next();
	 	        	node = new DefaultMutableTreeNode(dp.getLocalName(), true);
	 	            nodeDataProperty.add(node);
	 	        }
	 	        
	 	       // adds Individuals
	 	        Iterator <Individual > indList= onto.getIndividuals().iterator();
	 	        if( indList!=null)
	 	        while(indList.hasNext())
	 	        {
	 	        	Individual ind = indList.next();
	 	        	node = new DefaultMutableTreeNode(ind.getLocalName(), true);
	 	            nodeIndividual.add(node);
	 	        } 
	 	        JTree  tree = new JTree(root);	 	      
	 	        Font fontTreeOnto = new Font("Dialog",Font.BOLD,12);
	 	        tree.setFont(fontTreeOnto );
	 	        tree.setForeground((new Color(32,11,244)));
	 	       	 	
			 	interf.getScrolModel().getViewport().setView(tree); 
			 	interf.getDesktopPane().validate();	
				
			}
		 	catch (FileNotFoundException e1) 
		 	{
				e1.printStackTrace();
			}	 	        
		}
	
			
	}
}


