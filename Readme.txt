IR_Proj3_Web_V2:
~~~~~~~~~~~~~~~~

Dev Environment (recommendation)
1) latest version of Java SDK installed 1.8+
2) latest version of netbeans installed with the latest Tomcat web container (pre-bundled with netbeans download)
3) Import the application as maven based web application into netbeans.

In edu.ub.ir.oof1.Properties.java --> update the Solr server configurations.

Make sure Solr server is running.

Write click and run the project.

==================================================================================
NYT_Util_Final:
~~~~~~~~~~~~~~~

Dev Environment (recommendation)
1) latest version of Java SDK installed 1.8+
2) latest version of netbeans installed.
3) Import the application as maven based Java application into netbeans.

In edu.ub.ir.oof1.Properties.java --> update the Solr server configurations.

Make sure Solr server is running.

NYT Corpus data folder structure:
Assuming NYT corpus is in the current directory '.'
Make sure the NYT corpus sub-folder structure in the file system is as below and the files are present under the 2nd level sub-folders
 - ./01/01
 - ./01/02
 - ./01/::
 - ./01/25
 - ./02/01
Make sure the 1st level sub-folders are 01 to 06.
Make sure the 2nd level sub-folders are 01 to 31.

Write click and run the project and the argument is NYT corpus base directory.

==================================================================================
WikiNews_Reuters_Util:
~~~~~~~~~~~~~~~~~~~~~~

Dev Environment (recommendation)
1) latest version of Java SDK installed 1.8+
2) latest version of netbeans installed.
3) Import the application as Java based application into netbeans.

Make sure Solr server is running.

-----------------------------------------------------------------------
Reuters Corpus data folder structure:
Assuming Reuters corpus is in the current directory '.'
Make sure the NYT corpus sub-folder structure in the file system is as below and the files are present under the 1st level sub-folders
 - ./retail
 - ./trade
 - ./gas
 
Make sure the 1st level sub-folders are only from the above categories.
-----------------------------------------------------------------------

Generate the Jar and run the application using following command.
1) To run reuters corpus:
java -jar Reuters <retuers corpus folder location> <Solr Server URL>
Ex: java -jar Reuters /docs/reuters http://192.168.0.6:8983/solr

2) To run WikiNews corpus:
java -jar edu.ub.san.wiki.news.parser.WikiNewsParser <WikiNews corpus xml file location> <Solr Server URL>
Ex: java -jar edu.ub.san.wiki.news.parser.WikiNewsParser /docs/enwikinews-20140308-pages-articles-multistream.xml http://192.168.0.6:8983/solr

==================================================================================