

package com.id2222

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import scala.util.hashing.{MurmurHash3=>MH3}
import scala.math

class JaccardSimlarity(shingles_doc:Shingles,sc:SparkContext){
           println("jaccard similarity is starting")
                   
           println( "1st step")
      var str= StringBuilder.newBuilder                     
      def doJaccardSimilarity(file1:String,file2:String,i:Int,j:Int):(List[List[Int]],Double)={
            
           val file01:String=file1.takeRight(9)
           //println(file01)
          val file02:String= file2.takeRight(9)
          val hash_sorted_set=scala.collection.mutable.MutableList.empty[List[Int]]
          val (hash_file_set1,shingle_print_set1)=shingles_doc.doShingles(file1)
          val hash_file1=hash_file_set1
          val shingles1 = shingle_print_set1
          // println("jaccard similarity is starting::: hash_file1")
          val (hash_file_set2,shingle_print_set2)=shingles_doc.doShingles(file2)
          val hash_file2=hash_file_set2
          val shingles2 = shingle_print_set2
          val file1_list=  hash_file1.toList
          val file2_list=  hash_file2.toList
          val hash_code_sets=(hash_sorted_set += file1_list += file2_list).toList
          //println("File names are in side dojaccard:"+file1+file2)
           //println("hash_file1.size ::::"+hash_file1.size,+hash_file2.size)
          val fileitersection :Double=  (file1_list.intersect(file2_list)).distinct.size
           //println("fileitersection:::   "+fileitersection)
           
          val fileunion_distint=  (file1_list.union(file2_list)).distinct
          val union = fileunion_distint.size
           //println("fileunion:::   "+union)
          val jacc_sim: Double= fileitersection.toDouble/union.toDouble
                     
          val x= i+"  "+j+"  "+file01.toString() +" " + file02.toString() +"->" +" jaccarda simlirity is"+jacc_sim
          str.append(x)
          str.append("\n")
          if(i==5 && j==6)
          {
            val shingles_RDD= sc.parallelize(Seq(shingles1.map { x => x}))
            val hashcode_RDD= sc.parallelize(Seq(hash_file1))
            val shingles_RDD1= sc.parallelize(Seq(shingles2.map { x => x}))
            val hashcode_RDD2= sc.parallelize(Seq(hash_file2))
            shingles_RDD.saveAsTextFile("output_Shingles/"+file01)
            hashcode_RDD.saveAsTextFile("output_Hashcode/"+file01)
            shingles_RDD.saveAsTextFile("output_Shingles/"+file02)
            hashcode_RDD.saveAsTextFile("output_Hashcode/"+file02)
          }
          
          
          //print("calculated jaccard similarity" + jacc_sim)
           println("\n")
           
           return (hash_code_sets,jacc_sim)
          
        } 
      def print_jacc_sim()
      {
        println(str.toString())
      }
      
      
}