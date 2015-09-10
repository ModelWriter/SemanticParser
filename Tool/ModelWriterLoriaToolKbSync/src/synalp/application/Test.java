/***  @ author:  Mariem Mahfoudh***/

package synalp.application;


import synalp.commons.input.knowledgeBase.IOntologyAnalysis;
import synalp.commons.input.knowledgeBase.OntologyAnalysis;
import synalp.commons.input.texts.ExcelFile;
import synalp.commons.input.texts.TextPreProcessing;
import synalp.gui.Interface;
import synalp.synchronization.CreateLink;


public class Test 
{	
	public static void main(String[] args) throws Exception
	{
		
		/******Create  corpus text *********/	
		// input files		
		String fileKB = ("src/synalp/commons/input/component-03072015.rdf");
		String fileDB = ("src/synalp/commons/input/rules.xls");
		
		// output files
		String fileTextFromDB = ("src/synalp/commons/output/rulesDB.txt");
		String fileTextFromKB= ("src/synalp/commons/output/definitionKB.txt");
		
		// convert  DB rules to plain text		
		//ExcelFile xls= new ExcelFile(fileDB);				
		//xls.setInputFile(fileTextFromDB);		

		// extract definition from KB	    
		//IOntologyAnalysis  onto= new OntologyAnalysis (fileKB);
		//onto.getIndividuals();
		//onto.CreateTextFromDefinition( fileTextFromKB) ;	 		
		
		
		/***  Parsing plain txt  ***/
		// 1. stop word List		
		String parsingFileKB = ("src/synalp/commons/output/parsingFileKB.txt");
/*		TextPreProcessing preprocessKB = new TextPreProcessing (fileTextFromKB);
		preprocessKB.removeStopList(parsingFileKB);
		

		
		
		/***  Parsing plain txt  ***/
		// 1. stop word List		
		//String parsingFileDB = ("src/synalp/commons/output/parsingFileDB.txt");
		//TextPreProcessing preprocessDB= new TextPreProcessing (fileTextFromDB);
		//preprocessDB.removeStopList(parsingFileDB);
		//preprocessDB.cleanText(parsingFileDB);
		
	
		
		
		// show interface
		
		Interface gui= new Interface();
		
		
	}	
}
