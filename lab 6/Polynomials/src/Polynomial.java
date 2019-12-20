import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polynomial {
    private int length;
    private List<Integer> coefficients;

    public Polynomial() {
    }

    Polynomial(int length) {
        this.length = length;
        this.coefficients = new ArrayList<>(Collections.nCopies(length, 0));
    }

    Polynomial(List<Integer> coefficients) {
        this.length = coefficients.size();
        this.coefficients = coefficients;
    }

    int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    List<Integer> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    void print() {
        for (int i = 0; i < this.length - 1; i++) {
            System.out.print(this.coefficients.get(i) + "x^" + i + " + ");
        }
        System.out.print(this.coefficients.get(this.length - 1) + "x^" + (this.length - 1));
        System.out.println();
    }

    void addZeroesToCoefficients(int length) {
        for (int i = this.length; i < length; i++) {
            this.coefficients.add(0);
        }
        this.length = length;
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "length=" + length +
                ", coefficients=" + coefficients +
                '}';
    }
}
