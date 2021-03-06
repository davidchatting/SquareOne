/* Generated by Together *///20th November 1999 - DJCpackage uk.ac.bham.cs.djc;import java.net.URL;import java.util.Vector;import uk.ac.bham.cs.djc.mediaDescriptors.MediaBlock;/**	<p> * 	 * 	<a href="MediaFile.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 20th November 1999 */public class MediaFile {    /**	List of media blocks which have been parsed from this file */    protected Vector mediaBlocks=new Vector();    /**	The location of this file */    protected URL location;    /** Options associated with this file */    protected String options=new String();        /**	Constructor     * newLocation - location of this file     */    public MediaFile(URL newLocation) {	location=newLocation;    }        /**	Constructor     * newLocation - location of this file     * newOptions - options associated with this file     */    public MediaFile(URL newLocation, String newOptions) {	this(newLocation);	options=newOptions;    }        /** Add a media block to the list associated with this file     * 	newMediaBlock - media block to add to the list     * 	Throws Exception if the object to add is not an instance of MediaBlock     */    public void addMediaBlock(MediaBlock newMediaBlock) throws Exception{	if(newMediaBlock instanceof MediaBlock) {	    mediaBlocks.addElement(newMediaBlock);	}	else {	    throw new Exception("Object must be an instance of MediaBlock");	}    }        /** Create a string representing this media file     */    public String toString() {	return(new String(mediaBlocks.size()+" media blocks"));    }        /**	Get a Vector containing all the media blocks associated with this file     */    public Vector getAllMediaBlocks() {	return((Vector)mediaBlocks.clone());    }}