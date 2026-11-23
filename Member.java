// Represents a library member and stores the books borrowed by that member.
public class Member {
    private int memberID;
    private String name;
    private Book[] borrowedBooks;
    private int borrowedCount;

    public Member(int memberID, String name) {
        this.memberID = memberID;
        this.name = name;
        this.borrowedBooks = new Book[10];
        this.borrowedCount = 0;
    }

    public int getID() {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canBorrow() {
        return borrowedCount < borrowedBooks.length;
    }

    public void borrowBook(Book book) {
        if (borrowedCount < borrowedBooks.length) {
            borrowedBooks[borrowedCount] = book;
            borrowedCount++;
        } else {
            System.out.println("Borrowed books array is full.");
        }
    }

    public void returnBook(Book book) {
        for (int i = 0; i < borrowedCount; i++) {
            if (borrowedBooks[i] == book) {
                for (int j = i; j < borrowedCount - 1; j++) {
                    borrowedBooks[j] = borrowedBooks[j + 1];
                }
                borrowedBooks[borrowedCount - 1] = null;
                borrowedCount--;
                return;
            }
        }
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public String toFileString() {
        return memberID + "," + name;
    }

    public String toString() {
        return "Member ID: " + memberID + " | Name: " + name + " | Borrowed Books: " + getBorrowedCount();
    }
}
