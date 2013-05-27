SquareOne
=========

Integration of multimedia descriptions of football matches (parsing MPEG video and text) - written in 2000 for my BSc - so BEWARE, depreciation city!

Identifies frame-breaks in video and different types of motion. Detects (crudely) pitches and goal-posts. Parses sports reports for timed events.

Operation of the Application
============================

For all applications detailed here require that a runtime Java environment 
of at least version 1.1 is installed on the system. For all applications the 
Jar files, Football.jar and MPEG_Play.jar 
(http://www.davidchatting.com/CSSE97/FinalProject/code/jars.html) 
should be added to the Java classpath.

The main application allowing the combination of report and video files is 
executed with the following command:

java uk.ac.bham.cs.djc.ui.Application MATCH_REPORT_FILE matchReport.txt 
FOOTBALL_MOVIE_FILE movie.mpg[,nmins]

The optional use of an n minute offset allows a movie starting in the nth 
minute of the match to be specified. If no arguments are given a file browser is 
opened to allow the user to open a previously saved data file. The application 
will by default save the data file interest.out for every new run. Video 
parsing is time consuming for long movies. For reasonable execution times (in the 
order of ten minutes) the length of the parsed sections of the movie should be 
limited to up to five minutes. Where a movie is specified without a report file, 
the whole movie will be parsed to find the single most interesting event. The 
references for files can be given as local filenames or remote URLs.

To run the main application QuickTime for Java must be installed, 
unfortunately this is currently only available for MacOS and Windows operating systems. 
This must be added to the classpath. In addition the Java Swing packages must 
be installed and added to the classpath. 

Applications demonstrating frame analysis and report parsing are executed 
with the following commands below. Both have the same requirements as the main 
application, except neither requires QuickTime for Java and so can be run a wider range 
of platforms.

java uk.ac.bham.cs.djc.ui.FrameAnalyser

java uk.ac.bham.cs.djc.ui.NewspaperDemo



David Chatting - 5th September 2000
