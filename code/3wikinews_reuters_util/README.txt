Dev Environment (recommendation)
1) latest version of Java SDK installed 1.8+
2) latest version of netbeans installed.
3) Import the application as Java based application into netbeans.

Make sure Solr server is running.

===========================================================
Reuters Corpus data folder structure:
Assuming Reuters corpus is in the current directory '.'
Make sure the NYT corpus sub-folder structure in the file system is as below and the files are present under the 1st level sub-folders
 - ./retail
 - ./trade
 - ./gas
 
Make sure the 1st level sub-folders are only from the above categories.
===========================================================

Generate the Jar and run the application using following command.
1) To run reuters corpus:
java -jar Reuters <retuers corpus folder location> <Solr Server URL>
Ex: java -jar Reuters /docs/reuters http://192.168.0.6:8983/solr

2) To run WikiNews corpus:
java -jar edu.ub.san.wiki.news.parser.WikiNewsParser <WikiNews corpus xml file location> <Solr Server URL>
Ex: java -jar edu.ub.san.wiki.news.parser.WikiNewsParser /docs/enwikinews-20140308-pages-articles-multistream.xml http://192.168.0.6:8983/solr


