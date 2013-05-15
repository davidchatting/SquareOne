//9th December 1999package uk.ac.bham.cs.djc.MPEG_Play;import de.tuchemnitz.informatik.MPEG_Play.*;import java.awt.Graphics;import java.awt.Image;import java.awt.Color;import java.io.InputStream;/**	The MPEG_Play classes depend on having a canvas on which to display the movie, an instance of MPEG_Play. * 	This class extends MPEG_Play, but removes all functionality allowing decoding of the stream without display. * 	 * 	<a href="MPEG_Play_null.java">Source code</a> * 	@author David Chatting (djc@cs.bham.ac.uk) *	@version Created 9th December 1999 */public class MPEG_Play_null extends MPEG_Play {    	/**	Empty constructor    	 */    	MPEG_Play_null(InputStream s, myFrame f, boolean ud) {	}		/**	Empty constructor	 */	public MPEG_Play_null() {	}	/**	Empty open_url()	 * 	@returns always false	 */	protected boolean open_url() {	    return(false);	}					   	/**	Empty start()	 */	public void start() {	}	/**	Empty stop()	 */	public void stop() {	}	/**	Empty close_chain()	 */	public void close_chain() {	}	/**	Empty run()	 */	public void run() { 	}		/**	set_dim() is not disabled or altered	 */	public void set_dim (int width, int height, int o_width, int o_height) {	    	super.set_dim(width,height,o_width,o_height);	}	/**	Empty set_Pixels()	 */	public void set_Pixels(int Pixels[][], int f_idx, int f_type) {	}	/**	Empty update()	 */   	public void update(Graphics g) {	}	/**	Empty paint()	 */	public void paint(Graphics g) {	}		/**	Empty main()	 */	public static void main( String args[] ) {	}}