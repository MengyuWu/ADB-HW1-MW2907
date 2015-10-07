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
			1) In Build.xml, update the following two lines to reflect the
			version of Java that you are using:
		 		<property name="target" value="1.6"/>
    	 			<property name="source" value="1.6"/>
    	 		2) In Build.xml, if fork="yes" in the line below, change fork="no"
    	 			<java classname="main.bingRun" failonerror="true" fork="no">

	3) ant bingRun -Dargs='<Bing Account Key> <Precision> <Query>'
	ex. ant bingRun -Dargs='pb71DGWbKoLI5Vki6bTSeAIM4otYkmdXMqSV+s/WvP0 0.9 taj mahal'

d). Internal Design:
constant.java:This class contains constants such as the Bing Account Key, the alpha/beta/gamma values for the 
Rocchio Algorithm, and the stop words to be filtered out.

bingRun.java: The main class. Its most prominent functions include taking and validating the user's arguments, 
running the iteration loop, and computing the terms to be added to the query string in the next iteration
(if necessary) based on the Rocchio Algorithm.

bingHelper.java: A helper class that uses the Bing API to retrieve the top ten documents matching the query. It 
forms a connection with the server, retrieves data using this connection, and converts the data into Document objects.

Document.java: Each document returned from the server is an instance of this class. This class contains methods for
showing document contents to the user (name, url, description), calculating term frequency and adding any new words
into the set of all terms, and calculating the tf-idf for each term in the document.

Term.java: Each term has a term (name) and weight.

AllTerms.java: Contains methods for calculating document frequency (the number of docs that have Term t), and keeps 
a set of all unique terms encountered...

QueryVector.java:


e). Query-modification Method:
The main algorithm is based on Rocchio Algorithm:
1. first build the vector. 
We did stop words elimination to ignore words. The full list can be seen in constant.java,
here is the first line:
	"a","an", "able","about","across","after","all","almost",

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
