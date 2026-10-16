/**
 * WORKED EXAMPLE — not part of the graded application.
 *
 * A tiny, deliberately-broken sample used to practise the JBMC + Semgrep
 * workflow before applying it to the real app in src/. See WALKTHROUGH.md
 * in this folder for the step-by-step commands and what to expect.
 */
public class ArraySample {

    // Deliberate off-by-one: loop condition uses <= against array.length,
    // so the final iteration reads one element past the end of the array.
    public static int sumAll(int[] values) {
        int total = 0;
        for (int i = 0; i <= values.length; i++) {
            total += values[i];
        }
        return total;
    }

    public static void main(String[] args) {
        int[] sample = { 1, 2, 3, 4, 5 };
        System.out.println("Sum: " + sumAll(sample));
    }
}
