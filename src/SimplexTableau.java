/**
 * Klasa wspomaga utworzenie tzw. tablicy Simpleksowej. Tablica ta składa się z macierzy bazowego układu równań oraz dodatkowo jest wzbogacona o wiersz nazwany wierszem funkcji celu (ostatni wiersz tablicy).
 */
public class SimplexTableau {
    private double[][] leftHandSide;
    private double[] rightHandSide, objectiveFunction, operator;
    private double[][] simplexBoard;
    private int numberOfConstraints, numberOfVariables;
    private boolean maximizeObjectiveFunction;

    /**
     * @param leftHandSide      - Macierz zawierajaca lewa strone nierownosci.
     * @param rightHandSide     - Wektor zawierajacy prawa strone nierownosci.
     * @param objectiveFunction - Postac funkcji celu.
     * @param operator          - Wektor okreslajacy znak nierownosci; 1 mniejsze lub rowne, 0 rowne, -1 wieksze lub rowne. Wartości te mają odpowiednią postać w celu dodania zmiennych sztucznych/
     */
    public SimplexTableau(double[][] leftHandSide, double[] rightHandSide, double[] objectiveFunction, double[] operator, boolean maximizeObjectiveFunction) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.objectiveFunction = objectiveFunction;
        this.operator = operator;
        this.maximizeObjectiveFunction = maximizeObjectiveFunction;
        numberOfConstraints = rightHandSide.length;
        numberOfVariables = objectiveFunction.length;
        if (maximizeObjectiveFunction)
            initSimplexBoard();
        else
            initPivotSimplexBoard();
    }

    public double[][] getSimplexBoard() {
        return simplexBoard;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    /**
     * Tworzenie tablicy simpleksowej. Przypisanie LHS do odpowiednich miejsc za pomocą System.arraycopy (Ich położenie/indeksy nie zmieniają się).
     * Przypisanie RHS do ostatniej kolumny tablicy simpleksowej.
     * Przypisanie zmiennych sztucznych do kolumn wystepujacych za LHS.
     * Przypisanie wiersza wartości funkcji celu. Przypisywanie odbywa się w osobnej pętli ze względu, że przypisywane są tylko wartości z funkcji celu, a reszta zostaje wypełniana domyślnie - zerami.
     */
    public void initSimplexBoard() {
        simplexBoard = new double[numberOfConstraints + 1][numberOfConstraints + numberOfVariables + 1];
        for (int i = 0; i < numberOfConstraints; i++) {
            System.arraycopy(leftHandSide[i], 0, simplexBoard[i], 0, leftHandSide[i].length); //addLeftHandSideToSimplexBoard()
            simplexBoard[i][numberOfConstraints + numberOfVariables] = rightHandSide[i]; //addRightHandSideToSimplexBoard()
            simplexBoard[i][numberOfVariables + i] = operator[i]; //addSlackVariablesToSimplexBoard()
        }

        for (int i = 0; i < numberOfVariables; i++) {
            simplexBoard[numberOfConstraints][i] = objectiveFunction[i]; //addObjectiveFunctionToSimplexBoard()
        }
    }

    /**
     * Tworzenie tablicy simpleksowej dla minimalizacji funckji celu. Czyli:
     * Wiersze są kolumnami
     * Kolumny są wierszami
     * RHS są w wierszu funkcji celu z przeciwnym znakiem
     * Wartości funkcji celu są w kolumnie wyrazów wolnych
     * Zmiana znaku operatora
     */
    public void initPivotSimplexBoard() {
        simplexBoard = new double[numberOfVariables + 1][numberOfConstraints + numberOfVariables + 1];
        for (int i = 0; i < numberOfConstraints; i++) {
            simplexBoard[numberOfVariables][i] = -rightHandSide[i];
            for (int j = 0; j < numberOfVariables; j++) {
                simplexBoard[j][i] = leftHandSide[i][j];
            }
        }
        for (int i = 0; i < numberOfVariables; i++) {
            simplexBoard[i][numberOfConstraints + numberOfVariables] = objectiveFunction[i];
            if (operator[i] != 0)
                simplexBoard[i][numberOfConstraints + i] = -operator[i];
            else
                simplexBoard[i][numberOfConstraints + i] = operator[i];
        }
    }
}
