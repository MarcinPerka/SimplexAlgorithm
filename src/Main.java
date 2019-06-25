public class Main {
    /**
     * UWAGA !!!! tablica operatorSlack przyjmuje liczby 0, -1 oraz 1. Odpowiadają one kolejno: 0 : =,  1 : <=, -1 : >=
     */
    public static void main(String[] args) {
        /**
         * Fn. celu true - maksymalizacja, false - minimalizacja
         * Fn. celu 20000x0 + 25000x1 -> min
         * 400x0 + 300x1 >= 25000
         * 300x0 + 400x1 >= 27000
         * 200x0 + 500x1 >= 30000
         * UWAGA !!!! tablica operatorSlack przyjmuje liczby 0, -1 oraz 1. Odpowiadają one kolejno: 0 : =,  1 : <=, -1 : >=
         */

        double[][] leftHandSide = {{400, 300}, {300, 400}, {200, 500}};
        double[] rightHandSide = {25000, 27000, 30000};
        double[] objectiveFunction = {20000, 25000};
        double[] operatorSlack = {-1, -1, -1};
        boolean maximizeObjectiveFunction = false;

        SimplexTableau simplexTableau = new SimplexTableau(leftHandSide, rightHandSide, objectiveFunction, operatorSlack, maximizeObjectiveFunction);

        if (maximizeObjectiveFunction) {
            SimplexAlg simplexAlg = new SimplexAlg(simplexTableau);
        } else {
            SimplexAlgMin simplexAlgMin = new SimplexAlgMin(simplexTableau);
        }
    }

}

