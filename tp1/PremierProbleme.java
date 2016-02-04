import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class PremierProbleme {

	public static final int NOMBRE_DE_COTES = 4;

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

		final int coherence = COHERENCE_DE_DOMAINE;
		final int heuristique = HEURISTIQUE_DOMOVERWDEG;
		final int restart = RESTART_AUCUN;
		final boolean bris_symetries = false;

		// Creation du solveur
		Solver solver = new Solver();

		// Creation d'une matrice de dimensions NOMBRE_DE_COTES x n de variables
		// dont les domaines sont les entiers de 1 a n.
		IntVar[][] lignes;
		if (coherence == COHERENCE_DE_BORNES)
			lignes = VariableFactory.boundedMatrix("x", NOMBRE_DE_COTES, n, 1, n, solver);
		else
			lignes = VariableFactory.enumeratedMatrix("x", NOMBRE_DE_COTES, n, 1, n, solver);

		// Ajout des contraintes forcant chacun des elements d'une ligne a avoir
		// une couleur differente
		for (int i = 0; i < NOMBRE_DE_COTES; i++) {
			if (coherence == COHERENCE_DE_BORNES)
				solver.post(IntConstraintFactory.alldifferent(lignes[i], "BC"));
			else
				solver.post(IntConstraintFactory.alldifferent(lignes[i], "AC"));
		}

		// Creation de la matrice des opposes NOMBRE_DE_COTES / 2 x 2 * n
		IntVar[][] opposes = new IntVar[2 * n][NOMBRE_DE_COTES / 2];
		for (int i = 0; i < NOMBRE_DE_COTES; i++) {
			for (int j = 0; j < n; j++) {
				opposes[i % 2 + j * 2][i / 2] = lignes[i][j];
			}
		}

		// Ajout des couleurs par l'oppose des cubes
		for (int i = 0; i < 2 * n; i++) {
			Tuples tuples = creerTuples(i / 2);
			solver.post(IntConstraintFactory.table(opposes[i][0], opposes[i][1], tuples, "AC2001"));
		}

		// Assure que les deux opposes sont differents
		for (int i = 0; i < 2 * n; i += 2) {
			LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(opposes[i][0], "=", opposes[i + 1][0]),
					IntConstraintFactory.arithm(opposes[i][1], "!=", opposes[i + 1][1]));
			LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(opposes[i][0], "=", opposes[i + 1][1]),
					IntConstraintFactory.arithm(opposes[i][1], "!=", opposes[i + 1][0]));
		}

		// Vecteur contenant toutes les variables de la matrice dans un seul
		// vecteur
		IntVar[] toutesLesVariables = new IntVar[NOMBRE_DE_COTES * n];
		for (int i = 0; i < NOMBRE_DE_COTES * n; i++) {
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
			for (int j = 0; j < n; j++) {
				System.out.print(printCouleur(lignes[i][j].getValue()));
				System.out.print("  ");
			}
			System.out.println("");
		}
		Chatterbox.printStatistics(solver);
	}

	private static Tuples creerTuples(int cote) {
		Tuples tuples = new Tuples();

		switch (cote) {
		case 0:
			tuples.add(JAUNE, VERT);
			tuples.add(VERT, JAUNE);
			tuples.add(ROUGE, BLEU);
			tuples.add(BLEU, ROUGE);
			tuples.add(VERT, ROUGE);
			tuples.add(ROUGE, VERT);
			break;
		case 1:
			tuples.add(VERT, VERT);
			tuples.add(ROUGE, BLEU);
			tuples.add(BLEU, ROUGE);
			tuples.add(JAUNE, BLEU);
			tuples.add(BLEU, JAUNE);
			break;
		case 2:
			tuples.add(BLEU, ROUGE);
			tuples.add(ROUGE, BLEU);
			tuples.add(JAUNE, JAUNE);
			tuples.add(JAUNE, VERT);
			tuples.add(VERT, JAUNE);
			break;
		case 3:
			tuples.add(JAUNE, ROUGE);
			tuples.add(ROUGE, JAUNE);
			tuples.add(ROUGE, VERT);
			tuples.add(VERT, ROUGE);
			tuples.add(BLEU, JAUNE);
			tuples.add(JAUNE, BLEU);
			break;
		}

		return tuples;
	}

	private static String printCouleur(int indice) {
		String couleur = "";

		switch (indice) {
		case JAUNE:
			couleur = "JAUNE";
			break;
		case ROUGE:
			couleur = "ROUGE";
			break;
		case BLEU:
			couleur = "BLEU";
			break;
		case VERT:
			couleur = "VERT";
			break;
		}

		return couleur;
	}
}
