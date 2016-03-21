import java.util.Dictionary;
import java.util.Hashtable;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class Cryptography {
	public static final int NUMBER_OF_LETTERS_IN_LANGUAGE = 26; // English

	public static final int HEURISTIQUE_DEFAUT = 0;
	public static final int HEURISTIQUE_DOMOVERWDEG = 1;
	public static final int HEURISTIQUE_IMPACT_BASED_SEARCH = 2;

	public static final int RESTART_AUCUN = 0;
	public static final int RESTART_LUBY = 1;
	public static final int RESTART_GEOMETRIQUE = 2;

	public static final int COHERENCE_DE_BORNES = 0;
	public static final int COHERENCE_DE_DOMAINE = 1;

	public static void main(String[] args) {
		// Caesar cipher using a left rotation of three places, equivalent to a
		// right shift of 23
		// plainText = THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
		final String cypherText = "QEB NRFZH YOLTK CLU GRJMP LSBO QEB IXWV ALD";
		final int n = cypherText.length();

		final int coherence = COHERENCE_DE_BORNES;
		final int heuristique = HEURISTIQUE_DEFAUT;
		final int restart = RESTART_AUCUN;
		final boolean bris_symetries = false;

		// Creation of the solver
		Solver solver = new Solver();

		// Creation of a matrix of dimensions NUMBER_OF_LETTERS_IN_LANGUAGE x n
		// of variables with domains of integer 0 to
		// NUMBER_OF_LETTERS_IN_LANGUAGE.
		IntVar[][] rows;
		if (coherence == COHERENCE_DE_BORNES)
			rows = VariableFactory.boundedMatrix("x", NUMBER_OF_LETTERS_IN_LANGUAGE, n, 0,
					NUMBER_OF_LETTERS_IN_LANGUAGE, solver);
		else
			rows = VariableFactory.enumeratedMatrix("x", NUMBER_OF_LETTERS_IN_LANGUAGE, n, 0,
					NUMBER_OF_LETTERS_IN_LANGUAGE, solver);

		// Ajout d'une contrainte forcant toutes les variables a prendre des
		// variables differentes
		if (coherence == COHERENCE_DE_BORNES)
			solver.post(IntConstraintFactory.alldifferent(toutesLesVariables, "BC"));
		else
			solver.post(IntConstraintFactory.alldifferent(toutesLesVariables, "AC"));

		// Creation de la tranpose de la matrice rows.
		IntVar[][] colonnes = new IntVar[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				colonnes[i][j] = rows[j][i];
			}
		}

		// Creation d'une variable n'ayant qu'une seule valeur dans son domaine
		IntVar variableSommeMagique = VariableFactory.fixed(sommeMagique, solver);
		IntVar[] diagonale1 = new IntVar[n]; // Contient les variables sur la
												// diagonale negative de la
												// matrice
		IntVar[] diagonale2 = new IntVar[n]; // Contient les variables sur la
												// diagonale positive de la
												// matrice
		for (int i = 0; i < n; i++) {
			// Ajout de deux contraintes forcant les sommes des rows et des
			// colonnes a etre egales a la constante magique
			solver.post(IntConstraintFactory.sum(rows[i], variableSommeMagique));
			solver.post(IntConstraintFactory.sum(colonnes[i], variableSommeMagique));
			diagonale1[i] = rows[i][i];
			diagonale2[i] = rows[n - i - 1][i];
		}
		// Force la somme des variables sur les deux diagonales a etre egale a
		// la constante somme magique
		solver.post(IntConstraintFactory.sum(diagonale1, variableSommeMagique));
		solver.post(IntConstraintFactory.sum(diagonale2, variableSommeMagique));

		if (bris_symetries) {
			for (int i = 1; i < n / 2; i++)
				solver.post(IntConstraintFactory.arithm(rows[i - 1][i - 1], "<", rows[i][i]));

			solver.post(IntConstraintFactory.arithm(rows[0][0], "<", rows[n - 1][0]));
			solver.post(IntConstraintFactory.arithm(rows[0][0], "<", rows[0][n - 1]));
			solver.post(IntConstraintFactory.arithm(rows[0][0], "<", rows[n - 1][n - 1]));
			// solver.post(IntConstraintFactory.arithm(rows[n - 1][0], "<",
			// rows[0][n - 1]));
		}

		// Vecteur contenant toutes les variables de la matrice dans un seul
		// vecteur
		IntVar[] toutesLesVariables = new IntVar[n * n];
		for (int i = 0; i < n * n; i++) {
			toutesLesVariables[i] = rows[i / n][i % n];
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
				if (rows[i][j].getValue() < 10)
					System.out.print(" ");
				if (rows[i][j].getValue() < 100)
					System.out.print(" ");
				System.out.print(rows[i][j].getValue());
				System.out.print("  ");
			}
			System.out.println("");
		}
		Chatterbox.printStatistics(solver);
	}

	private Dictionary<Character, Double> getEnglishFrequencies() {
		// create a new hashtable
		   Dictionary<Character, Double> dict = new Hashtable<>();
			  
		   // add elements in the hashtable
		   dict.put('A', 8.167);
		   dict.put('B', 1.492);
		   dict.put('C', 2.782);
		   dict.put('D', 4.253);
		   dict.put('E', 12.702);
		   dict.put('F', 2.228);
		   dict.put('G', 2.015);
		   dict.put('H', 6.094);
		   dict.put('I', 6.966);
		   dict.put('J', 0.153);
		   dict.put('K', 0.772);
		   dict.put('L', 4.025);
		   dict.put('M', 2.406);
		   dict.put('N', 6.749);
		   dict.put('O', 7.507);
		   dict.put('P', 1.929);
		   dict.put('Q', 0.095);
		   dict.put('R', 5.987);
		   dict.put('S', 6.327);
		   dict.put('T', 9.056);
		   dict.put('U', 2.758);
		   dict.put('V', 0.978);
		   dict.put('W', 2.361);
		   dict.put('X', 0.150);
		   dict.put('Y', 1.974);
		   dict.put('Z', 0.074);
			  
		   return dict;
	}
}
