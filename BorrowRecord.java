// Stores the borrowing details between a member and a book.
public class BorrowRecord {
    private int recordID;
    private Member member;
    private Book book;
    private String borrowDate;
    private String returnDate;

    public BorrowRecord(int recordID, Member member, Book book, String borrowDate) {
        this.recordID = recordID;
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = null;
    }

    public int getRecordID() {
        return recordID;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public String toFileString() {
        String savedReturnDate = returnDate == null ? "Not Returned" : returnDate;
        return recordID + "," + member.getID() + "," + book.getBookNumber() + "," + borrowDate + "," + savedReturnDate;
    }

    public String toString() {
        return "Record ID: " + recordID +
                " | Member ID: " + member.getID() +
                " | Book Number: " + book.getBookNumber() +
                " | Borrow Date: " + borrowDate +
                " | Status: " + (isReturned() ? "Returned" : "Borrowed");
    }
}
