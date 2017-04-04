import java.util.*;

import soot.*;
import soot.toolkits.graph.*;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;


public class PathEncodingPass extends BodyTransformer{

	protected void internalTransform(Body body, String phaseName, Map options) {
		new MyAnalysis(new ExceptionalUnitGraph(body));
		
	}

}

//class MyAnalysis extends ForwardFlowAnalysis
//{
//    FlowSet emptySet = new ArraySparseSet();
//    Map<Unit, FlowSet> unitToGenerateSet;
//
//    MyAnalysis(UnitGraph graph)
//    {
//        super(graph);
//        if(graph.iterator().hasNext()){
//        	Unit functionEntry = graph.iterator().next();
//        	recordEdge(functionEntry);
//        }
//
//    }
//    
//    protected void recordEdge(Unit parent)
//    {    	
//    	Iterator children = parent.getUnitBoxes().iterator();
//    	if(children == null){
//    		return;
//    	}
//    	while(children.hasNext()){
//    		UnitBox ub = (UnitBox) children.next();
//    		Unit child = ub.getUnit();
//    		System.out.println("child "+child);
//    	//s	recordEdge(child);
//    	}
//    }


class MyAnalysis extends ForwardFlowAnalysis
{
    FlowSet emptySet = new ArraySparseSet();
    Map<Unit, FlowSet> unitToGenerateSet;
    Map <Map <Unit, Unit>, Integer> edges ;

    MyAnalysis(UnitGraph graph)
    {
        super(graph);
        int j=0;
        Unit parent = null;
        edges = new HashMap();
        for(Iterator unitIt = graph.iterator(); unitIt.hasNext();){
        	 Unit child = (Unit) unitIt.next(); 
        	 j++;
        	System.out.println("iterartion "+j);
        	System.out.println("unit "+child);
        	
//        	if (child instanceof IfStmt) {
//				IfStmt condition = (IfStmt) child;
//				Iterator branches = condition.getUnitBoxes().iterator();
//				//Iterator branches = condition.getBoxesPointingToThis().iterator();
//	        	while(branches.hasNext()){
//	        		UnitBox ub = (UnitBox) branches.next();
//	        		Unit branch = ub.getUnit();
//	        		if (branch instanceof GotoStmt) {
//						GotoStmt gotoStatement = (GotoStmt) branch;
//						System.out.println("units in boxes "+branch);
//					}	        		
//	        	}
//				
//			}
//        	if (child instanceof GotoStmt) {
//        		GotoStmt gotoStatement = (GotoStmt) child;			
//				
//			}        if i == 3 goto label0;
        	if (parent != null){
        		Map <Unit,Unit> edge = new HashMap<>();
        		edge.put(parent, child);
        		edges.put(edge, 0);
        	}
        	//List labels = s.getUnitBoxes(); 
        	
        	parent = child;        	
        	System.out.println("end");
        }
        System.out.println("map");
        Iterator it = edges.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Map innerMap = (Map)pair.getKey();
            Iterator it1 = innerMap.entrySet().iterator();
            while (it1.hasNext()) {
            	 Map.Entry innerPair = (Map.Entry)it1.next();
            	 System.out.println(innerPair.getKey() + " ### " + innerPair.getValue()+"***"+pair.getValue());
            	 it1.remove();
            }            
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        
        
//        Iterator it = edges.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " ### " + pair.getValue());
//            it.remove(); // avoids a ConcurrentModificationException
//        }
        
    }

    /**
     * All INs are initialized to the empty set.
     **/
    protected Object newInitialFlow()
    {
        return emptySet.clone();
    }

    /**
     * IN(Start) is the empty set
     **/
    protected Object entryInitialFlow()
    {
        return emptySet.clone();
    }

    /**
     * OUT is the same as IN plus the genSet.
     **/
    protected void flowThrough(Object inValue, Object unit, Object outValue)
    {
        FlowSet
            in = (FlowSet) inValue,
            out = (FlowSet) outValue;

        // perform generation (kill set is empty)
        in.union(unitToGenerateSet.get(unit), out);
    }

    /**
     * All paths == Intersection.
     **/
    protected void merge(Object in1, Object in2, Object out)
    {
        FlowSet
            inSet1 = (FlowSet) in1,
            inSet2 = (FlowSet) in2,
            outSet = (FlowSet) out;

        inSet1.intersection(inSet2, outSet);
    }

    protected void copy(Object source, Object dest)
    {
        FlowSet
            sourceSet = (FlowSet) source,
            destSet = (FlowSet) dest;

        sourceSet.copy(destSet);
    }
}