package synalp.gui;


/**
 * 
 * @author Mariem Mahfoudh
 *
 */
import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;

import synalp.commons.input.texts.TextPreProcessing;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

//import synalp.commons.input.texts.ExcelFile;

public class  LoadText  extends JFrame implements ActionListener
{
		 
	private Interface interf;
	
	private String fileName;

	public LoadText(Interface interf)
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
			
		 	/*********/

		 	String nFic =  file.getPath();
		 	try
		 	{
		 		File unFic = new File(nFic);
		 		fileName="src/synalp/commons/input/corpora/"+file.getName();		 	    
		 	   
		 	    // Preprocessing the input text
		 	   // TextPreProcessing txtpreprocess = new TextPreProcessing(newPlace);
		 	   // txtpreprocess.lemmatize();
		 	 
		 	    
		 	    int taille = (int)unFic.length();

		 	    FileInputStream inputS = new FileInputStream(unFic);
		 	    FileOutputStream foe = new FileOutputStream(fileName);
		 	    byte contenux[]= new byte[taille];
		 	    inputS.read(contenux);		 	    
		 	    inputS.close();
		 	    foe.write(contenux);
		 	    foe.close();
		 	    interf.setTextFile(fileName);//"src/synalp/commons/input/corpora/lemmFile.txt
		 
		 	    // show txt 
		 	    JScrollPane scrollPane = new JScrollPane();
		 	    scrollPane.setBounds(10,40,500,300);	
		 	    BuildFrame frame= new BuildFrame();
		 	    scrollPane.getViewport().setView(frame.readFileCheck(interf.getJtext(),fileName));
		 	    interf.getDesktopPane().add(scrollPane);
		 	    interf.getDesktopPane().validate();		 	             
		 	}
		 	catch (IOException  a)
		 	{
		 		System.out.println("File not created: " + a.getCause());
		 	} 
		}		    
	}
}



