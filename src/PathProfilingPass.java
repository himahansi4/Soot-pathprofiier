import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.util.Chain;


public class PathProfilingPass extends BodyTransformer {
	 static SootClass counterClass;
     static SootMethod increaseCounter, reportCounter;
// 
//     static {
//       counterClass    = Scene.v().loadClassAndSupport("Runtime");
//       increaseCounter = counterClass.getMethod("void increase(int)");
//       reportCounter   = counterClass.getMethod("void report()");
//     }
     protected void internalTransform(Body body, String phase, Map options) {
         // body's method
         SootMethod method = body.getMethod();
   
         // debugging
         System.out.println("instrumenting method : " + method.getSignature());  
         
     }
}
