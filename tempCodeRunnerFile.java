import java.text.SimpleDateFormat;
import java.util.*;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;



class Expense {
    private String date;
    private double amount;
    private String category;
    private String description;

    public Expense(String date, double amount, String category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    // Getters and setters...

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Category {
    private String name;
    private List<Expense> expenses;

    public Category(String name) {
        this.name = name;
        expenses = new ArrayList<>();
    }

    // Getters and setters...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    // Add an expense to the category

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}

class ExpenseTracker {
    private List<Category> categories;

    // Save expense data to a file
    public void saveExpenseDataToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(categories);
            System.out.println("Expense data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving expense data: " + e.getMessage());
        }
    }

    // Load expense data from a file
    public void loadExpenseDataFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            categories = (List<Category>) inputStream.readObject();
            System.out.println("Expense data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while loading expense data: " + e.getMessage());
        }
    }

    public ExpenseTracker() {
        categories = new ArrayList<>();
    }

    // Add a new category

    public void addCategory(Category category) {
        categories.add(category);
    }

    // Record a new expense

    public void recordExpense(Expense expense) {
        Category category = findCategory(expense.getCategory());
        if (category != null) {
            category.addExpense(expense);
            System.out.println("Expense recorded successfully.");
        } else {
            System.out.println("Category not found. Please create the category first.");
        }
    }

    // Find a category by name

    private Category findCategory(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return null;
    }

   // Generate a monthly expense report
public void generateMonthlyExpenseReport(int month, int year) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    System.out.println("Monthly Expense Report - " + String.format("%02d/%d", month, year));

    for (Category category : categories) {
        double totalCategoryExpense = 0.0;

        for (Expense expense : category.getExpenses()) {
            String[] dateParts = expense.getDate().split("/");
            int expenseMonth = Integer.parseInt(dateParts[1]);
            int expenseYear = Integer.parseInt(dateParts[2]);

            if (expenseMonth == month && expenseYear == year) {
                totalCategoryExpense += expense.getAmount();
            }
        }

        System.out.println(category.getName() + ": $" + totalCategoryExpense);
    }
}


    // Additional Features:

    // Get total expenses for a specific category
    public double getTotalCategoryExpense(String categoryName) {
        Category category = findCategory(categoryName);
        if (category != null) {
            double totalCategoryExpense = 0.0;
            for (Expense expense : category.getExpenses()) {
                totalCategoryExpense += expense.getAmount();
            }
            return totalCategoryExpense;
        }
        return 0.0;
    }

    // Get expenses within a date range
    public List<Expense> getExpensesWithinDateRange(String startDate, String endDate) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Category category : categories) {
            for (Expense expense : category.getExpenses()) {
                if (isWithinDateRange(expense.getDate(), startDate, endDate)) {
                    filteredExpenses.add(expense);
                }
            }
        }
        return filteredExpenses;
    }

    // Check if a date is within a specified range
    private boolean isWithinDateRange(String date, String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date expenseDate = dateFormat.parse(date);
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return expenseDate.compareTo(start) >= 0 && expenseDate.compareTo(end) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Get top N categories with the highest expenses
    public List<Category> getTopExpenseCategories(int n) {
        List<Category> sortedCategories = new ArrayList<>(categories);
        sortedCategories.sort((c1, c2) -> Double.compare(getTotalCategoryExpense(c2.getName()), getTotalCategoryExpense(c1.getName())));
        return sortedCategories.subList(0, Math.min(n, sortedCategories.size()));
    }
}



public class Main {
    private static final String DATA_FILE = "expense_data.ser";

    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker();

        // Load expense data from file (if exists)
        loadExpenseData(expenseTracker);

        Scanner scanner = new Scanner(System.in);


        int choice = 0;
        while (choice != 8) {
            System.out.println("Expense Tracker");
            System.out.println("1. Add Category");
            System.out.println("2. Record Expense");
            System.out.println("3. Generate Monthly Expense Report");
            System.out.println("4. Get Total Category Expense");
            System.out.println("5. Get Expenses within Date Range");
            System.out.println("6. Get Top Expense Categories");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter category name: ");
                    String categoryName = scanner.nextLine();
                    Category category = new Category(categoryName);
                    expenseTracker.addCategory(category);
                    System.out.println("Category added successfully.");
                    break;
                case 2:
                    System.out.print("Enter expense date (dd/MM/yyyy): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter expense amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter expense category: ");
                    String expenseCategory = scanner.nextLine();
                    System.out.print("Enter expense description: ");
                    String description = scanner.nextLine();
                    Expense expense = new Expense(date, amount, expenseCategory, description);
                    expenseTracker.recordExpense(expense);
                    break;
                case 3:
                    System.out.print("Enter month (1-12): ");
                    int month = scanner.nextInt();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    expenseTracker.generateMonthlyExpenseReport(month, year);
                    break;
                case 4:
                    System.out.print("Enter category name: ");
                    String categoryName2 = scanner.nextLine();
                    double totalExpense = expenseTracker.getTotalCategoryExpense(categoryName2);
                    System.out.println("Total expense for category " + categoryName2 + ": $" + totalExpense);
                    break;
                case 5:
                    System.out.print("Enter start date (dd/MM/yyyy): ");
                    String startDate = scanner.nextLine();
                    System.out.print("Enter end date (dd/MM/yyyy): ");
                    String endDate = scanner.nextLine();
                    List<Expense> expensesInRange = expenseTracker.getExpensesWithinDateRange(startDate, endDate);
                    System.out.println("Expenses within date range:");
                    for (Expense exp : expensesInRange) {
                        System.out.println("Date: " + exp.getDate() + ", Amount: $" + exp.getAmount() + ", Category: " + exp.getCategory() + ", Description: " + exp.getDescription());
                    }
                    break;
                case 6:
                    System.out.print("Enter the number of top categories to display: ");
                    int topN = scanner.nextInt();
                    List<Category> topCategories = expenseTracker.getTopExpenseCategories(topN);
                    System.out.println("Top " + topN + " Expense Categories:");
                    for (Category cat : topCategories) {
                        System.out.println("Category: " + cat.getName() + ", Total Expense: $" + expenseTracker.getTotalCategoryExpense(cat.getName()));
                    }
                    break;
                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                }
            }

            scanner.close();
            saveExpenseData(expenseTracker);}


    private static void loadExpenseData(ExpenseTracker expenseTracker) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            expenseTracker = (ExpenseTracker) inputStream.readObject();
            System.out.println("Expense data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while loading expense data: " + e.getMessage());
        }
    }

    private static void saveExpenseData(ExpenseTracker expenseTracker) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            outputStream.writeObject(expenseTracker);
            System.out.println("Expense data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving expense data: " + e.getMessage());
        }
    }
}