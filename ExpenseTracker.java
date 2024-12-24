import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Double> amounts = new ArrayList<>();

        System.out.println("Enter expenses (type 'done' to finish):");
        while (true) {
            System.out.print("Category: ");
            String category = sc.next();
            if (category.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Amount: ");
            double amount = sc.nextDouble();

            if (categories.contains(category)) {
                int index = categories.indexOf(category);
                amounts.set(index, amounts.get(index) + amount);
            } else {
                categories.add(category);
                amounts.add(amount);
            }
        }

        System.out.println("\nExpense Breakdown:");
        double total = 0;
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(categories.get(i) + ": Rs " + amounts.get(i));
            total += amounts.get(i);
        }
        System.out.println("Total Expenses: Rs " + total);

        System.out.println("\nSimple Chart:");
        for (int i = 0; i < categories.size(); i++) {
            double percentage = (amounts.get(i) / total) * 100;
            System.out.printf("%s: %.2f%%\n", categories.get(i), percentage);
        }

        sc.close();
    }
}

