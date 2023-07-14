import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Expense implements Serializable {
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

    // Getters and setters

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

    @Override
    public String toString() {
        return "Expense [date=" + date + ", amount=" + amount + ", category=" + category + ", description=" + description + "]";
    }
}

class Category implements Serializable {
    private String name;
    private List<Expense> expenses;

    public Category(String name) {
        this.name = name;
        expenses = new ArrayList<>();
    }

    // Getters and setters

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

    @Override
    public String toString() {
        return "Category [name=" + name + ", expenses=" + expenses + "]";
    }
}

class ExpenseTracker implements Serializable {
    private List<Category> categories;

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
        saveExpenseData(); // Save data after recording expense
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        System.out.println("Monthly Expense Report - " + dateFormat.format(new Date(year - 1900, month - 1, 1)));

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

    // Get total expense for a category

    public double getTotalCategoryExpense(String categoryName) {
        Category category = findCategory(categoryName);
        if (category != null) {
            double totalExpense = 0.0;
            for (Expense expense : category.getExpenses()) {
                totalExpense += expense.getAmount();
            }
            return totalExpense;
        } else {
            System.out.println("Category not found.");
            return 0.0;
        }
    }

    // Get expenses within a date range

    public List<Expense> getExpensesWithinDateRange(String startDate, String endDate) {
        List<Expense> expensesWithinRange = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            for (Category category : categories) {
                for (Expense expense : category.getExpenses()) {
                    Date expenseDate = dateFormat.parse(expense.getDate());
                    if (expenseDate.compareTo(start) >= 0 && expenseDate.compareTo(end) <= 0) {
                        expensesWithinRange.add(expense);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
        return expensesWithinRange;
    }

    // Get top expense categories

    public List<Category> getTopExpenseCategories(int count) {
        if (count <= 0) {
            System.out.println("Invalid count.");
            return new ArrayList<>();
        }

        List<Category> topCategories = new ArrayList<>(categories);
        topCategories.sort((c1, c2) -> {
            double c1Expense = getTotalCategoryExpense(c1.getName());
            double c2Expense = getTotalCategoryExpense(c2.getName());
            return Double.compare(c2Expense, c1Expense);
        });

        if (topCategories.size() > count) {
            topCategories = topCategories.subList(0, count);
        }

        return topCategories;
    }

    // Save expense data to a file

    public void saveExpenseData() {
        String fileName = "expense_data.ser";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            System.out.println("Expense data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving expense data: " + e.getMessage());
        }
    }

    // Load expense data from a file

    public static ExpenseTracker loadExpenseData() {
        String fileName = "expense_data.ser";
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            ExpenseTracker expenseTracker = (ExpenseTracker) inputStream.readObject();
            System.out.println("Expense data loaded successfully.");
            return expenseTracker;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error occurred while loading expense data: " + e.getMessage());
            return new ExpenseTracker();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ExpenseTracker expenseTracker = ExpenseTracker.loadExpenseData();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Expense Tracker");
            System.out.println("1. Add Category");
            System.out.println("2. Record Expense");
            System.out.println("3. Generate Monthly Expense Report");
            System.out.println("4. Get Total Category Expense");
            System.out.println("5. Get Expenses within Date Range");
            System.out.println("6. Get Top Expense Categories");
            System.out.println("7. Load Expense Data");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    scanner.nextLine();
                    System.out.print("Enter expense category: ");
                    String expenseCategory = scanner.nextLine();
                    System.out.print("Enter expense description: ");
                    String description = scanner.nextLine();
                    Expense expense = new Expense(date, amount, expenseCategory, description);
                    expenseTracker.recordExpense(expense);
                    break;
                case 3:
                    System.out.print("Enter month: ");
                    int month = scanner.nextInt();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
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
                    List<Expense> expensesWithinRange = expenseTracker.getExpensesWithinDateRange(startDate, endDate);
                    System.out.println("Expenses within date range: ");
                    for (Expense exp : expensesWithinRange) {
                        System.out.println(exp);
                    }
                    break;
                case 6:
                    System.out.print("Enter count: ");
                    int count = scanner.nextInt();
                    scanner.nextLine();
                    List<Category> topExpenseCategories = expenseTracker.getTopExpenseCategories(count);
                    System.out.println("Top " + count + " Expense Categories: ");
                    for (Category cat : topExpenseCategories) {
                        System.out.println(cat);
                    }
                    break;
                case 7:
                    expenseTracker = ExpenseTracker.loadExpenseData();
                    break;
                case 8:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }
}
