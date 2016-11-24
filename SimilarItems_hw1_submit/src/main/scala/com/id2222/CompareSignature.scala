package com.id2222

class CompareSignature{
	
	def comparetwovectors(doc1:List[Int],doc2:List[Int]):Double= {
		val intersection=((doc1.intersect(doc2)).length).toDouble
		//println("Minhash _ Similarity intersection::::::::::"+intersection)
		val union =((doc1.union(doc2)).distinct.length).toDouble
		val Minhash_sim:Double=intersection/union
		return Minhash_sim
		
	}
  
}