package com.id2222

class LSH(s:Double,noofbands:Int,minHash:MinHash,jaccard_sim:JaccardSimlarity)
{
  /**
   * s=0.8,noofbands=2
   */
  var str= StringBuilder.newBuilder 
  val n= 6
  val p=65563
  def doCompute_LSH(signatures_matrix:List[List[Int]],fiename_array:Array[String])={
    /**
     * hash functions-> to bands
     * signature matrix into 2 parts means 2bands
     * in this example 7 documents 6 hashcodes
     * each row 
     */
    val signature_length=n
    val partition= signature_length/2
    val band1 = scala.collection.mutable.MutableList.empty[List[Int]]
    val bucket1=scala.collection.mutable.MutableList.empty[Int]
    val band2 = scala.collection.mutable.MutableList.empty[List[Int]]
    val bucket2 = scala.collection.mutable.MutableList.empty[Int]
    //val lsh_jsim=scala.collection.mutable.MutableList.empty[Double]
    val hash_set_matrix = scala.collection.mutable.MutableList.empty[List[Int]]
    val candidatepairs = scala.collection.mutable.MutableList.empty[List[Int]]
    for(i<- 0 to (signatures_matrix.length-1))
     {
         band1 += signatures_matrix(i).take(partition)
         band2 += signatures_matrix(i).takeRight(partition)
      }
    
      val coeffs=minHash.doHash(noofbands)
     
      
    for(i<-0 to (band1.length-1))
    {
      val band1_hash= doHash(2,3,band1(i),p)
      val band2_hash= doHash(5,7,band1(i),p)
      bucket1 +=band1_hash
      bucket2 +=band2_hash
      
    }
    val candidate_pair_band1= doCandidatepair(bucket1.toList)
    val candidate_pair_band2=doCandidatepair(bucket1.toList)
    val candidate_pair_combined= candidate_pair_band1::: candidate_pair_band2
    val candidate_pair_set=candidate_pair_combined.distinct
    println("candidate pairs"+candidate_pair_set.toString() )
    for(i<- 0 to candidate_pair_set.length-1 )
    {
       val x= candidate_pair_set(i)
       val file1=fiename_array(x(0))
       val file2=fiename_array(x(1)) 
     
     val (hash_set,jaccsim) = jaccard_sim.doJaccardSimilarity(file1, file2, x(0), x(1))
     val hash_sets=hash_set
     val lsh_jsim =jaccsim
     
     if((x(0)+1)==x(1))
       {
        if(x(0)==0)
         {
           hash_set_matrix += hash_sets(0)
         }
       hash_set_matrix += hash_sets(1)
       }
      if(lsh_jsim >s)
      {
        
       candidatepairs += candidate_pair_set(i)
       str.append("\n")
       val z=  "candiadte pairs with threshold:::::::"+x(0)+" -> "+x(1)
       str.append(z)
       str.append("\n")
       val y= x(0)+"  "+x(1)+"->" +"LSH_ jaccarda simlirity is"+jaccsim
       str.append(y)
       str.append("\n")
      }    
    }
    
  val hash_sets_matrix=(hash_set_matrix.toList)
  val hash_sets_matrix_size=hash_sets_matrix.size
  
  }
  
  
  
  def doHash(a:Int,b:Int,band:List[Int],p:Int):Int={
      var signature=scala.collection.mutable.MutableList.empty[Int]
      var minHashCode= p+1
      for(i<-0 to band.length-1){
        val band_hashed_hash= (a*band(i)+b)% p
        if (band_hashed_hash < minHashCode){
      			minHashCode = band_hashed_hash
          }
        
      	}
      //signature+=(minHashCode)
   return minHashCode
  }
  
  
  def doCandidatepair(bucket1:List[Int]):List[List[Int]]=
  {
     val candidatepair=scala.collection.mutable.MutableList.empty[List[Int]]
    for(i<- 0 to bucket1.length-1 )
    {
      for(j<- i+1 to bucket1.length-1 )
      {
      if(bucket1(i)==bucket1(j))
      {
        candidatepair+= List(i,j)
      }
      }
    }
    return candidatepair.toList
  }
  def print_jacc_sim()
    {
       
        println(str.toString())
    }
  
}