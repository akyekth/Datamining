package com.id2222

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import scala.util.hashing.{MurmurHash3=>MH3}
import scala.math
import scala.collection.immutable.SortedSet
import scala.collection.immutable.Vector
import scala.util.Random

class MinHash(shingles_object: Shingles){
	
   /**
    * we need next largest prime number to maxShingleid
    * pick the random value of prime number in this site
    * http://compoasso.free.fr/primelistweb/page/prime/liste_online_en.php
    */
	 val p=65563
   /**
   * generate hash function for entire set
   * 
   */
  def doHash(n:Int):List[List[Int]]=
  {
     /**
        * * maximum shingleId =655536
        * */
       val maxShingleId=(scala.math.pow(2, 16).toInt)-1
       
       
       def dopickRandom(n:Int):List[Int] =
       {
         val randList=scala.collection.mutable.MutableList.empty[Int]
         
         var k= n
         while(k > 0)
   		    {
     			  var randIndex=Random.nextInt(maxShingleId)
     		  	while(randList.contains(randIndex))
     			  {
     				  randIndex = Random.nextInt(maxShingleId)
     				
     			  }
     			  randList += randIndex
     			   k=k-1
   		    }
   		   // println("DoHash().................")
   		   // println("randomlist  length is:"+randList.length)
   		    return randList.toList 
   		  } 
       val coeffiecients_list_AB=scala.collection.mutable.MutableList.empty[List[Int]]      
      // coeffA and coeffB are list of int.list contains n=hash numbers
      val coeffA= dopickRandom(n)
      coeffiecients_list_AB +=coeffA
      //println("length is:"+coeffA.length)
     // coeffA.map { x => println(x) }
      val coeffB= dopickRandom(n)
      coeffiecients_list_AB +=coeffB
      
      return coeffiecients_list_AB.toList
  }
   def dominHash(hashed_set:List[Int],coeff_list:List[List[Int]],n:Int):List[Int] =
   {
       
      /*
       * hash function (ax+b)% p a
       * @a and b are coefficients
       * x data 
       * by using mumurhash3 also we can find hash value
       * by varieying seed value
       * for each seed value it maintain one hash function
       * apply that hash function on all shingles 
       * 2 hash function with another seed value and apply on all shingles
       * process is going on
       * 
       */
       val coeffA=coeff_list(0)
       val coeffB=coeff_list(1)
       val hashed_shingles=hashed_set 
       var signature=scala.collection.mutable.MutableList.empty[Int]
      
      //pick random coefficients of n hash functions 
      /**
       * generating minhash signatures for all documents
       */
      for( i <- 0 to n-1)
      {
      	var minHashCode= p+1
      	for(shingle<-hashed_shingles)
      	{
      		
      		val hashcode=(coeffA(i)*shingle+coeffB(i))% p
      		//println("MinHash function strated.hashcode.."+hashcode)
      		if (hashcode < minHashCode)
      			minHashCode = hashcode
      	}
      	signature+=(minHashCode)
      	
      //signatures.++:(signature)
      //	signature.foreach { x => println(x) }
      }
     
      return signature.toList
   }// end of the dominhash()
}