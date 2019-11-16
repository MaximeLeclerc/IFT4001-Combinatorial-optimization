//  http://prettyprinter.de/

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton;
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class Horaire
{
    public static final int HEURISTIQUE_DEFAUT              = 0;
    public static final int HEURISTIQUE_DOMOVERWDEG         = 1;
    public static final int HEURISTIQUE_IMPACT_BASED_SEARCH = 2;
    public static final int HEURISTIQUE_ACTIVITY            = 3;

    public static final int RESTART_AUCUN       = 0;
    public static final int RESTART_LUBY        = 1;
    public static final int RESTART_GEOMETRIQUE = 2;

    public static void main ( String[] args )
    {
        final int heuristique = HEURISTIQUE_DOMOVERWDEG;
        final int restart = RESTART_AUCUN;

        // un employé doit travailler entre 5 et 7 heures par jour incluant
        // une pause obligatoire de 30 minutes.
        final int travailMin = 5 * 2 - 1;
        final int travailMax = 7 * 2 - 1;

        // Nombre d’employés requis
        final int employesRequis = 1;

        // Nombre d’employés souhaité
        final int[] employesSouhaites = { 1, 2, 3, 4, 5, 4, 2, 3, 4, 3, 5, 5,
                4, 3, 3, 3 };

        // On estime à x * 20$ par demi-heure la perte de profit lorsqu’il y
        // a x employés de moins ou x employés de plus que le nombre souhaité.
        final int perteParDemiHeure = 20;

        // Il y a p périodes dans une journée et n employés. Dans l'instance
        // que vous devez résoudre, nous avons p = 16 et n = 5.
        final int employes = 5;     // lignes
        final int periodes = 16;    // colonnes

        // Creation du solveur
        Solver solver = new Solver( );

        // Creation d'une matrice de dimensions p x n de variables dont les
        // domaines sont les entiers de 0 a 1.
        IntVar[][] lignes =
                VariableFactory.boundedMatrix( "horaire", employes, periodes,
                        0, 1, solver );

        IntVar perte = VariableFactory
                .bounded( "perte", 0, employes * periodes * perteParDemiHeure,
                        solver );

        IntVar[] toutesLesVariables =
                creeVariablesEtContraintes( employes, periodes,
                        employesRequis, travailMin, travailMax,
                        employesSouhaites, perte, solver, lignes );

        configureStrategieEtRecherche( heuristique, restart, solver,
                toutesLesVariables );

        solver.findOptimalSolution( ResolutionPolicy.MINIMIZE, perte );

        imprimeResultats( employes, periodes, solver, lignes );

        Chatterbox.printStatistics( solver );
    }

    private static IntVar[] creeVariablesEtContraintes ( int employes,
            int periodes, int employesRequis, int travailMin, int travailMax,
            int[] employesSouhaites, IntVar perte, Solver solver, IntVar[][]
            lignes )
    {
        // Vecteur contenant toutes les variables de la matrice dans un seul
        // vecteur
        IntVar[] toutesLesVariables = new IntVar[ employes * periodes ];

        for ( int i = 0; i < employes; i++ )
        {
            for ( int j = 0; j < periodes; j++ )
            {
                toutesLesVariables[ ( i * periodes ) + j ] = lignes[ i ][ j ];
            }
        }

        // Creation de la tranpose de la matrice lignes.
        IntVar[][] colonnes = new IntVar[ periodes ][ employes ];
        for ( int i = 0; i < periodes; i++ )
        {
            for ( int j = 0; j < employes; j++ )
            {
                colonnes[ i ][ j ] = lignes[ j ][ i ];
            }
        }

        // Creation de variables n'ayant qu'une seule valeur dans leur domaine
        IntVar varEmployesRequis = VariableFactory.fixed( employesRequis,
                solver );
        IntVar varTravailMin = VariableFactory.fixed( travailMin, solver );
        IntVar varTravailMax = VariableFactory.fixed( travailMax, solver );

        for ( int i = 0; i < periodes; i++ )
        {
            // Ajout de contraintes forcant les sommes des employes a etre
            // superieure ou egale au minimum
            solver.post( IntConstraintFactory
                    .sum( colonnes[ i ], ">=", varEmployesRequis ) );
        }

        for ( int i = 0; i < employes; i++ )
        {
            // Ajout de contraintes forcant les sommes des heures par
            // employes a etre superieure ou egale au minimum
            solver.post( IntConstraintFactory.sum( lignes[ i ], ">=",
                    varTravailMin ) );

        }

        for ( int i = 0; i < employes; i++ )
        {
            // Ajout de contraintes forcant les sommes des heures par
            // employes a etre inferieure ou egale au maximum
            solver.post( IntConstraintFactory.sum( lignes[ i ], "<=",
                    varTravailMax ) );
        }

        // Ajout de contraintes forcant sur les pauses. La pause doit être
        // précédée et suivie d'une période de travail d'au moins deux heures.
        StringBuilder regexp = new StringBuilder("0*1*1111011111*0*");
        IAutomaton auto = new FiniteAutomaton(regexp.toString());

        for ( int i = 0; i < employes; i++ )
        {
            solver.post( IntConstraintFactory.regular( lignes[ i ], auto ));
        }

        IntVar[] employesParPeriode = VariableFactory.boundedArray(
                "employesParPeriode", periodes, 0, employes, solver );

        for ( int i = 0; i < periodes; i++ )
        {
            // Ajout de contraintes calulant le nombre d'employes pour chaque
            // periode
            solver.post( IntConstraintFactory.sum( colonnes[ i ],
                    employesParPeriode[ i ] ) );
        }

        IntVar[] varEmployesSouhaites = VariableFactory.boundedArray(
                "employesSouhaites", periodes, employesRequis, employes,
                solver );

        for ( int i = 0; i < periodes; i++ )
        {
            // Ajout de contraintes sur le nombre d'employes souhaites
            solver.post( IntConstraintFactory.arithm(
                    varEmployesSouhaites[ i ], "=", employesSouhaites[ i ] ) );
        }

        IntVar[] perteParPeriode = VariableFactory.boundedArray(
                "perteParPeriode", periodes, 0, employes, solver );

        for ( int i = 0; i < periodes; i++ )
        {
            // Ajout de contraintes calulant la perte pour chaque periode
            solver.post( IntConstraintFactory.distance( employesParPeriode[ i ],
                    varEmployesSouhaites[ i ], "=", perteParPeriode[ i ] ) );

        }

        final int[] coeffsPerteLignes = { 20, 20, 20, 20, 20, 20, 20, 20, 20,
                20, 20, 20, 20, 20, 20, 20 };

        solver.post( IntConstraintFactory.scalar( perteParPeriode,
                coeffsPerteLignes, perte ) );

        return toutesLesVariables;
    }

    private static void configureStrategieEtRecherche ( int heuristique,
            int restart, Solver solver, IntVar[] toutesLesVariables )
    {
        switch ( heuristique )
        {
            case HEURISTIQUE_DOMOVERWDEG:
                solver.set( IntStrategyFactory
                        .domOverWDeg( toutesLesVariables, 42 ) );
                break;
            case HEURISTIQUE_IMPACT_BASED_SEARCH:
                solver.set(
                        IntStrategyFactory.impact( toutesLesVariables, 42 ) );
                break;
            case HEURISTIQUE_ACTIVITY:
                solver.set(
                        IntStrategyFactory.activity( toutesLesVariables, 42 ) );
                break;
        }


        switch ( restart )
        {
            case RESTART_LUBY:
                SearchMonitorFactory
                        .luby( solver, 2, 2, new FailCounter( 2 ), 25000 );
                break;
            case RESTART_GEOMETRIQUE:
                SearchMonitorFactory
                        .geometrical( solver, 2, 2.1, new FailCounter( 2 ),
                                25000 );
                break;
        }

    }

    private static void imprimeResultats ( int employes, int periodes, Solver
            solver, IntVar[][] lignes )
    {
        for ( int i = 0; i < employes; i++ )
        {
            for ( int j = 0; j < periodes; j++ )
            {
                if ( lignes[ i ][ j ].getValue( ) < 10 )
                {
                    System.out.print( " " );
                }

                if ( lignes[ i ][ j ].getValue( ) < 100 )
                {
                    System.out.print( " " );
                }

                System.out.print( lignes[ i ][ j ].getValue( ) );
                System.out.print( "  " );
            }

            System.out.println( "" );
        }
        System.out.println( "" );

    }
}