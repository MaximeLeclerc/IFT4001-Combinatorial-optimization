import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.util.objects.graphs.DirectedGraph;
import org.chocosolver.util.objects.setDataStructures.SetType;
import org.chocosolver.solver.*;
import org.chocosolver.solver.trace.*;
import org.chocosolver.solver.search.limits.*;
import org.chocosolver.solver.search.loop.monitors.*;

public class PremierProbleme {
	public static final int NOMBRE_DE_COULEURS = 4;
	
	public static final int JAUNE = 0;
	public static final int ROUGE = 1;
	public static final int BLEU = 2;
	public static final int VERT = 3;
	
    public static void main(String[] args) {
    	
    	
    	// Creation du solveur
        Solver solver = new Solver();

        // Creation des graphes de NOMBRE_DE_COULEURS sommets pour chacun des quatre cubes
        DirectedGraph graphe1 = new DirectedGraph(solver, NOMBRE_DE_COULEURS, SetType.BITSET, true);
        graphe1.addArc(JAUNE, VERT);
        graphe1.addArc(ROUGE, BLEU);
        graphe1.addArc(VERT, ROUGE);
        
        DirectedGraph graphe2 = new DirectedGraph(solver, NOMBRE_DE_COULEURS, SetType.BITSET, true);
        graphe2.addArc(VERT, VERT);
        graphe2.addArc(ROUGE, BLEU);
        graphe2.addArc(JAUNE, BLEU);
        
        DirectedGraph graphe3 = new DirectedGraph(solver, NOMBRE_DE_COULEURS, SetType.BITSET, true);
        graphe3.addArc(BLEU, ROUGE);
        graphe3.addArc(JAUNE, JAUNE);
        graphe3.addArc(JAUNE, VERT);
        
        DirectedGraph graphe4 = new DirectedGraph(solver, NOMBRE_DE_COULEURS, SetType.BITSET, true);
        graphe4.addArc(JAUNE, ROUGE);
        graphe4.addArc(ROUGE, VERT);
        graphe4.addArc(BLEU, JAUNE);
        
        Chatterbox.printStatistics(solver);
    }
}
