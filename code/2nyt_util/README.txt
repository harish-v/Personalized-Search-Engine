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

