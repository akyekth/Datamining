package com.id2222

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import scala.util.hashing.{MurmurHash3=>MH3}
import scala.collection.immutable.SortedSet

class Shingles(sc:SparkContext){

   var str= StringBuilder.newBuilder
   def doShingles(file:String): (SortedSet[Int],List[Seq[String]]) ={ 

       //println (file);
       
       /**
        * read the file name from file
        */
       
       val i = file.takeRight(9)
       val doc_data = sc.textFile(file);
       val k=5
       /**
        * doc1 is RDD and convert the rdd to string by using
        * toArray() and mkstring 
        */
       val doc1 = doc_data.flatMap(line => line.split(" ")).collect()
       val doc2=doc1.mkString(" ")
       
       /**
        * eliminate unwanted charcters and spaces
        *
        */
       // now this is 5 characters as a shingle
       // but we can use the same for words also just delete \\s+ amd mkstring("")
       var pattern= "[\t\n\r*.!:()\\s+]".r
       val str1=(pattern replaceAllIn(doc2,"")).mkString("").split("")
       /**
        * bags contains duplicate values but shingles didnt contain duplicates
        * 
        *shingle is based on words:: {(word1,word2),(word2,word3),(word3,word4)}
        */
       //doc1.map { x => println(x)}
       val bags= str1.iterator.sliding(k).toList
       val shingle=bags.distinct
       val shingles_print= shingle.take(3)
       str.append(shingles_print.toString)
      
       //shingles_print.foreach { x => println(x.mkString(",")) }
       //var x = shingles.map(i=>i.map(x=>MH3.stringHash(x,seed)))
       
       /**
        * genearate hash value for each shingle 
        * make hah code as ordered set by using SortedSet
        */
       val shi_hashcode= shingle.map(i=>i.hashCode()).toSet
       val hashcode = collection.immutable.SortedSet[Int]() ++ shi_hashcode
       val hashcode_print=hashcode.take(3)
       str.append("\n")
       str.append(hashcode_print.mkString(","))
       
       /**
        * convert string into RDD because 
        *String didnt save as a text file but rdd will do 
        */
       
      // shingles_RDD.map { x => x.map{y=> println(y)}}
       
       /**
        *  collect all shingles and hashcode of those letters
        *  put it in respective folders
        */
//      println("::::: Shingles of file::::::::")
//      shingles_RDD.collect.take(5).foreach {println }
//      println(":::::RDD hashcode of shingles of file::::::::")
//      hashcode_RDD.collect.take(5).foreach(println)
//      
       
       //shingles_RDD.saveAsTextFile("output_Shingles/"+i)
       //hashcode_RDD.saveAsTextFile("output_Hashcode/"+i)
       
       return (hashcode,shingle)
       
      
    }
   def doPrint()
   {
     println("::::::::::  Shingles the document and hashcode::::::::::")
     println(str.toString())
   }
        
   
   
}
       
