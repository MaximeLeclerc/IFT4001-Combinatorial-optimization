import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import org.chocosolver.solver.trace.*;
import org.chocosolver.solver.search.limits.*;
import org.chocosolver.solver.search.loop.monitors.*;

public class CarreMagique {
    public static final int HEURISTIQUE_DEFAUT = 0;
    public static final int HEURISTIQUE_DOMOVERWDEG = 1;
    public static final int HEURISTIQUE_IMPACT_BASED_SEARCH = 2;

    public static final int RESTART_AUCUN = 0;
    public static final int RESTART_LUBY = 1;
    public static final int RESTART_GEOMETRIQUE = 2;
    
    public static final int COHERENCE_DE_BORNES = 0;
    public static final int COHERENCE_DE_DOMAINE = 1; 

    public static void main(String[] args) {
        final int n = 3;
        final int sommeMagique = n * (n * n + 1) / 2;

        final int coherence = COHERENCE_DE_BORNES;
        final int heuristique = HEURISTIQUE_DEFAUT;
        final int restart = RESTART_AUCUN;
        final boolean bris_symetries = false;

        // Creation du solveur
        Solver solver = new Solver();

        // Creation d'une matrice de dimensions n x n de variables dont les domaines sont les entiers de 1 a n^2.
        IntVar[][] lignes;
        if (coherence == COHERENCE_DE_BORNES)
            lignes = VariableFactory.boundedMatrix("x", n, n, 1, n * n, solver);
        else
            lignes = VariableFactory.enumeratedMatrix("x", n, n, 1, n * n, solver);

        // Vecteur contenant toutes les variables de la matrice dans un seul vecteur
        IntVar[] toutesLesVariables = new IntVar[n * n];
        for (int i = 0; i < n * n; i++) {
            toutesLesVariables[i] = lignes[i / n][i % n];
        }
        // Ajout d'une contrainte forcant toutes les variables a prendre des variables differentes
        if (coherence == COHERENCE_DE_BORNES)
            solver.post(IntConstraintFactory.alldifferent(toutesLesVariables, "BC"));
        else
            solver.post(IntConstraintFactory.alldifferent(toutesLesVariables, "AC"));

        // Creation de la tranpose de la matrice lignes.
        IntVar[][] colonnes = new IntVar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                colonnes[i][j] = lignes[j][i];
            }
        }

        // Creation d'une variable n'ayant qu'une seule valeur dans son domaine
        IntVar variableSommeMagique = VariableFactory.fixed(sommeMagique, solver);
        IntVar[] diagonale1 = new IntVar[n]; // Contient les variables sur la diagonale negative de la matrice
        IntVar[] diagonale2 = new IntVar[n]; // Contient les variables sur la diagonale positive de la matrice
        for (int i = 0; i < n; i++) {
            // Ajout de deux contraintes forcant les sommes des lignes et des colonnes a etre egales a la constante magique
            solver.post(IntConstraintFactory.sum(lignes[i], variableSommeMagique));
            solver.post(IntConstraintFactory.sum(colonnes[i], variableSommeMagique));
            diagonale1[i] = lignes[i][i];
            diagonale2[i] = lignes[n - i - 1][i];
        }
        // Force la somme des variables sur les deux diagonales a etre egale a la constante somme magique
        solver.post(IntConstraintFactory.sum(diagonale1, variableSommeMagique));
        solver.post(IntConstraintFactory.sum(diagonale2, variableSommeMagique));

        if (bris_symetries) {
            for (int i = 1; i < n / 2; i++)
                solver.post(IntConstraintFactory.arithm(lignes[i - 1][i - 1], "<", lignes[i][i]));

            solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[n - 1][0]));
            solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[0][n - 1]));
            solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[n - 1][n - 1]));
            // solver.post(IntConstraintFactory.arithm(lignes[n - 1][0], "<", lignes[0][n - 1]));
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

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (lignes[i][j].getValue() < 10)
                    System.out.print(" ");
                if (lignes[i][j].getValue() < 100)
                    System.out.print(" ");
                System.out.print(lignes[i][j].getValue());
                System.out.print("  ");
            }
            System.out.println("");
        }
        Chatterbox.printStatistics(solver);
    }
}
