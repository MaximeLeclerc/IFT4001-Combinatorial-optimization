import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.*;
import org.chocosolver.solver.trace.*;

public class Cubes {

    public static final int HEURISTIQUE_DEFAUT = 0;

    public static final int RESTART_AUCUN = 0;

    public static void main(String[] args) {

        final int heuristique = HEURISTIQUE_DEFAUT;
        final int restart = RESTART_AUCUN;

        // Creation du solveur
        Solver solver = new Solver();

        // Cr�ation de quatre vecteurs qui contiennent les couleurs dans le m�me ordre d'apparition que sur les cubes (incluant les rotations possibles)
        // Bleu : 1
        // Rouge : 2
        // Jaune : 3
        // Vert : 4
        int[] cube1 = {1,3,2,4,1,3,2,1,4,2,3,1,4,2,1,4,2,2,1,4,2,1,2,2,4,1,2,2,2,3,4,4,2,3,4,2,4,4,3,2,4,4};
        int[] cube2 = {1,4,2,4,1,4,2,1,4,2,4,1,4,2,1,1,2,3,1,1,2,1,3,2,1,1,3,2,1,4,3,4,1,4,3,1,4,3,4,1,4,3};
        int[] cube3 = {3,1,3,2,3,1,3,3,2,3,1,3,2,3,3,3,3,4,3,3,3,3,4,3,3,3,4,3,4,1,3,2,4,1,3,4,2,3,1,4,2,3};
        int[] cube4 = {4,3,2,2,4,3,2,4,2,2,3,4,2,2,4,1,2,3,4,1,2,4,3,2,1,4,3,2,3,3,1,2,3,3,1,3,2,1,2,3,2,1};

        // Positions de d�but valides (pour garder l'ordre de chaque face vers le haut, le bas, la gauche, la droite)
        IntVar i = VariableFactory.enumerated("i", new int[]{0,1,2,3,7,8,9,10,14,15,16,17,21,22,23,24,28,29,30,31,35,36,37,38}, solver);
        IntVar j = VariableFactory.enumerated("j", new int[]{0,1,2,3,7,8,9,10,14,15,16,17,21,22,23,24,28,29,30,31,35,36,37,38}, solver);
        IntVar k = VariableFactory.enumerated("k", new int[]{0,1,2,3,7,8,9,10,14,15,16,17,21,22,23,24,28,29,30,31,35,36,37,38}, solver);
        IntVar l = VariableFactory.enumerated("l", new int[]{0,1,2,3,7,8,9,10,14,15,16,17,21,22,23,24,28,29,30,31,35,36,37,38}, solver);

        IntVar x1 = VariableFactory.enumerated("x1", new int[]{1,2,3,4}, solver);
        IntVar x2 = VariableFactory.enumerated("x2", new int[]{1,2,3,4}, solver);
        IntVar x3 = VariableFactory.enumerated("x3", new int[]{1,2,3,4}, solver);
        IntVar x4 = VariableFactory.enumerated("x4", new int[]{1,2,3,4}, solver);

        IntVar x5 = VariableFactory.enumerated("x5", new int[]{1,2,3,4}, solver);
        IntVar x6 = VariableFactory.enumerated("x6", new int[]{1,2,3,4}, solver);
        IntVar x7 = VariableFactory.enumerated("x7", new int[]{1,2,3,4}, solver);
        IntVar x8 = VariableFactory.enumerated("x8", new int[]{1,2,3,4}, solver);

        IntVar x9 = VariableFactory.enumerated("x9", new int[]{1,2,3,4}, solver);
        IntVar x10 = VariableFactory.enumerated("x10", new int[]{1,2,3,4}, solver);
        IntVar x11 = VariableFactory.enumerated("x11", new int[]{1,2,3,4}, solver);
        IntVar x12 = VariableFactory.enumerated("x12", new int[]{1,2,3,4}, solver);

        IntVar x13 = VariableFactory.enumerated("x13", new int[]{1,2,3,4}, solver);
        IntVar x14 = VariableFactory.enumerated("x14", new int[]{1,2,3,4}, solver);
        IntVar x15 = VariableFactory.enumerated("x15", new int[]{1,2,3,4}, solver);
        IntVar x16 = VariableFactory.enumerated("x16", new int[]{1,2,3,4}, solver);

        solver.post(IntConstraintFactory.element(x1, cube1, i, 0, "none"));
        solver.post(IntConstraintFactory.element(x2, cube2, j, 0, "none"));
        solver.post(IntConstraintFactory.element(x3, cube3, k, 0, "none"));
        solver.post(IntConstraintFactory.element(x4, cube4, l, 0, "none"));

        solver.post(IntConstraintFactory.element(x5, cube1, i, -1, "none"));
        solver.post(IntConstraintFactory.element(x6, cube2, j, -1, "none"));
        solver.post(IntConstraintFactory.element(x7, cube3, k, -1, "none"));
        solver.post(IntConstraintFactory.element(x8, cube4, l, -1, "none"));

        solver.post(IntConstraintFactory.element(x9, cube1, i, -2, "none"));
        solver.post(IntConstraintFactory.element(x10, cube2, j, -2, "none"));
        solver.post(IntConstraintFactory.element(x11, cube3, k, -2, "none"));
        solver.post(IntConstraintFactory.element(x12, cube4, l, -2, "none"));

        solver.post(IntConstraintFactory.element(x13, cube1, i, -3, "none"));
        solver.post(IntConstraintFactory.element(x14, cube2, j, -3, "none"));
        solver.post(IntConstraintFactory.element(x15, cube3, k, -3, "none"));
        solver.post(IntConstraintFactory.element(x16, cube4, l, -3, "none"));

        // Ajout d'une contrainte forcant toutes les variables a prendre des variables differentes
        solver.post(IntConstraintFactory.alldifferent(new IntVar[]{x1, x2, x3, x4}, "BC"));
        solver.post(IntConstraintFactory.alldifferent(new IntVar[]{x5, x6, x7, x8}, "BC"));
        solver.post(IntConstraintFactory.alldifferent(new IntVar[]{x9, x10, x11, x12}, "BC"));
        solver.post(IntConstraintFactory.alldifferent(new IntVar[]{x13, x14, x15, x16}, "BC"));

        IntVar[] toutesLesVariables = new IntVar[20];

        toutesLesVariables[0] = i;
        toutesLesVariables[1] = j;
        toutesLesVariables[2] = k;
        toutesLesVariables[3] = l;

        toutesLesVariables[4] = x1;
        toutesLesVariables[5] = x2;
        toutesLesVariables[6] = x3;
        toutesLesVariables[7] = x4;
        toutesLesVariables[8] = x5;
        toutesLesVariables[9] = x6;
        toutesLesVariables[10] = x7;
        toutesLesVariables[11] = x8;
        toutesLesVariables[12] = x9;
        toutesLesVariables[13] = x10;
        toutesLesVariables[14] = x11;
        toutesLesVariables[15] = x12;
        toutesLesVariables[16] = x13;
        toutesLesVariables[17] = x14;
        toutesLesVariables[18] = x15;
        toutesLesVariables[19] = x16;


        solver.findSolution();

        System.out.print("\nIndices de d�but de l'array\n");

        System.out.print(i.getValue());
        System.out.print("  ");
        System.out.print(j.getValue());
        System.out.print("  ");
        System.out.print(k.getValue());
        System.out.print("  ");
        System.out.print(l.getValue());

        System.out.print("\n\nBleu = 1 \n");
        System.out.print("Rouge = 2 \n");
        System.out.print("Jaune = 3 \n");
        System.out.print("Vert = 4 \n");

        System.out.print("\nCube 1 :\n");
        System.out.print(x1.getValue());
        System.out.print("  ");
        System.out.print(x5.getValue());
        System.out.print("  ");
        System.out.print(x9.getValue());
        System.out.print("  ");
        System.out.print(x13.getValue());
        System.out.print("  \n");

        System.out.print("Cube 2 :\n");
        System.out.print(x2.getValue());
        System.out.print("  ");
        System.out.print(x6.getValue());
        System.out.print("  ");
        System.out.print(x10.getValue());
        System.out.print("  ");
        System.out.print(x14.getValue());
        System.out.print("  \n");

        System.out.print("Cube 3 :\n");
        System.out.print(x3.getValue());
        System.out.print("  ");
        System.out.print(x7.getValue());
        System.out.print("  ");
        System.out.print(x11.getValue());
        System.out.print("  ");
        System.out.print(x15.getValue());
        System.out.print("  \n");

        System.out.print("Cube 4 :\n");
        System.out.print(x4.getValue());
        System.out.print("  ");
        System.out.print(x8.getValue());
        System.out.print("  ");
        System.out.print(x12.getValue());
        System.out.print("  ");
        System.out.print(x16.getValue());
        System.out.print("  \n");

        Chatterbox.printStatistics(solver);

    }
}