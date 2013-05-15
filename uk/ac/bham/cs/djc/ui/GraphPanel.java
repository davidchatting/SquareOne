//	DJC:	Created 15th March 2000.package uk.ac.bham.cs.djc.ui;import uk.ac.bham.cs.djc.*;import javax.swing.JPanel;import java.awt.GridLayout;import java.awt.Dimension;import javax.swing.JButton;import javax.swing.JSeparator;import java.awt.event.ActionListener;import java.awt.event.ActionEvent;import uk.ac.bham.cs.djc.time.Feature;import uk.ac.bham.cs.djc.time.Interest;/**	Panel in which graphs can be displayed. * 	 * 	<a href="GraphPanel.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 15th March 2000 */public class GraphPanel extends JPanel implements ActionListener{    /**	The graphs to display */    protected Graph[] graphs=null;        /**	Create a new GraphPanel to graph the fetaures of this Interest object      *	@param	thisPeriodOfInterest the period of interest that the graphs of each feature should be made     */    public GraphPanel(Interest thisPeriodOfInterest) {	super(new GridLayout(thisPeriodOfInterest.getNumberOfFeatures()*2,1));	//Set layout		graphs=new Graph[thisPeriodOfInterest.getNumberOfFeatures()];		Feature currentFeature=null;	Graph newGraph=null;	//For each feature:	for(int n=0;n<thisPeriodOfInterest.getNumberOfFeatures();n++) {	    currentFeature=thisPeriodOfInterest.getFeatureN(n);	    	    //Make a new graph:	    newGraph=new Graph(currentFeature);	    newGraph.setVisible(true);	    	    //Add to the array of graphs:	    graphs[n]=newGraph;	    	    //Add to the panel:	    add(newGraph);	    	    add(new JSeparator());	    	    currentFeature=null;	}    }        /**	Gets the size of this component     *  @param 	prevCreatedDimen a previously allocated Dimension object into which the result will be placed,     *  	if null the result is a newly allocated Dimension object     *	@return	A Dimension object that indicates the size of this component     */    public Dimension getSize(Dimension prevCreatedDimen) {	Dimension result=null;		int width=100;	int height=50;		//Work out the dimensions of the panel:	if(graphs.length>0) {	    width=(graphs[0].getSize((Dimension)null)).width;	    height=(graphs[0].getSize((Dimension)null)).height*graphs.length;	}		if(prevCreatedDimen!=null) {	    prevCreatedDimen.setSize(width,height);	    result=prevCreatedDimen;	}	else result=new Dimension(width,height);		return(result);    }        /**	Resizes this component     *	@param newDimension the new dimensions     */    public void setSize(Dimension newDimension) {	setSize(newDimension.width,newDimension.height);    }        /**	Sets the size of this component     *	@param width the new width     *	@param height the new height     */    public void setSize(int width,int height) {	//For each graph:	for(int i=0;i<graphs.length;i++) {	    //Set the graph's size:	    graphs[i].setSize(width,(height/graphs.length));	}    }        /**	Required for the implementation of the ActionListener interface, to allow object to listen to Clock events	     * 	Is invoked when an event is fired on one of the class with which this listener is registered     *	@param	ActionEvent the event catch by this listener     */    public void actionPerformed(ActionEvent event) {	String actionCommand=event.getActionCommand();		//if(the event signals that parsing has begun)	String timeHeader=new String("time:	");	if(actionCommand.startsWith(timeHeader)) {	    try {	    	int time=Integer.parseInt(actionCommand.substring(timeHeader.length(),actionCommand.length()));	    	for(int g=0;g<graphs.length;g++) {			graphs[g].setHighlight(time);	    	}	    }	    catch(NumberFormatException nfe) {	    }	}    }}