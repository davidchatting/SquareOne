package uk.ac.bham.cs.djc.ui;import java.awt.*;import java.awt.event.*;import java.applet.*;import java.io.IOException;import quicktime.*;import quicktime.io.*;import quicktime.qd.*;import quicktime.std.*;import quicktime.std.movies.*;import quicktime.std.movies.media.*;import quicktime.app.display.*;import quicktime.app.players.*;import quicktime.qd.QDRect;import quicktime.std.clocks.TimeRecord;import uk.ac.bham.cs.djc.time.Period;import java.awt.Canvas;import java.awt.Component;import java.awt.Dimension;import java.awt.Color;import java.awt.event.ActionEvent;/**	Football MPEG movie player using Apple's QuickTime for Java * 	Adapted from PlayMovie.java in the PlayMovie example in the Java SDK Sample Code distributed by Apple * 	 * 	<a href="QTFootballMoviePlayer.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created January 2000 */public class QTFootballMoviePlayer extends FootballMoviePlayer implements Errors{    	/**	The player */	private QTDrawable myPlayer;		/**	The movie */	private Movie m=null;		/**	The QuickTime canvas */	private QTCanvas myQTCanvas;		/**	The movie controller (visually play/pause buttons etc) */	private MovieController mc=null;    	/**	Run the player as an application 	*	@param	args the string at element zero is the path of the movie file to play	*/    	public static void main (String args[]) {		getNewQTFootballMoviePlayer(args[0]);	}		/**	Get a new QTFootballMoviePlayer	 * 	@param	movieURL the path of the movie file	 * 	@return	the new QTFootballMoviePlayer	 */	public static QTFootballMoviePlayer getNewQTFootballMoviePlayer(String movieURL) {	    QTFootballMoviePlayer moviePlayer=null;	    	    try { 		QTSession.open();		// make a window and show it - we only have one window/one movie at a time		moviePlayer = new QTFootballMoviePlayer(movieURL);	    }	    catch (QTException e) {		// at this point we close down QT if an exception is generated because it means		// there was a problem with the initialization of QT>		e.printStackTrace();		QTSession.close ();	    }	    	    return(moviePlayer);	}	/**	Create a new QTFootballMoviePlayer	 * 	@param	movieURL the path of the movie file	 */	public QTFootballMoviePlayer (String movieURL) {		super (movieURL);				//Craete and add a new canvas:		myQTCanvas = new QTCanvas();				add(myQTCanvas);	}		/**	Set the movie to play from the URL specified	 *	@param	theURL the URL of the movie to set	 *	@param	startTime the time in the match where this movie starts	 *	@exception Exception if there is a problem opening this movie for playing	 */	public void setMovie(String theURL,int startTime) throws Exception{	    theURL=fixURL(theURL);	    movieStartTime=startTime;	    System.out.println("movieStartTime:	" + movieStartTime);	    	    if(!theURL.equals(currentMovieFile)) {	    	try {			//Create the DataRef that contains the information about where the movie is:			DataRef urlMovie = new DataRef(theURL);							//Create the movie:			m = Movie.fromDataRef (urlMovie,StdQTConstants.newMovieActive);						//Create the movie controller:			mc = new MovieController (m);						mc.setKeysEnabled (false);	//Keys can't be used to control the movie			mc.setVisible(true);		//Control strip						//Create a new player:			myPlayer = new QTPlayer (mc);			myQTCanvas.setClient(myPlayer, true);						//Repaint this panel and all of those in the same window:			Component root=getHigestComponent(this);			Dimension newSize=root.getSize();			root.setSize(newSize);						currentMovieFile=theURL;	    	}	    	catch (QTException err) {			throw new Exception(err.toString());	    	}	    }	    else;	//No need to change the movie	}		/**	Transform from file:/myDirectory/myMovie.mpg to file:///myDirectory/myMovie.mpg URL format	 * 	QuickTime classes class don't work properly if the of a movie is specified without leading //	 * 	@param	theURL the URL to fix	 * 	@return	the fixed URL (a new object)	 */	public static String fixURL(String theURL) {	    String fixedURL=theURL;	    	    if(theURL.startsWith("file:/")) {		if(!theURL.startsWith("file://")) {			fixedURL=((new StringBuffer(theURL)).insert(5,"//")).toString();		}	    }	    	    return(fixedURL);	}		/**	Get the utimate parent of this panel, the root	 * 	@param	currentComponent the component to get the root of	 * 	@return	the root	 */	protected Component getHigestComponent(Component thisComponent) {	    Container parent=thisComponent.getParent();	    if(parent!=null) return(getHigestComponent(parent));	    else return(thisComponent);	}		/**	Get the player	 * 	@return	the player	 */	public QTDrawable getPlayer() {	    return(myPlayer);	}		/**	Get the canvas	 * 	@return the canvas	 */	public QTCanvas getCanvas () {		return(myQTCanvas);	}		/**	Get the movie	 * 	@return the movie	 */	public Movie getMovie() {		return m;	}		/**	Close the FootballMoviePlayer	 */	public static void close() {	    QTSession.close();	}		/**	Stop the video playing	 */	public void stopPlayer() {		try {			if (m != null)				m.setRate(0);		} catch (QTException err) {			err.printStackTrace();		}	}		/**	Select this video clip so that next time the movie is played only this selection is seen     	 * 	@param clipToPlay the clip to play     	 * 	@exception Exception if the clip can't be played     	 */	public void selectClip(Period periodToPlay) throws Exception{	    try {	    	TimeRecord startTime=new TimeRecord();	    	TimeRecord duration=new TimeRecord();	   			//Need to subtract the offset of the video:	    	startTime.setValue((long)(periodToPlay.getStart()));	//Number of ticks	    	startTime.setScale(1000);				//Ticks per second 1000, because period returns milliseconds	    	duration.setValue((long)periodToPlay.getDuration());	//Number of ticks	    	duration.setScale(1000);				//Ticks per second 1000, because period returns milliseconds	   	 		//Select the selection to play:	    	mc.setSelectionBegin(startTime);	    	mc.setSelectionDuration(duration);	    	mc.setPlaySelection(true);				//Make sure the selection gets set:		while(!mc.getPlaySelection()) {		    try { 			(Thread.currentThread()).yield();		    }		    catch(Exception e) {		    }		}	    }	    catch(quicktime.QTException qte) {		//Catch and rethrow the exception:		throw new Exception(qte.toString());	    }	}		/**	Get the current time from the begining of the video	 *	@return	the relative time	 *	@exception	Exception if can't obtain the relative time	 */	public int getRelativeTime() throws Exception{	    int result=0;	    	    try {		result=(mc.getCurrentTime()*1000)+movieStartTime;		result/=mc.getTimeScale();	    }	    catch(quicktime.std.StdQTException sqte) {		throw new Exception(sqte.toString());	    }	    	    return(result);	}		/**	Gets the size of this component     	 *	@param	newDimension the new size of this component     	 */	public void setSize(Dimension newDimension) {	    super.setSize(newDimension);	}		/**	Gets the size of this component     	 *	@return	A Dimension object that indicates the size of this component     	 */	public Dimension getSize(){	    	Dimension result=null;				try {			QDRect bounds=m.getBounds();			result=new Dimension(bounds.getWidth()+50,bounds.getHeight()+50);		}		//catch(StdQTException stde) {		catch(Exception e) {		    	//Catch a StdQTException or NullPointerException if m not set:		    	//Default: maintain standard 3:4 ratio of tv:		    	result=new Dimension(270,360);		}				return(result);    	}		/**	Paint this component - drawing the graph in the panel     	*	@param	g the Graphics context     	*/    	public void paintComponent(Graphics g) {	    	g.setColor(Color.black);		super.paintComponent(g); //paint background    	}}