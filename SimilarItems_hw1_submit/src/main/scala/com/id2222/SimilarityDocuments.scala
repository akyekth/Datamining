package com.id2222
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import scala.util.hashing.{MurmurHash3=>MH3}

object SimilarityDocuments {
  
  def main(args: Array[String]) = 
  {
    
    //Start the Spark context
   val conf = new SparkConf()
      .setAppName("SimilarItems.Shingles.documents")
      .setMaster("local")
   val sc = new SparkContext(conf)
   //Read list of file from Data folder    
   val data1 = sc.wholeTextFiles("Data1/")
   val files = data1.map { case (filename, content) => filename}
   val shingles_doc=new Shingles(sc)
   val fiename_array=files.collect().toArray
   val hash_set_matrix = scala.collection.mutable.MutableList.empty[List[Int]]
   val signature_matrix = scala.collection.mutable.MutableList.empty[List[Int]]
   val jaccard_sim= new JaccardSimlarity(shingles_doc,sc)
   
   val n=fiename_array.length
   for(i<- 0 to n-1 )
    {
     for(j<- i+1 to n-1 )
     {
      val (hash_set,jaccsim) = jaccard_sim.doJaccardSimilarity(fiename_array(i),fiename_array(j),i,j)
      val hash_sets=hash_set
      if((i+1)==j)
       {
        if(i==0)
         {
           hash_set_matrix += hash_sets(0)
         }
       hash_set_matrix += hash_sets(1)
       }
      
     } 
    }
   val hash_sets_matrix=(hash_set_matrix.toList)
   val hash_sets_matrix_size=hash_sets_matrix.size
   
   /**
    * Minhash class started
    */
   val minHash= new MinHash(shingles_doc)
   /**
    * hashcode() returns coefficient values of hashcode
    * to generate diff hash functions
    * example 6 documents generate 5 hash functions
    * 
    */
   val coeff_list= minHash.doHash(n-1)
   println("hash_sets_matrix.size::::::::"+hash_sets_matrix.size)
   
   /**
    * for each document each shingle apply n hashcodes
    * it seems doc-> n- shingles -> n * n-1 hashcodes
    */
   for(i <- 0 to (hash_sets_matrix.size-1)){
          
     val signature_value= minHash.dominHash(hash_sets_matrix(i),coeff_list,n-1)
     signature_matrix += signature_value
     val signature_length=signature_value.length
   }
   
   val signatures_matrix=signature_matrix.toList
   println("signatures_matrix.size::::::."+signatures_matrix.size)
   
   val compare_sign_object=new CompareSignature()
   val sim_between_vectors=scala.collection.mutable.MutableList.empty[Double]
   
   
   /**
    * to compare similarity between two vectors
    * comparetwovetor()
    * 
    */ 
  // println("\n")
   
   //shingles_doc.doPrint()
   // print jaccard similarity
     println("\n")
   println("===========Jaccard Similarity==========")
   println("\n")
   jaccard_sim.print_jacc_sim()
   println("\n")
   println("===========MinHash_Jaccard Similarity==========")
   println("\n")
   
   for(i<- 0 to (signatures_matrix.length-1))
   {
       for(j<- i+1 to (signatures_matrix.length-1) )
       {
        val sim_between_vector=compare_sign_object.comparetwovectors(signatures_matrix(i), signatures_matrix(j))
        sim_between_vectors += sim_between_vector
         
         print("MinHash jacard similarity between two minhash signatures is:::::::: "+i)
         print("->"+j)
         print("::::::::::" +sim_between_vector)
            println("\n")
       }
       println("\n")
  }
   
   val minhash_sim_vector=sim_between_vectors.toList
   
   /**
    * LSH calculation
    */
   val bands=2
   val t=0.6
   val lsh=new LSH(t,bands,minHash,jaccard_sim)
   lsh.doCompute_LSH(signatures_matrix,fiename_array)
   println(":::::::::::::::::::LSH _Jaccard_Similarity:::::::::::::::::::")
   println("-------------candidate pairs with threshold- --------t="+t)
   lsh.print_jacc_sim()
  } 
   
  }
