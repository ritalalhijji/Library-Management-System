// Represents a library book and extends LibraryItem to apply inheritance.
public class Book extends LibraryItem {
    private BookCategory category;
    private boolean isAvailable;

    public Book(int bookNumber, String title, BookCategory category) {
        super(bookNumber, title);
        this.category = category;
        this.isAvailable = true;
    }

    public int getBookNumber() {
        return getItemNumber();
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrow() {
        isAvailable = false;
    }

    public void returnBook() {
        isAvailable = true;
    }

    public String toFileString() {
        return getBookNumber() + "," + getTitle() + "," + category + "," + isAvailable;
    }

    public String toString() {
        return "Book Number: " + getBookNumber() +
                " | Title: " + getTitle() +
                " | Category: " + category +
                " | Available: " + isAvailable;
    }
}
