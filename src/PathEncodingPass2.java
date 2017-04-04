import java.util.*;
import java.util.Map.Entry;

import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;


public class PathEncodingPass2 extends BodyTransformer{
	protected void internalTransform(Body body, String phaseName, Map options) {
		new MyAnalysis( new ExceptionalUnitGraph(body));		
	}
}

class MyAnalysis 
{
    Map <Edge,Integer> edges ;
	Map <Unit,Integer> numPaths = new HashMap();
	UnitGraph graph;
	Map <Unit,Integer> numbrancesForUnit = new HashMap();

    MyAnalysis(UnitGraph graph)
    {       
    	this.graph = graph;
       // Unit parent = null;
        edges = new HashMap();
        
        System.out.println("Method "+ graph.getBody().getMethod().getSubSignature());
        //Storing the edges of the graph
        System.out.println("Creating edges map");
        Iterator headsIt = graph.getHeads().iterator();
        while(headsIt.hasNext()){
        	Unit head = (Unit) headsIt.next();
        	recordEdge(head,edges);
        }
//        //Annotate all exit node out of the graph with 1       
//        Iterator tailsIt1 = graph.getTails().iterator();
//        while(tailsIt1.hasNext()){
//        	Unit tail = (Unit) tailsIt1.next();
//        	numPaths.put(tail, 1);
//        } 
//      displayResults();
        //Since all the exit are annotated, annotate every node starting from tails until heads
        Iterator tailsIt2 = graph.getTails().iterator();
        while(tailsIt2.hasNext()){
        	Unit tail = (Unit) tailsIt2.next();
        	//displayResults();
        	calculateNumPaths(tail);
        } 
        displayResults();
        //Annotate edges
//        System.out.println("Annotating edges");
//        Iterator graphIt =	graph.iterator();          
//        while(graphIt.hasNext()){
//        	Unit unit = (Unit) graphIt.next();
//        	Map <Unit, Integer> mapChildrenNumPaths = new HashMap();
//        	List<Unit> children = graph.getSuccsOf(unit);
//        	for (Unit child : children){
//        		Edge edge = new Edge(unit, child);        		
//        		if(edges.get(edge)> 0){
//        			System.out.println("parent****** "+ edge.parent);
//            		System.out.println("child ******"+edge.child);    		
//        			//Units children already annotated
//        			break;
//        		}
//        		mapChildrenNumPaths.put(child ,numPaths.get(child));
//        	}
////        	//annotateEdges(unit,mapChildrenNumPaths);
//        }        
//        displayResults();

    }
    
    protected void annotateEdges(Unit parent,Map <Unit, Integer>  mapChildrenNumPaths) {
    	ValueComparator bvc = new ValueComparator(mapChildrenNumPaths);
        TreeMap<Unit, Integer> sorted_map = new TreeMap<Unit, Integer>(bvc);
        
        sorted_map.putAll(mapChildrenNumPaths);      
        int annotation = 0;
        Iterator it = sorted_map.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pair = (Map.Entry)it.next();
        	Edge edge = new Edge(parent, (Unit) pair.getKey());
        	if(edges.containsKey(edge)){
        		System.out.println("HAs key");
        		System.out.println("parent "+ parent);
        		System.out.println("child "+(Unit) pair.getKey());
        		System.out.println();
        	}
        }
	}    
    
//    protected void annotateEdges(Unit parent,Map <Unit, Integer>  mapChildrenNumPaths) {
//    	ValueComparator bvc = new ValueComparator(mapChildrenNumPaths);
//        TreeMap<Unit, Integer> sorted_map = new TreeMap<Unit, Integer>(bvc);
//        
//        sorted_map.putAll(mapChildrenNumPaths);      
//        int annotation = 0;
//        for (Entry<Unit, Integer> childValuePair : sorted_map.entrySet()) {
//        	Unit child = (Unit) childValuePair.getKey();
//        	Edge edge = new Edge(parent, child);
////        	if(edges.containsKey(edge)){
////        		System.out.println("HAs key");
////        		System.out.println("parent "+ parent);
////        		System.out.println("child "+child);
////        		System.out.println();
////        	}
//    		System.out.println("parent "+ parent);
//    		System.out.println("child "+child);
//    		if(edges.containsKey(edge)){
//    			System.out.println("HAs key");
//        		System.out.println("parent "+ parent);
//        		System.out.println("child "+child);
//        		System.out.println();
//    		}
////        	int val = edges.get(edge);
////        	System.out.println("HAs key: value "+ val);
//        	edges.put(edge,annotation);
//        	annotation = annotation + childValuePair.getValue();
//        }
//        //System.out.println("results: " + sorted_map);
//	}
    
    protected void calculateNumPaths(Unit unit){ 
    	List <Unit> parents = graph.getPredsOf(unit);  	 	
   	  	if(parents == null){	
   	  		return;
   	  	} 
   	  	for (Unit parent : parents) {
		  	int sum = 0;
		  	if(numPaths.containsKey(parent)){
		  		sum = numPaths.get(parent);
		  	}
		  	numPaths.put(parent,sum+numbrancesForUnit.get(unit));		
		}   
   	  	for (Unit parent : parents) {	
			calculateNumPaths(parent);
		}   			
   	  	 	  	
   	}
    
//    protected void calculateNumPaths(Unit unit){ 
//    	System.out.println("Unit*** "+ unit);
//    	List <Unit> parents = graph.getPredsOf(unit);  	 	
//   	  	if(parents == null){	
//   	  		System.out.println(" no parent ");
//   	  		return;
//   	  	} 
//   	  	System.out.println("***1");
//   	  	for (Unit parent : parents) {
//   	  		System.out.println("***2");
//	   	  	List <Unit> children = graph.getSuccsOf(parent);
//	   	  	System.out.println("***3");
//		  	int sum = 0;	
//		  	for(Unit child : children){ 		  		
//		  		System.out.println("***4 child : "+child);
//		  		displayResults();
//		  		sum = sum + numPaths.get(child);
//		  	}
//		  	System.out.println("***7");
//		  	numPaths.put(parent, sum); 	  		
//		}   
//   	 System.out.println("***6");
//   	  	//try {
//   	  		for (Unit parent : parents) {	
//   	  			System.out.println("parent "+parent); 
//		  		calculateNumPaths(parent);
//		  	}   			
//		//} catch (Exception e) {
//		//	System.out.println("*****error****"+e.toString()); 
//		//}
//   	  	 	  	
//   	}
   
  protected void recordEdge(Unit parent,Map edges)
  {
  	List <Unit> children = graph.getSuccsOf(parent);  	
  	if(children == null){
  		numbrancesForUnit.put(parent,1);
  		return;
  	}
  	numbrancesForUnit.put(parent,children.size());
  	for(Unit child : children){  		
//  		System.out.println("parent "+ parent);
//  		System.out.println("child "+children.get(i));
//  		System.out.println();
		Edge edge = new Edge(parent,child);
		edges.put(edge, 0);
  		recordEdge(child,edges);
  	}  	
  }
  protected void displayResults(){
//    System.out.println("Edges Map");
//    Iterator it = edges.entrySet().iterator();
//    while (it.hasNext()) {
//    	Map.Entry pair = (Map.Entry)it.next();
//    	Edge e = (Edge)pair.getKey();
//    	System.out.println("parent "+ e.parent);
//    	System.out.println("child "+ e.child);
//    	System.out.println("count "+ pair.getValue()+"\n");
//    }
//    System.out.println("Node Map");
//    Iterator it1 = PathEncodingPass2.numbrancesForUnit.entrySet().iterator();
//    while (it1.hasNext()) {
//    	Map.Entry pair = (Map.Entry)it1.next();
//    	System.out.println("node "+ pair.getKey());        	
//    	System.out.println("count "+ pair.getValue()+"\n");
//    }
    System.out.println("Num Paths Map");
    Iterator it3 = numPaths.entrySet().iterator();
    while (it3.hasNext()) {
    	Map.Entry pair = (Map.Entry)it3.next();
    	System.out.println("node "+ pair.getKey());        	
    	System.out.println("count "+ pair.getValue()+"\n");
    	//it3.remove(); // avoids a ConcurrentModificationException
    }
  }

}
class Edge {
	Unit parent;
	Unit child;
	Edge(Unit parent, Unit child){
		if(parent == null || child == null){
			System.out.println("Parent or child null@@@");
		}
		this.parent = parent;
		this.child = child;
	}
	@Override public boolean equals(Object obj) {
		if(obj == null){
			System.out.println("Object null"+ obj);
		}
		Edge edge = (Edge)obj;
		if(edge.parent == null || edge.child == null){
			System.out.println("Parent or child null");
		}
		if(this.parent == edge.parent && this.child == edge.child){
			return true;
		}
		return false;
	}
	@Override public int hashCode() {
//		System.out.println("parent ******"+this.parent);
//		System.out.println("child ******"+this.child);
//		System.out.println("hashcode ******"+this.parent.hashCode() + this.child.hashCode());
		return this.parent.hashCode() + this.child.hashCode();
		
	}
//	public Edge cast(Object object) {
//		return (Edge)(object);
//	}
}

class ValueComparator implements Comparator<Unit> {
    Map<Unit, Integer> base;

    public ValueComparator(Map<Unit, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    //Sort in the Descending order
    public int compare(Unit a, Unit b) {
        if (base.get(a) <= base.get(b)) {
            return -1;
        } else { 
            return 1;
        } // returning 0 would merge keys
    }
}