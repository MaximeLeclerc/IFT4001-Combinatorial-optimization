import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.util.objects.graphs.MultivaluedDecisionDiagram;
import org.chocosolver.solver.*;
import org.chocosolver.solver.trace.*;
import org.chocosolver.solver.search.limits.*;
import org.chocosolver.solver.search.loop.monitors.*;

public class PremierProbleme {
	
	public static final int INVERSE = 2;

	public static final int JAUNE = 1;
	public static final int ROUGE = 2;
	public static final int BLEU = 3;
	public static final int VERT = 4;
	
	public static final int HEURISTIQUE_DEFAUT = 0;
    public static final int HEURISTIQUE_DOMOVERWDEG = 1;
    public static final int HEURISTIQUE_IMPACT_BASED_SEARCH = 2;

    public static final int RESTART_AUCUN = 0;
    public static final int RESTART_LUBY = 1;
    public static final int RESTART_GEOMETRIQUE = 2;
	
    public static final int COHERENCE_DE_BORNES = 0;
    public static final int COHERENCE_DE_DOMAINE = 1; 
	
    public static void main(String[] args) {
    	
    	final int n = 4;
    	
    	final int coherence = COHERENCE_DE_BORNES;
    	final int heuristique = HEURISTIQUE_DEFAUT;
        final int restart = RESTART_AUCUN;
        final boolean bris_symetries = false;
    	
    	// Creation du solveur
        Solver solver = new Solver();

        // Creation d'une matrice de dimensions NOMBRE_DE_COTES x n de variables dont les domaines sont les entiers de 1 a n.
        IntVar[][] lignesAvantArriere;
        if (coherence == COHERENCE_DE_BORNES)
        	lignesAvantArriere = VariableFactory.boundedMatrix("x", INVERSE, n, 1, n, solver);
        else
        	lignesAvantArriere = VariableFactory.enumeratedMatrix("x", INVERSE, n, 1, n, solver);
        
        // Ajout des contraintes forcant chacun des elements d'une ligne a avoir une couleur differente
        for (int i = 0; i < INVERSE; i++) {
        	if (coherence == COHERENCE_DE_BORNES)
                solver.post(IntConstraintFactory.alldifferent(lignesAvantArriere[i], "BC"));
            else
                solver.post(IntConstraintFactory.alldifferent(lignesAvantArriere[i], "AC"));
        }
        
        // Creation de la tranpose de la matrice lignes.
        IntVar[][] colonnes = new IntVar[n][INVERSE];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < INVERSE; j++) {
                colonnes[i][j] = lignesAvantArriere[j][i];
            }
        }
        
        // Ajout des couleurs par l'oppose du cube 1
        Tuples tuples1 = new Tuples();
        tuples1.add(JAUNE, VERT);
        tuples1.add(ROUGE, BLEU);
        tuples1.add(VERT, ROUGE);
        MultivaluedDecisionDiagram mdd1 = new MultivaluedDecisionDiagram(colonnes[0], tuples1);
        solver.post(IntConstraintFactory.mddc(colonnes[0], mdd1));
        
        // Ajout des couleurs par l'oppose du cube 2
        Tuples tuples2 = new Tuples();
        tuples2.add(VERT, VERT);
        tuples2.add(ROUGE, BLEU);
        tuples2.add(JAUNE, BLEU);
        MultivaluedDecisionDiagram mdd2 = new MultivaluedDecisionDiagram(colonnes[1], tuples2);
        solver.post(IntConstraintFactory.mddc(colonnes[1], mdd2));
        
        // Ajout des couleurs par l'oppose du cube 3
        Tuples tuples3 = new Tuples();
        tuples3.add(BLEU, ROUGE);
        tuples3.add(JAUNE, JAUNE);
        tuples3.add(JAUNE, VERT);
        MultivaluedDecisionDiagram mdd3 = new MultivaluedDecisionDiagram(colonnes[2], tuples3);
        solver.post(IntConstraintFactory.mddc(colonnes[2], mdd3));
        
        // Ajout des couleurs par l'oppose du cube 4
        Tuples tuples4 = new Tuples();
        tuples4.add(JAUNE, ROUGE);
        tuples4.add(ROUGE, VERT);
        tuples4.add(BLEU, JAUNE);
        MultivaluedDecisionDiagram mdd4 = new MultivaluedDecisionDiagram(colonnes[3], tuples4);
        solver.post(IntConstraintFactory.mddc(colonnes[3], mdd4));
        
        // Vecteur contenant toutes les variables de la matrice dans un seul vecteur
        IntVar[] toutesLesVariables = new IntVar[INVERSE * n];
        for (int i = 0; i < INVERSE * n; i++) {
            toutesLesVariables[i] = lignesAvantArriere[i / n][i % n];
        }
        
        switch(heuristique) {
	        case HEURISTIQUE_DOMOVERWDEG:
	            solver.set(IntStrategyFactory.domOverWDeg(toutesLesVariables, 42));
	            break;
	        case HEURISTIQUE_IMPACT_BASED_SEARCH:
	            solver.set(IntStrategyFactory.impact(toutesLesVariables, 42));
	            break;
        }

        switch(restart) {
	        case RESTART_LUBY:
	            SearchMonitorFactory.luby(solver, 2, 2, new FailCounter(solver, 2), 25000);
	            break;
	        case RESTART_GEOMETRIQUE:
	            SearchMonitorFactory.geometrical(solver, 2, 2.1, new FailCounter(solver, 2), 25000);
	            break;
        }

        solver.findSolution();

        for (int i = 0; i < INVERSE; i++) {
            for (int j = 0; j < n; j++) {
                if (lignesAvantArriere[i][j].getValue() < 10)
                    System.out.print(" ");
                if (lignesAvantArriere[i][j].getValue() < 100)
                    System.out.print(" ");
                System.out.print(lignesAvantArriere[i][j].getValue());
                System.out.print("  ");
            }
            System.out.println("");
        }
        Chatterbox.printStatistics(solver);
    }
}
