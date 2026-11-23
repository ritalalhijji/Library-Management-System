// Contains the main library operations such as add, search, update, borrow, return, delete, sort, save, and load.
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private Book[] books;
    private Member[] members;
    private BorrowRecord[] records;
    private int bookCount;
    private int memberCount;
    private int recordCount;
    private int[] dailyBorrowCount;
    private int nextRecordID = 1;

    public LibraryManager() {
        books = new Book[100];
        members = new Member[100];
        records = new BorrowRecord[100];
        bookCount = 0;
        memberCount = 0;
        recordCount = 0;
        dailyBorrowCount = new int[7];
    }

    public void addBook(Book book) {
        if (book == null || book.getBookNumber() <= 0 || book.getTitle() == null || book.getTitle().trim().isEmpty() || book.getCategory() == null) {
            System.out.println("Invalid book information.");
            return;
        }
        if (searchBooks(book.getBookNumber()) != null) {
            System.out.println("Book number already exists.");
            return;
        }
        if (bookCount < books.length) {
            books[bookCount] = book;
            bookCount++;
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Books array is full.");
        }
    }

    public void addBook(int bookNumber, String title, BookCategory category) {
        addBook(new Book(bookNumber, title, category));
    }

    public void addMember(Member member) {
        if (member == null || member.getID() <= 0 || member.getName() == null || member.getName().trim().isEmpty()) {
            System.out.println("Invalid member information.");
            return;
        }
        if (searchMember(member.getID()) != null) {
            System.out.println("Member ID already exists.");
            return;
        }
        if (memberCount < members.length) {
            members[memberCount] = member;
            memberCount++;
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Members array is full.");
        }
    }

    public void addMember(int memberID, String name) {
        addMember(new Member(memberID, name));
    }

    public Book searchBooks(int bookNumber) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookNumber() == bookNumber) {
                return books[i];
            }
        }
        return null;
    }

    public Book[] searchBooks(String keyword) {
        Book[] results = new Book[bookCount];
        int resultCount = 0;
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results[resultCount] = books[i];
                resultCount++;
            }
        }
        Book[] finalResults = new Book[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }

    public Member searchMember(int memberID) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getID() == memberID) {
                return members[i];
            }
        }
        return null;
    }

    public void updateBook(int bookNumber, String newTitle, BookCategory newCategory) {
        Book book = searchBooks(bookNumber);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (newTitle == null || newTitle.trim().isEmpty() || newCategory == null) {
            System.out.println("Invalid book information.");
            return;
        }
        book.setTitle(newTitle);
        book.setCategory(newCategory);
        System.out.println("Book updated successfully.");
    }

    public void deleteBook(int bookNumber) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookNumber() == bookNumber) {
                if (!books[i].isAvailable() || hasRecordForBook(bookNumber)) {
                    System.out.println("Cannot delete a borrowed book.");
                    return;
                }
                for (int j = i; j < bookCount - 1; j++) {
                    books[j] = books[j + 1];
                }
                books[bookCount - 1] = null;
                bookCount--;
                System.out.println("Book deleted successfully.");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    private boolean hasRecordForBook(int bookNumber) {
        for (int i = 0; i < recordCount; i++) {
            if (records[i].getBook().getBookNumber() == bookNumber) return true;
        }
        return false;
    }

    public void borrowBook(int memberID, int bookNumber) {
        Member member = searchMember(memberID);
        Book book = searchBooks(bookNumber);

        if (member == null || book == null) {
            System.out.println("Invalid member ID or book number.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book not available.");
            return;
        }

        if (recordCount >= records.length) {
            System.out.println("Borrow records array is full.");
            return;
        }

        if (!member.canBorrow()) {
            System.out.println("Member has reached the borrowing limit (10 books).");
            return;
        }
        book.borrow();
        member.borrowBook(book);
        BorrowRecord record = new BorrowRecord(nextRecordID++, member, book, LocalDate.now().toString());
        records[recordCount] = record;
        recordCount++;
        updateDailyBorrowCount(LocalDate.now().getDayOfWeek().getValue() - 1);
        System.out.println("Borrowed successfully: " + record);
    }

    private void updateDailyBorrowCount(int dayIndex) {
        if (dayIndex >= 0 && dayIndex < dailyBorrowCount.length) {
            dailyBorrowCount[dayIndex]++;
        }
    }

    public void returnBook(int recordID) {
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = records[i];
            if (record.getRecordID() == recordID) {
                if (record.isReturned()) {
                    System.out.println("This book was already returned.");
                } else {
                    record.setReturnDate(LocalDate.now().toString());
                    record.getBook().returnBook();
                    record.getMember().returnBook(record.getBook());
                    System.out.println("Book returned successfully.");
                }
                return;
            }
        }
        System.out.println("Record ID not found.");
    }

    public void listAll() {
        if (bookCount == 0 && memberCount == 0 && recordCount == 0) {
            System.out.println("No data available.");
            return;
        }

        System.out.println("\nBooks:");
        if (bookCount == 0) {
            System.out.println("No books available.");
        } else {
            for (int i = 0; i < bookCount; i++) {
                System.out.println(books[i]);
            }
        }

        System.out.println("\nMembers:");
        if (memberCount == 0) {
            System.out.println("No members available.");
        } else {
            for (int i = 0; i < memberCount; i++) {
                System.out.println(members[i]);
            }
        }

        System.out.println("\nBorrow Records:");
        if (recordCount == 0) {
            System.out.println("No borrow records available.");
        } else {
            for (int i = 0; i < recordCount; i++) {
                System.out.println(records[i]);
            }
        }
    }

    public void sortByTitle() {
        for (int i = 0; i < bookCount - 1; i++) {
            for (int j = 0; j < bookCount - i - 1; j++) {
                if (books[j].getTitle().compareToIgnoreCase(books[j + 1].getTitle()) > 0) {
                    Book temp = books[j];
                    books[j] = books[j + 1];
                    books[j + 1] = temp;
                }
            }
        }
        System.out.println("Books sorted by title.");
    }

    // Tab-separated fields support commas in titles/names. Tabs and line breaks are escaped.
    private static String encode(String value) {
        return value.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String decode(String value) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                if (next == 't') out.append('\t');
                else if (next == 'n') out.append('\n');
                else if (next == 'r') out.append('\r');
                else out.append(next);
            } else out.append(c);
        }
        return out.toString();
    }

    private static void writeLines(String filename, List<String> lines) throws Exception {
        Path target = Path.of(filename);
        Path temp = Path.of(filename + ".tmp");
        Files.write(temp, lines, StandardCharsets.UTF_8);
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String[] fields(String line, int expected) {
        String[] values = line.split("\t", -1);
        if (values.length != expected) throw new IllegalArgumentException("Invalid data format");
        return values;
    }

    public void saveBooksToFile() {
        try {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < bookCount; i++) {
                Book b = books[i];
                lines.add(b.getBookNumber() + "\t" + encode(b.getTitle()) + "\t" + b.getCategory() + "\t" + b.isAvailable());
            }
            writeLines("books.txt", lines);
            System.out.println("Books saved to books.txt.");
        } catch (Exception e) { System.out.println("Error saving books: " + e.getMessage()); }
    }

    public void saveMembersToFile() {
        try {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < memberCount; i++) {
                Member m = members[i];
                lines.add(m.getID() + "\t" + encode(m.getName()));
            }
            writeLines("members.txt", lines);
            System.out.println("Members saved to members.txt.");
        } catch (Exception e) { System.out.println("Error saving members: " + e.getMessage()); }
    }

    public void saveBorrowRecordsToFile() {
        try {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < recordCount; i++) {
                BorrowRecord r = records[i];
                lines.add(r.getRecordID() + "\t" + r.getMember().getID() + "\t" + r.getBook().getBookNumber()
                    + "\t" + r.getBorrowDate() + "\t" + (r.isReturned() ? r.getReturnDate() : "Not Returned"));
            }
            writeLines("borrow_records.txt", lines);
            System.out.println("Borrow records saved to borrow_records.txt.");
        } catch (Exception e) { System.out.println("Error saving records: " + e.getMessage()); }
    }

    public void saveAllDataToFiles() {
        saveBooksToFile();
        saveMembersToFile();
        saveBorrowRecordsToFile();
    }

    // Load into temporary arrays first; never replace working data with a partial or invalid snapshot.
    public void loadAllDataFromFiles() {
        try {
            Path bookFile = Path.of("books.txt"), memberFile = Path.of("members.txt"), recordFile = Path.of("borrow_records.txt");
            if (!Files.exists(bookFile) || !Files.exists(memberFile) || !Files.exists(recordFile)) {
                System.out.println("All three data files are required to load a complete snapshot.");
                return;
            }
            List<String> bookLines = Files.readAllLines(bookFile, StandardCharsets.UTF_8);
            List<String> memberLines = Files.readAllLines(memberFile, StandardCharsets.UTF_8);
            List<String> recordLines = Files.readAllLines(recordFile, StandardCharsets.UTF_8);
            if (bookLines.size() > 100 || memberLines.size() > 100 || recordLines.size() > 100)
                throw new IllegalArgumentException("Saved data exceeds system capacity");

            LibraryManager loaded = new LibraryManager();
            for (String line : bookLines) {
                String[] f = line.contains("\t") ? fields(line, 4) : line.split(",", 4);
                if (f.length != 4) throw new IllegalArgumentException("Invalid book row");
                int id = Integer.parseInt(f[0]);
                if (id <= 0 || loaded.searchBooks(id) != null) throw new IllegalArgumentException("Duplicate/invalid book number");
                String title = line.contains("\t") ? decode(f[1]) : f[1];
                if (title.isBlank()) throw new IllegalArgumentException("Empty book title");
                Book b = new Book(id, title, BookCategory.valueOf(f[2]));
                if (!f[3].equals("true") && !f[3].equals("false")) throw new IllegalArgumentException("Invalid availability");
                if (f[3].equals("false")) b.borrow();
                loaded.books[loaded.bookCount++] = b;
            }
            for (String line : memberLines) {
                String[] f = line.contains("\t") ? fields(line, 2) : line.split(",", 2);
                if (f.length != 2) throw new IllegalArgumentException("Invalid member row");
                int id = Integer.parseInt(f[0]);
                if (id <= 0 || loaded.searchMember(id) != null) throw new IllegalArgumentException("Duplicate/invalid member ID");
                String name = line.contains("\t") ? decode(f[1]) : f[1];
                if (name.isBlank()) throw new IllegalArgumentException("Empty member name");
                loaded.members[loaded.memberCount++] = new Member(id, name);
            }
            int maxRecordID = 0;
            java.util.Set<Integer> usedRecordIDs = new java.util.HashSet<>();
            java.util.Set<Integer> activeBooks = new java.util.HashSet<>();
            for (String line : recordLines) {
                String[] f = line.contains("\t") ? fields(line, 5) : line.split(",", 5);
                if (f.length != 5) throw new IllegalArgumentException("Invalid record row");
                int id = Integer.parseInt(f[0]);
                if (id <= 0 || !usedRecordIDs.add(id)) throw new IllegalArgumentException("Duplicate/invalid record ID");
                Member m = loaded.searchMember(Integer.parseInt(f[1]));
                Book b = loaded.searchBooks(Integer.parseInt(f[2]));
                if (m == null || b == null) throw new IllegalArgumentException("Record refers to a missing member/book");
                BorrowRecord r = new BorrowRecord(id, m, b, f[3]);
                if (!f[4].equals("Not Returned")) r.setReturnDate(f[4]);
                else {
                    if (!activeBooks.add(b.getBookNumber()) || !m.canBorrow())
                        throw new IllegalArgumentException("Conflicting active loans or member limit exceeded");
                    m.borrowBook(b);
                }
                loaded.records[loaded.recordCount++] = r;
                maxRecordID = Math.max(maxRecordID, id);
            }
            for (int i = 0; i < loaded.bookCount; i++) {
                Book b = loaded.books[i];
                if (b.isAvailable() == activeBooks.contains(b.getBookNumber()))
                    throw new IllegalArgumentException("Book availability conflicts with loan records");
            }
            this.books = loaded.books;
            this.members = loaded.members;
            this.records = loaded.records;
            this.bookCount = loaded.bookCount;
            this.memberCount = loaded.memberCount;
            this.recordCount = loaded.recordCount;
            this.nextRecordID = maxRecordID + 1;
            this.dailyBorrowCount = new int[7];
            System.out.println("All library data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Load cancelled; current data preserved: " + e.getMessage());
        }
    }

    public void loadBooksFromFile() { loadAllDataFromFiles(); }
    public void loadMembersFromFile() { loadAllDataFromFiles(); }
    public void loadBorrowRecordsFromFile() { loadAllDataFromFiles(); }
}
