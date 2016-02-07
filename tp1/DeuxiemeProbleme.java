import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import org.chocosolver.solver.trace.*;
import org.chocosolver.solver.search.limits.*;
import org.chocosolver.solver.search.loop.monitors.*;

public class DeuxiemeProbleme {
	public static final int NOMBRE_HEURES_MINIMUM = 5;
	public static final int NOMBRE_HEURES_MAXIMUM = 7;

	public static final int HEURISTIQUE_DEFAUT = 0;
	public static final int HEURISTIQUE_DOMOVERWDEG = 1;
	public static final int HEURISTIQUE_IMPACT_BASED_SEARCH = 2;

	public static final int RESTART_AUCUN = 0;
	public static final int RESTART_LUBY = 1;
	public static final int RESTART_GEOMETRIQUE = 2;

	public static final int COHERENCE_DE_BORNES = 0;
	public static final int COHERENCE_DE_DOMAINE = 1;

	public static void main(String[] args) {
		final int p = 16;
		final int n = 5;

		final int coherence = COHERENCE_DE_BORNES;
		final int heuristique = HEURISTIQUE_DEFAUT;
		final int restart = RESTART_AUCUN;
		final boolean bris_symetries = false;

		// Creation du solveur
		Solver solver = new Solver();

		// Creation d'une matrice de dimensions n x p de variables dont les
		// domaines sont les entiers de 0 ou 1.
		IntVar[][] lignes;
		if (coherence == COHERENCE_DE_BORNES)
			lignes = VariableFactory.boundedMatrix("x", n, p, 0, 1, solver);
		else
			lignes = VariableFactory.enumeratedMatrix("x", n, p, 0, 1, solver);

		// Un employe doit travailler entre 5 et 7 heures.
		IntVar variableNombreHeuresMinimum = VariableFactory.fixed(NOMBRE_HEURES_MINIMUM, solver);
		IntVar variableNombreHeuresMaximum = VariableFactory.fixed(NOMBRE_HEURES_MAXIMUM, solver);
		for (int i = 0; i < n; i++) {
			solver.post(IntConstraintFactory.sum(lignes[i], ">=", variableNombreHeuresMinimum));
			solver.post(IntConstraintFactory.sum(lignes[i], "<=", variableNombreHeuresMaximum));
		}

		// Vecteur contenant toutes les variables de la matrice dans un seul
		// vecteur
		IntVar[] toutesLesVariables = new IntVar[n * n];
		for (int i = 0; i < n * n; i++) {
			toutesLesVariables[i] = lignes[i / n][i % n];
		}

		if (bris_symetries) {
			for (int i = 1; i < n / 2; i++)
				solver.post(IntConstraintFactory.arithm(lignes[i - 1][i - 1], "<", lignes[i][i]));

			solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[n - 1][0]));
			solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[0][n - 1]));
			solver.post(IntConstraintFactory.arithm(lignes[0][0], "<", lignes[n - 1][n - 1]));
			// solver.post(IntConstraintFactory.arithm(lignes[n - 1][0], "<",
			// lignes[0][n - 1]));
		}

		switch (heuristique) {
		case HEURISTIQUE_DOMOVERWDEG:
			solver.set(IntStrategyFactory.domOverWDeg(toutesLesVariables, 42));
			break;
		case HEURISTIQUE_IMPACT_BASED_SEARCH:
			solver.set(IntStrategyFactory.impact(toutesLesVariables, 42));
			break;
		}

		switch (restart) {
		case RESTART_LUBY:
			SearchMonitorFactory.luby(solver, 2, 2, new FailCounter(solver, 2), 25000);
			break;
		case RESTART_GEOMETRIQUE:
			SearchMonitorFactory.geometrical(solver, 2, 2.1, new FailCounter(solver, 2), 25000);
			break;
		}

		solver.findSolution();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
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
