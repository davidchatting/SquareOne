/* Generated by Together *///	DJC:	Created Saturday 20th November 1999.package uk.ac.bham.cs.djc.ui;import java.applet.*;import java.awt.*;import java.util.Vector;import java.net.URL;import java.net.MalformedURLException;import java.io.File;import java.io.IOException;import uk.ac.bham.cs.djc.time.Schedule;import uk.ac.bham.cs.djc.Parameter;import uk.ac.bham.cs.djc.ParserDescription;import uk.ac.bham.cs.djc.MediaFile;import uk.ac.bham.cs.djc.time.TimesOfInterest;import uk.ac.bham.cs.djc.time.TimeLine;import uk.ac.bham.cs.djc.FootballMovieFileParser;import uk.ac.bham.cs.djc.mediaDescriptors.TemporalMediaBlock;import java.lang.reflect.Constructor;import quicktime.app.display.QTCanvas;import java.awt.event.ActionListener;import java.awt.event.ActionEvent;import javax.swing.JApplet;import javax.swing.JPanel;import javax.swing.JFrame;import java.awt.GridLayout;import java.awt.Dimension;/**	The multimedia football summarising application  * 	Can be run as either an application or an applet * 	 * 	<a href="Application.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 20th November 1999 */public class Application extends JApplet implements ActionListener, Runnable {    /**	Vector of parser descriptions for different media files */    private Vector parsers=new Vector();        /**	Flag set true if run as an application, false if applet */    private static boolean isApplication=false;        /**	Schedule respresenting where media occur in time */    private Schedule thisSchedule=new Schedule();        /**	Mapping between parameter names and classes responsible for parsing that file type */    private static String[][] parameters= {	{"FOOTBALL_MOVIE_FILE","uk.ac.bham.cs.djc.FootballMovieFileParser"},    						{"MATCH_REPORT_FILE","uk.ac.bham.cs.djc.MatchReportFileParser"},    						{"HOME_TEAM_SHEET_FILE","uk.ac.bham.cs.djc.TeamSheetFile"},						{"AWAY_TEAM_SHEET_FILE","uk.ac.bham.cs.djc.TeamSheetFile"},						{"HOME_TEAM_COLOURS_FILE","uk.ac.bham.cs.djc.TeamColoursFile"},						{"AWAY_TEAM_COLOURS_FILE","uk.ac.bham.cs.djc.TeamColoursFile"}	};        /**	Vector of the listeners registered to receive events from the application */    private Vector listeners=new Vector();        /**	Currently visable panel */    private JPanel currentPanel=null;        /**	Frame enclosing applet - when run as application */    private JFrame enclosingFrame=null;        /**	Reference to the SettingsPanel */    private SettingsPanel thisSettingsPanel=null;        /**	The data file from which to read/write */    private String interestFileName=new String("interest.out");        /**	If interest should be read from file or not */    private boolean readFromFile=false;        /**	The movie parsing thread */    private Thread movieParsingThread=null;        /** Constructor     * 	@param argumentList an array of parameter name and value strings     */    public Application(String argumentList[]){	thisSettingsPanel=new SettingsPanel(this);	thisSettingsPanel.setVisible(false);		if(argumentList.length!=0) setParameters(argumentList);	else {	    readFromFile=true;	    interestFileName=thisSettingsPanel.openFileChooser().getFile();	}		//Start parsing the movie:	movieParsingThread=new Thread(this);	movieParsingThread.start();    }    /**	Run as an application      *	@param argumentList a string of parameter name and value pairs     */    public static void main(String argumentList[]) {	isApplication=true;	new AppletFrame(argumentList);    }    /**	Sets the application parameters taking the argument list     *	@param argumentList a string of parameter name and value pairs     */    private void setParameters(String argumentList[]) {	//for(each string in the argument list)	for (int i = 0; i < argumentList.length;) {	    //if(string is a parameter name)            if(isParameterName(argumentList[i])) {		//Create a new Parameter with that name:		Parameter currentParameter=new Parameter(argumentList[i]);				i++;		//while(the following strings are not parameter names (ie parameter values))		while(i<argumentList.length && !isParameterName(argumentList[i])) {		    	//Add the value to the parameter:		    	currentParameter.addValue(argumentList[i]);			i++;		}		//Add this parameter to the vector of parameters:		thisSettingsPanel.addParameter(currentParameter);            }	}	//thisSettingsPanel.listParameters();    }        /**	Is the argument a recognised parameter name?      *	@param argument the name to test     *	@return result     */    private boolean isParameterName(String argument) {	boolean result=false;		//Find a parser class with a matching paramter name to argument:	for (int i = 0; i < ((parameters.length)/2) && result==false; i++) {		if(argument.equals(parameters[i][0])) result=true;	}        	return(result);    }        /**	Return a string containing the classname and package of a class to parse this parameter      * 	@param	parameter the name of the parameter to find a parsing class for     *	@exception Exception if no parser class found     */    private String getParsingClassForThisParameter(String parameter) throws Exception{	String parsingClass=null;		//Find a parser class with a matching paramter name:	for (int i = 0; i < ((parameters.length)/2) && parsingClass==null; i++) {		if(parameter.equals(parameters[i][0])) {		    parsingClass=parameters[i][1];		}	}		//Throw exception if no parser class found:	if(parsingClass==null) throw new Exception("No class found to parse parameter: " + parameter);		return(parsingClass);    }        /**	Initalise the application */    public void init() {	//The applcation is a listener to its own events:	addListener(this);	//Add layout manager:	getContentPane().setLayout(new GridLayout(1,1));		//Change the pane to the SettingsPanel:	if(thisSettingsPanel==null) {	    thisSettingsPanel=new SettingsPanel(this);	}		//Settings panel is currently unused:	//changePanel(thisSettingsPanel);    }        /**	Start the application */    public void start() {    }        /**	Parse the media files to recover metadata     *	@exception	Exception if there is no video data      */    public void parse() throws Exception {	TimesOfInterest intersection=null;	if(readFromFile) {	   	intersection=TimesOfInterest.readFromFile(interestFileName);	}	else {	    	//Create a schedule from all the temporal media files (text & video):		buildScheduleFromMediaFiles();	    		//Get the timelines for the video and match reports (TemporalMediaBlocks):	    	TimeLine videoTimeLine=thisSchedule.getMediaBlockClassTimeLine("uk.ac.bham.cs.djc.mediaDescriptors.VideoBlock");		TimeLine textTimeLine=thisSchedule.getMediaBlockClassTimeLine("uk.ac.bham.cs.djc.mediaDescriptors.TextualEventDescriptor");				//if(there are timelines for both the video and reports):		if(videoTimeLine!=null && textTimeLine!=null) {			//Get the intersection between the video and textual event descriptions,			//	throws Exception if if one or both of media block classes do not have a timeline:			intersection=new TimesOfInterest(thisSchedule.getIntersection(videoTimeLine,textTimeLine));			if(intersection.numOfPeriods()==0) throw new Exception("No intersection");		}		//Otherwise, if there's just a video timeline:		else if(videoTimeLine!=null) {		    	//Parse all the video data available:		    	intersection=new TimesOfInterest(videoTimeLine);		}		else throw new Exception("No video data available");				//Parse the sections of video described by the timeline:		intersection=parse(intersection);				//Write the timeline to file:		intersection.writeToFile(interestFileName);	}			try {		//From the measures of interest prune the intersection to just the described events:		intersection.findMostInterestingSequences();					//Play the video which intersects with the event descriptions:		ResultsPanel results=new ResultsPanel(this,intersection);		changePanel(results);	}	catch(Exception e) {		System.out.println(e.toString());	}    }        /**	Build a schedule of events from the media files     *	specificed in the application's parameter list     */    private void buildScheduleFromMediaFiles() {	//for(each parameter)	for (int i = 0; i<thisSettingsPanel.getNumberOfParameters(); i++) {	    try {		//Parse the file specified by the parameter:	    	addMediaFileToSchedule(thisSettingsPanel.getNthParameter(i));	    }	    catch(Exception e) {		//Skip parameter if exception generated		System.out.println(e.toString());	    }	}    }        /**	Calculate the intersection between the two timelines of the specified type and parse the video stream     * 	@param	selectionToParse a TimesOfInterest describing the period of the video to parse (parsing is destructive)     * 	@return	a TimesOfInterest object with associated features and scores     * 	@exception IOException if unable to parse video     * 	@exception Exception if one or both of media block classes do not have a timeline     */    private TimesOfInterest parse(TimesOfInterest selectionToParse) throws IOException,Exception{	TimesOfInterest parsedSlection=null;		//Inform the UI elements that the application is now "parsing":	fireEvent(new ActionEvent(this,0,"parsing"));				//Parse the video:	int n=0;	ParserDescription currentParserDescription=null;	try {	    	//Find the parser for FOOTBALL_MOVIE_FILE:		do {		    currentParserDescription=(ParserDescription)parsers.elementAt(n);		    n++;		} while(!(currentParserDescription.getParameterName().equals("FOOTBALL_MOVIE_FILE")));		//Generates ArrayIndexOutOfBoundsException if not found				FootballMovieFileParser thisFootballMovieFileParser=(FootballMovieFileParser)(currentParserDescription.getParser());				//Parse the movie data:		thisFootballMovieFileParser.parseVideoSequence(selectionToParse);		parsedSlection=selectionToParse;	}	catch(ArrayIndexOutOfBoundsException aioobe) {	    	//Didn't find a parser for FOOTBALL_MOVIE_FILE	}		//Inform the UI elements that "parsing complete":	fireEvent(new ActionEvent(this,0,"parsing complete"));		return(parsedSlection);    }        /**	Add this media file to the schedule     * 	@param thisParameter the parameter specifying this media file     * 	@exception	Exception if the media file couldn't be added to the schedule     */    private void addMediaFileToSchedule(Parameter thisParameter) throws Exception{	String parsingClass=getParsingClassForThisParameter(thisParameter.getName());		try {		MediaFile thisMediaFile=null;			//Create a class for this parameter:		Class c=Class.forName(parsingClass);		System.out.println("parsingClass: " + parsingClass + " for " + thisParameter.getName());			//Create a class for the arguments:		Class args[]= {Class.forName("java.net.URL"),Class.forName("java.lang.String")};				//Create a constructor for this class, with these arguments:		java.lang.reflect.Constructor thisConstructor=c.getConstructor(args);				//Get the URL of the media file refered to by this parameter:		URL URLOfThisParameter=getURLOfThisParameter(thisParameter);		System.out.println("URL of media file: " + URLOfThisParameter);				//Create an instance of this class, passing the URL as an argument:		Object initargs[]= {URLOfThisParameter,thisParameter.getOptions()};		thisMediaFile=(MediaFile)thisConstructor.newInstance(initargs);		System.out.println("Created new instance of " + parsingClass);						//Add this parser to the list of known parsers:		parsers.addElement(new ParserDescription(thisParameter.getName(),thisMediaFile));				//Add this media file to the schedule:		thisSchedule.add(thisMediaFile);	}	catch(Exception e) {	    throw(new Exception(e.toString() + " (parsingClass, " + parsingClass + ", for " + thisParameter.getName() + ")"));	}    }        /**	Get an absolute URL for this parameter     *	@param	thisParameter parameter to get the URL of    *	@return	the URL for this parameter    *	@exception MalformedURLException if the resulting URL is malformed    */    public URL getURLOfThisParameter(Parameter thisParameter) throws MalformedURLException{	String file=thisParameter.getFile();	URL URLtoReturn=null;		try {	    //Try and make a URL from the raw value of the parameter:	    URLtoReturn=new URL(file);	}	catch(java.net.MalformedURLException murle) {	    //Either no legal protocol could be found in a specification string	    //or the string could not be parsed (from javadoc)	    	    //Case 1: protocol not present => local file (possibily with relative path):	    try {		//Generate a absolute path:		String absolutePath;				if(isApplication) {		    //Get absolute path of local file:		    absolutePath=(new File(file)).getAbsolutePath();		    		    URLtoReturn=new URL("file","",absolutePath);		  } else {		      //Applet - relative to www server path		      //localDirectory=getCodeBase().getFile();		  }		}		catch(java.net.MalformedURLException murle2) {		    //Case 2: string is rubbish => location can't be opened		    //	Can't do much in this case!		    throw murle2;		}	    }	    	    return(URLtoReturn);    }        /**	Get an absolute URL for this parameter     *	@param	parameterName parameter name of paramter to get URL of    *	@exception	MalformedURLException if the resulting URL is malformed    */    public URL getURLOfThisParameter(String parameterName) throws MalformedURLException{	URL URLtoReturn=getURLOfThisParameter(new Parameter(parameterName,getParameterValue(parameterName)));		return(URLtoReturn);    }        /**     * 	If run as an applet will obtain parameters from HTML file,     * 	otherwise if it is an application from the parameter list     * 	@param	name of the parameter     * 	@return the parameter value     */    private String getParameterValue(String name){	//Currently can only have one parameter of each type		String value=null;	if(!isApplication) {	    //if run as an applet, get parameter from HTML:	    value=super.getParameter(name);	} else {	    //if run as an application, get parameter command line parameter list:	    for(int parameterNumber=0;parameterNumber<thisSettingsPanel.getNumberOfParameters();parameterNumber++) {		Parameter currentParameter=thisSettingsPanel.getNthParameter(parameterNumber);		if((currentParameter.getName()).equals(name)) value=currentParameter.getValue();	    }	}		return(value);    }        /**	For implementation of ActionListener - listens to its own events     *	@param	event describing the action performed    */    public void actionPerformed(ActionEvent event) {	String actionCommand=event.getActionCommand();		//if(the event signals that parsing has begun)	if(actionCommand.equals("parsing")) {	    //Change to the progress panel:	    changePanel(new ProgressPanel(this));	}    }        /**	Add listener to receive application events     *	@param	newListener the new listener to add    */    public void addListener(ActionListener newListener) {	listeners.addElement(newListener);    }        /**	Remove listener from list of registered objects     *	@param	listenerToRemove the listener to remove    */    public void removeListener(ActionListener listenerToRemove) {	listeners.removeElement(listenerToRemove);    }        /**	Fire this event to all registered listeners     *	@param	eventToFire the event to fire    */    private void fireEvent(ActionEvent eventToFire) {	for(int index=0;index<listeners.size();index++) {	    ((ActionListener)listeners.elementAt(index)).actionPerformed(eventToFire);	}    }        /**	Change the content of the frame to this panel     *	@param newPanel the new panel to display    */    public void changePanel(JPanel newPanel) {	currentPanel=newPanel;		//Clear previous contents:	getContentPane().removeAll();		//Add the new panel:	getContentPane().add(currentPanel);	//Set the size of the Application panel (applet):	getContentPane().setSize(currentPanel.getSize((Dimension)null));		//Try to pack the contents of the frame:	//if(enclosingFrame!=null) enclosingFrame.pack();		if(enclosingFrame!=null) enclosingFrame.setSize(enclosingFrame.getSize());		validate();	repaint();    }        /**	Set the size of the Application panel (applet)     * 	@param	newDimension the new dimension to set the panel to     */    public void setSize(Dimension newDimension) {	setSize(newDimension.width,newDimension.height);    }        /**	Set the size of the Application panel (applet)     * 	@param	width the new width to set the panel to     * 	@param	height the new height to set the panel to     */    public void setSize(int width,int height) {	currentPanel.setSize(width,height);		super.setSize(width,height);    }        /**	Get the size of the Application panel (applet)     * 	@return	the dimensions of the Application panel     */    public Dimension getSize() {	//Get the size of the current panel:	Dimension result=null;		if(currentPanel!=null) {	    result=currentPanel.getSize(null);	}	else {	    result=new Dimension(-1,-1);	}		return(result);    }        /**	Set the enclosing frame for the Application, only used when run as application     * 	@param	newEnclosingFrame the new enclosing frame     */    public void setEnclosingFrame(JFrame newEnclosingFrame) {	enclosingFrame=newEnclosingFrame;    }        /**	Overloaded stop() method on Applet class */    public void stop() {    }        /**	Allows parsing to be run in its own thread:     * 	For implementation of Runnable      */    public void run() {	try {		parse();	}	catch(Exception e) {	}    }}