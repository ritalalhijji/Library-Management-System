// Parent class that contains the shared item number and title attributes.
public class LibraryItem {
    private int itemNumber;
    private String title;

    public LibraryItem(int itemNumber, String title) {
        this.itemNumber = itemNumber;
        this.title = title;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
