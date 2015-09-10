package synalp.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import java.io.PrintStream;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import synalp.changes.Text.AddTextElement;
import synalp.changes.Text.RemoveTextElement;
import synalp.commons.statistic.Stats;

/**
 * 
 * @author Mariem Mahfoudh
 *
 */

public class Interface 
{
	
	private JDesktopPane  desktopPane ;
	private JTextArea JTxt, JModel, JLink;	
	private String ontoFile,textFile;	
	private JScrollPane scrollModel,scrollLink;
	
	private void createMenuBar(JFrame f) throws IOException
	{
		JMenuBar menuBar = new JMenuBar();
		// File Menu
		JMenu fMenu = new JMenu("File");
		 
		JMenuItem txtMenu = new JMenuItem("Upload Text");
		fMenu.add(txtMenu);
		txtMenu.addActionListener(new LoadText(this));
		 
		JMenuItem modelMenu = new JMenuItem("Upload Model");
		fMenu.addSeparator();
		fMenu.add(modelMenu);
		modelMenu.addActionListener(new LoadOnto(this));
		 
		// Run Menu
		JMenu rMenu = new JMenu("Link");
		JMenuItem geneLink= new JMenuItem("Generate Links");
		rMenu.add(geneLink);
		geneLink.addActionListener(new LinkView(this, 0));

		JMenuItem searchLink= new JMenuItem("Search Link");
		rMenu.add(searchLink);
		searchLink.addActionListener(new SearchLinkGui(this));
		
		JMenuItem addLink= new JMenuItem("Add Link");
		rMenu.add(addLink);
		addLink.addActionListener(new AddLinkGui(this));
		
		JMenuItem removeLink= new JMenuItem("Remove Link");
		rMenu.add(removeLink);
		removeLink.addActionListener(new RemoveLinkGui(this));
		
		// Change Menu
		JMenu cMenu = new JMenu("Change");
		
		JMenuItem renameWord = new JMenuItem("Rename Text Element");
		cMenu.add(renameWord);
		cMenu.addSeparator();
		renameWord.addActionListener(new RenameTextGui(this));
		
		JMenuItem addWord = new JMenuItem("Add Text Element");		
		cMenu.add(addWord);
		cMenu.addSeparator();
		addWord.addActionListener(new AddTextElement(this));
		
		JMenuItem removeWord = new JMenuItem("Remove Text Element");
		cMenu.add(removeWord);
		removeWord.addActionListener(new RemoveTextElement(this));
		
		// statistic Menu
		JMenu sMenu = new JMenu("Statistic");
		JMenuItem statMenu = new JMenuItem("Show statistic");
		sMenu.add(statMenu);
		statMenu.addActionListener(new Stats(this));
		
		menuBar.add(fMenu);
        menuBar.add(rMenu);
        menuBar.add(cMenu);
        menuBar.add(sMenu);

        menuBar.setSize(250,250);
        f.setJMenuBar(menuBar);	
	 }
	 
	 public Interface() throws IOException
	 {		
		BuildFrame frame= new BuildFrame();
		JFrame  f= frame.createFrame("ModelWriter Project", 1060,730);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(233,233,233));	
		
	    createMenuBar(f);
		//int x= 30, y= 15,  width= 140,  height= 30;
		 
		frame.addLabel (desktopPane, "The Text", 10,10,100,30); 
		JTxt = frame.addTextArea(desktopPane,10,40,500,300);
          
		frame.addLabel(desktopPane,"The Model", 530,10,100,30);
		JModel = frame.addTextArea(desktopPane,530,40,500,300);
		scrollModel= frame.addScrollPane(desktopPane, 530,40,500,300);
		
		frame.addButton(desktopPane,"Plain", "text view", 890,20,70,20, new OntoView(this, 0));
		frame.addButton(desktopPane,"Tree", "tree view", 960,20,70,20, new OntoView(this, 1));		
		 
		frame.addLabel(desktopPane,"The links between text and model", 230,350,300,30);
		JLink = frame.addTextArea(desktopPane,230,380,600,280);
		scrollLink= frame.addScrollPane(desktopPane,230,380,600,280);
		
		frame.addButton(desktopPane,"T2M", "TextToModel", 620,360,70,20, new LinkView(this,1));
		frame.addButton(desktopPane,"M2T", "ModelToText", 690,360,70,20, new LinkView(this,2));
		frame.addButton(desktopPane,"Link", "Link without redundance", 760,360,70,20, new LinkView(this,3));
				
		JPanel  panel = (JPanel)f.getContentPane();
		panel.add( desktopPane,BorderLayout.CENTER); 
		f.setVisible(true);
	        
		/** to resolve the problem of error comment of coreNLP **/
		PrintStream err = new PrintStream (" ");
		System.setErr(err);
	}

	 
	public JDesktopPane getDesktopPane() 
	{
		return desktopPane;
	}

	public void setDesktopPane(JDesktopPane desktopPane) 
	{
		this.desktopPane = desktopPane;
	}

	public JTextArea getJtext() 
	{
		return JTxt;
	}

	public JTextArea getJModel() 
	{
		return JModel;
	}

	public void setJtext(JTextArea jText) 
	{
		JTxt = jText;
	}

	public void setJModel(JTextArea jModel) 
	{
		JModel = jModel;
	}


	public JTextArea getJLink() {
		return JLink;
	}

	public void setJLink(JTextArea jLink) {
		JLink = jLink;
	}

	public String getOntoFile() {
		return ontoFile;
	}

	public String getTextFile() 
	{
		return textFile;
	}

	public void setOntoFile(String ontoFile) 
	{
		this.ontoFile = ontoFile;
	}

	public void setTextFile(String textFile) 
	{
		this.textFile = textFile;
	}
	
	public JScrollPane getScrolModel() {
		return scrollModel;
	}

	public JScrollPane getScrollLink() {
		return scrollLink;
	}

	public void setScrolModel(JScrollPane scrolModel) {
		this.scrollModel = scrolModel;
	}

	public void setScrollLink(JScrollPane scrollLink) {
		this.scrollLink = scrollLink;
	}

}
