//Adapted from://DJC 20th March 2000package uk.ac.bham.cs.djc.ui;import java.awt.*;import java.awt.event.*;import uk.ac.bham.cs.djc.FootballImageParser;import java.net.URL;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;/**	Panel in which the results of processing an image with a FootballImageParser object can be viewed * 	 * 	<a href="FrameAnalyserPanel.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 20th March 2000 */public class FrameAnalyserPanel extends Panel {    /**	The image of the frame */    Image image=null;        /**	The image panel showing the processed frame */    ImagePanel thisImagePanel=null;        /**	The label indicating presence or absence of a pitch */    Label pitchLabel=null;    /**	Create a new FrameAnalyserPanel object     * 	@param	newImage the image showing a frame of a football video to process     */    public FrameAnalyserPanel(Image newImage) {	//super(new GridLayout(2,1));		image=newImage;		GridBagLayout gridbag=new GridBagLayout();	GridBagConstraints constraints=new GridBagConstraints();	setLayout(gridbag);		//Set-up and add the image panel:	buildConstraints(constraints,0,0,1,1,100,100);	constraints.fill=GridBagConstraints.BOTH;	thisImagePanel=new ImagePanel(image);	gridbag.setConstraints(thisImagePanel,constraints);	add(thisImagePanel);		//Set-up and add the pitch label:	buildConstraints(constraints,0,1,1,1,100,0);	constraints.fill=GridBagConstraints.HORIZONTAL;	pitchLabel=new Label();	pitchLabel.setSize(100,50);	gridbag.setConstraints(pitchLabel,constraints);	add(pitchLabel);    }        /**	Process the image     * 	     */    public void parse() throws Exception{	//Wait until the image size is available:	while(image.getWidth(this) <0 || image.getHeight(this)<0) {	    try { 		(Thread.currentThread()).sleep(1000);		    }	    catch(InterruptedException ie) {	    }	}		//Make a new parser and parse the image:	FootballImageParser parser=new FootballImageParser(image.getWidth(this),					image.getHeight(this),1,4,image);		//Highlight the goalposts in the image:	try {	    thisImagePanel.highlightColumn(parser.getFirstPostColumn());	    thisImagePanel.highlightColumn(parser.getSecondPostColumn());	}	catch(Exception e) {	    //Goal posts were not found	}	//Display the pitch label:	if(parser.isPitch()) {	    pitchLabel.setText("A PITCH WAS FOUND");	}	else {	    pitchLabel.setText("NO PITCH WAS FOUND");	}    }        /**	Build the Grid-Bag layout constraints from these parameters     * 	@param	gbc the Grid-Bag constraints to set     * 	@param	gx the x coordinate in the grid     * 	@param	gy the y coordinate in the grid     * 	@param	gw the grid width     * 	@param	gh the grid height     * 	@param	wx the x weight     * 	@param	wy the y weight     */    public static void buildConstraints(GridBagConstraints gbc,int gx,int gy,int gw,int gh,int wx,int wy) {	gbc.gridx=gx;	gbc.gridy=gy;	gbc.gridwidth=gw;	gbc.gridheight=gh;	gbc.weightx=wx;	gbc.weighty=wy;    }}