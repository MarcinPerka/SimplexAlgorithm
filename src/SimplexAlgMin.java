import java.text.DecimalFormat;

public class SimplexAlgMin {
    private SimplexTableau simplexTableau;

    private double[][] tableaux;
    private int numberOfConstraints, numberOfVariables;
    private int[] basisVariables;

    public SimplexAlgMin(SimplexTableau simplexTableau) {
        this.simplexTableau = simplexTableau;

        tableaux = simplexTableau.getSimplexBoard();
        numberOfConstraints = simplexTableau.getNumberOfConstraints();
        numberOfVariables = simplexTableau.getNumberOfVariables();

        initBasisVariables();
        simplex();
    }

    /**
     * Inicjalizacja wektora z rozwiązaniem bazowym - na początku są to zawsze zmienne sztuczne
     */
    private void initBasisVariables() {
        basisVariables = new int[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++)
            basisVariables[i] = numberOfConstraints + i;
    }

    /**
     * Metoda wypisuje w terminalu kolejne tablice simpleksowe
     */
    public void showNewTableaux() {
        System.out.println("Nowa tablica simpleksowa: ");
        DecimalFormat df = new DecimalFormat("0.00");
        for (double[] row : tableaux) {
            for (double element : row) {
                System.out.print(df.format(element) + "\t");
            }
            System.out.println();
        }
        double valueObjectiveFunction = tableaux[numberOfVariables][numberOfConstraints + numberOfVariables];
        System.out.println("Wartość funkcji celu = " + df.format(valueObjectiveFunction));
        for (int i = 0; i < numberOfVariables; i++)
            if (basisVariables[i] < numberOfConstraints)
                System.out.println("x"
                        + basisVariables[i]
                        + " = "
                        + df.format(tableaux[numberOfVariables][numberOfConstraints + i]));
    }

    private void simplex() {
        while (true) {
            showNewTableaux();
            int j = findProperNewBasicVariable();
            if (j == -1) break;
            int i = findProperLeavingBasicVariable(j);
            pivot(i, j);
            basisVariables[i] = i;
        }
    }

    /**
     * @return indeks kolumny wskazujący na nową zmienną bazową,  z najmniejszą wartością wiersza funkcji celu (koszt)
     * Wyrazy ujemne w wierszu funkcji celu oznaczają, że rozwiązanie można poprawić
     */
    private int findProperNewBasicVariable() {
        int newBasicColumn = 0;
        for (int j = 1; j < numberOfConstraints + numberOfVariables; j++) {
            if (tableaux[numberOfVariables][j] < tableaux[numberOfVariables][newBasicColumn])
                newBasicColumn = j;
        }
        if (tableaux[numberOfVariables][newBasicColumn] >= 0)
            return -1;
        else return newBasicColumn;
    }

    /**
     * Metoda polega na obliczeniu ilorazu wyrazow wolnych (RHS) poprzez podzielenie ich wartosci przez
     * wartosci znajdujace sie w nowym wektorze bazowym. Najwieksza wartosc oznacza, ktora zmienna opuszcza rozwiazanie bazowe.
     *
     * @param newBasicColumn - Zmienna typu int okreslajaca indeks nowego wektora bazowego dopuszczalnego.
     * @return zmienna typu int okreslajaca numer zmiennej bazowej, ktora opuszcza rozwiazanie bazowe
     */
    private int findProperLeavingBasicVariable(int newBasicColumn) {
        int rowLeavingBasicVariable = -1;
        for (int i = 0; i < numberOfVariables; i++) {
            if (tableaux[i][newBasicColumn] <= 0)
                continue;
            else if (rowLeavingBasicVariable == -1)
                rowLeavingBasicVariable = i;
            else if ((tableaux[i][numberOfConstraints
                    + numberOfVariables] / tableaux[i][newBasicColumn]) < (tableaux[rowLeavingBasicVariable][numberOfConstraints
                    + numberOfVariables] / tableaux[rowLeavingBasicVariable][newBasicColumn]))
                rowLeavingBasicVariable = i;
        }
        if (rowLeavingBasicVariable == -1) throw new ArithmeticException("PROGRAM NIEOGRANICZONY");
        return rowLeavingBasicVariable;
    }

    /**
     * Zastosowanie metody Gaussa-Jordana do obliczenia macierzy odwrotnych.
     * Rozwiązanie układu rownan polega na tym, że zerujemy wspolczynniki pod i nad przekatną naszej macierzy.
     * Operacje w eliminacji Gaussa-Jordana wykonywane są tylko na wierszach
     *
     * @param p - numer wiersza
     * @param q - numer kolumny
     */
    private void pivot(int p, int q) {

        for (int i = 0; i <= numberOfVariables; i++)
            for (int j = 0; j <= numberOfConstraints
                    + numberOfVariables; j++)
                if (i != p && j != q)
                    tableaux[i][j] -= tableaux[p][j] * tableaux[i][q] / tableaux[p][q];

        for (int i = 0; i <= numberOfVariables; i++)
            if (i != p)
                tableaux[i][q] = 0.0;

        for (int j = 0; j <= numberOfConstraints + numberOfVariables; j++)
            if (j != q)
                tableaux[p][j] /= tableaux[p][q];
        tableaux[p][q] = 1.0;
    }
}
