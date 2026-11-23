// Starts the program, handles user login, and displays the menu-based console interface.
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Auth auth = new Auth();

        System.out.print("Username: ");
        String username = input.next();
        System.out.print("Password: ");
        String password = input.next();
        input.nextLine();

        if (!auth.login(username, password)) {
            System.out.println("Access denied.");
            return;
        }

        LibraryManager library = new LibraryManager();
        int choice = -1;

        while (choice != 14) {
            printMenu();
            try {
                choice = Integer.parseInt(input.nextLine().trim());

                switch (choice) {
                    case 1:
                        System.out.print("Book Number: ");
                        int bookNumber = Integer.parseInt(input.nextLine().trim());
                        System.out.print("Title: ");
                        String title = input.nextLine();
                        System.out.print("Category (SCIENCE/ART/LITERATURE/OTHER): ");
                        BookCategory category = BookCategory.valueOf(input.nextLine().trim().toUpperCase());
                        library.addBook(bookNumber, title, category);
                        break;
                    case 2:
                        System.out.print("Member ID: ");
                        int memberID = Integer.parseInt(input.nextLine().trim());
                        System.out.print("Name: ");
                        String name = input.nextLine();
                        library.addMember(memberID, name);
                        break;
                    case 3:
                        System.out.print("Search by title keyword: ");
                        String keyword = input.nextLine();
                        Book[] results = library.searchBooks(keyword);
                        if (results.length == 0) {
                            System.out.println("No matching books found.");
                        } else {
                            for (int i = 0; i < results.length; i++) {
                                System.out.println(results[i]);
                            }
                        }
                        break;
                    case 4:
                        System.out.print("Book Number to update: ");
                        int updateNumber = Integer.parseInt(input.nextLine().trim());
                        System.out.print("New Title: ");
                        String newTitle = input.nextLine();
                        System.out.print("New Category (SCIENCE/ART/LITERATURE/OTHER): ");
                        BookCategory newCategory = BookCategory.valueOf(input.nextLine().trim().toUpperCase());
                        library.updateBook(updateNumber, newTitle, newCategory);
                        break;
                    case 5:
                        System.out.print("Member ID: ");
                        int mID = Integer.parseInt(input.nextLine().trim());
                        System.out.print("Book Number: ");
                        int bNo = Integer.parseInt(input.nextLine().trim());
                        library.borrowBook(mID, bNo);
                        break;
                    case 6:
                        System.out.print("Record ID: ");
                        int rID = Integer.parseInt(input.nextLine().trim());
                        library.returnBook(rID);
                        break;
                    case 7:
                        library.listAll();
                        break;
                    case 8:
                        library.sortByTitle();
                        break;
                    case 9:
                        library.saveAllDataToFiles();
                        break;
                    case 10:
                        library.loadAllDataFromFiles();
                        break;
                    case 11:
                        library.saveMembersToFile();
                        break;
                    case 12:
                        library.saveBorrowRecordsToFile();
                        break;
                    case 13:
                        System.out.print("Book Number to delete: ");
                        int deleteNumber = Integer.parseInt(input.nextLine().trim());
                        library.deleteBook(deleteNumber);
                        break;
                    case 14:
                        auth.logout();
                        System.out.println("Goodbye.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\nLibrary Management System");
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Search Book by Title");
        System.out.println("4. Update Book");
        System.out.println("5. Borrow Book");
        System.out.println("6. Return Book");
        System.out.println("7. List All Data");
        System.out.println("8. Sort Books by Title");
        System.out.println("9. Save All Data To Files");
        System.out.println("10. Load All Data From Files");
        System.out.println("11. Save Members To File");
        System.out.println("12. Save Borrow Records To File");
        System.out.println("13. Delete Book");
        System.out.println("14. Exit");
        System.out.print("Choice: ");
    }
}
