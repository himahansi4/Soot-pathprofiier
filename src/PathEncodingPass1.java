import java.util.*;

import soot.*;
import soot.toolkits.graph.*;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;


public class PathEncodingPass1 extends BodyTransformer{

	protected void internalTransform(Body body, String phaseName, Map options) {
		//new MyAnalysis(new ExceptionalUnitGraph(body));
		SootMethod method = body.getMethod();
		  
        // debugging
        System.out.println("instrumenting method : " + method.getSignature());
  
        // get body's unit as a chain
        Chain units = body.getUnits();
     //   Node nodes = body.get
  
        // get a snapshot iterator of the unit since we are going to
        // mutate the chain when iterating over it.
        //
        Iterator stmtIt = units.snapshotIterator();
  
        // typical while loop for iterating over each statement
        while (stmtIt.hasNext()) {
  
          // cast back to a statement.
          Stmt child = (Stmt)stmtIt.next();
          System.out.println(child);
//          if (child instanceof IfStmt) {
//				IfStmt condition = (IfStmt) child;
//				Iterator branches = condition.getUnitBoxes().iterator();
//				System.out.println("units in boxes ");
//				//Iterator branches = condition.getBoxesPointingToThis().iterator();
//	        	while(branches.hasNext()){
//	        		UnitBox ub = (UnitBox) branches.next();
//	        		Unit branch = ub.getUnit();
//	        		System.out.println("units in boxes "+branch);
//	        		if (branch instanceof GotoStmt) {
//						GotoStmt gotoStatement = (GotoStmt) branch;
//						System.out.println("units in boxes "+branch);
//					}	        		
//	        	}
//				
//			}
          if (child instanceof GotoStmt) {
        	  GotoStmt condition = (GotoStmt) child;
        	  System.out.println("units in boxes "+condition);
				Iterator branches = condition.getUnitBoxes().iterator();
				//Iterator branches = condition.getBoxesPointingToThis().iterator();
	        	while(branches.hasNext()){
	        		UnitBox ub = (UnitBox) branches.next();
	        		Unit branch = ub.getUnit();
	        		if (branch instanceof GotoStmt) {
						GotoStmt gotoStatement = (GotoStmt) branch;
						System.out.println("units in boxes "+branch);
					}	        		
	        	}
				
			}
		
	}

}
}
