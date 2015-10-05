a). Mengyu Wu: mw2907  Melanie Hsu: mlh2197 

b). Files Submitted:
Src:
main/bingRun.java
constant/constant.java: all the constant parameters
entity/Document.java 
entity/Term.java
helper/bingHelper.java
vectors/AllTerms.java
vectors/QueryVector.java
lib:
commons-codec-1.10.jar
json-20140107.jar

c). How to Run Project:
To run: 
	1) ant clean 
	2) ant

	If you receive something similar to the following error:
		[javac] javac: invalid target release: 1.7 
		Please do the following:
			1) Get current version of Java using this command: java -version	
			2) In Build.xml, update the following two lines to reflect the
			version of Java that you are using:
		 		<property name="target" value="1.6"/>
    	 		<property name="source" value="1.6"/>

	3) ant bingRun -Dargs='<Bing Account Key> <Precision> <Query>'
	ex. ant bingRun -Dargs='pb71DGWbKoLI5Vki6bTSeAIM4otYkmdXMqSV+s/WvP0 0.9 gates'

d). Internal Design:

e). Query-modification Method:
The main algorithm is based on Rocchio Algorithm:
1. first build the vector. 
We did stop words elimination to ignore words: ("a","an","the","at","from","of").
The words set is all the words from titles and descriptions of snippets that Bing returns.
Then build the query vectors, and document vectors.
In the query vectors, if that term appears in the query set the weight to be 1, otherwise 0
In the document vectors, the weight is calculated as W=tf*log(N/df), tf is the term frequency 
int each document, N is the number of Documents, and df is the document frequency for each term.
we pick constants parameter for Rocchio Algorithm, alpha=1.0 beta=0.75 gamma=0.15
Then we calculated the query vector for the next iteration. 
Sort all the terms in descedning order according to the new calculated weights.
Pick two new words that do not belong to the previous query terms.


f). Bing Search Account Key: pb71DGWbKoLI5Vki6bTSeAIM4otYkmdXMqSV+s/WvP0

g). Additional Info: 
