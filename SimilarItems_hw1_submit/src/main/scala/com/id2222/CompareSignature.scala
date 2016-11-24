package com.id2222

class CompareSignature{
	
	def comparetwovectors(doc1:List[Int],doc2:List[Int]):Double= {
		
	  var counter:Double=0
	  for(i<- 0 to doc1.length-1)
	  {
	  
	    if(doc1(i) == doc2(i))
	    {
	      counter +=1
	    }
	  
	  }
	  return counter/(doc1.length).toDouble
	  
	  
//	  val intersection=((doc1.intersect(doc2)).length).toDouble
//		//println("Minhash _ Similarity intersection::::::::::"+intersection)
//		val union =((doc1.union(doc2)).distinct.length).toDouble
//		val Minhash_sim:Double=intersection/union
//		return Minhash_sim
		
	}
  
}
