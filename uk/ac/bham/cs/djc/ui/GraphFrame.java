//	DJC:	Created 15th March 2000.package uk.ac.bham.cs.djc.ui;import javax.swing.JFrame;import java.awt.Dimension;/**	Panel in which graphs can be displayed. * 	 * 	<a href="GraphFrame.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 15th March 2000 */public class GraphFrame extends JFrame{    /**	Reference to the contained graphPanel */    protected GraphPanel graphPanel=null;        /**	Create a new GraphFrame     * 	@param	title the title for this frame     * 	@param	newGraphPanel the GraphPanel to add     */    public GraphFrame(String title,GraphPanel newGraphPanel) {	super(title);		graphPanel=newGraphPanel;    }        /**	Gets the size of this component     *	@return	A Dimension object that indicates the size of this component     */    public Dimension getSize() {	Dimension size=super.getSize();		graphPanel.setSize(size);		return(size);    }}