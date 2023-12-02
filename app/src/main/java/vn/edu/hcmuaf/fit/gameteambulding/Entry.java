package vn.edu.hcmuaf.fit.gameteambulding;

public class Entry {
    public String ID;
    public String Entry_link;

    public Entry( String entry_link) {
        Entry_link = entry_link;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEntry_link(String entry_link) {
        Entry_link = entry_link;
    }

    public String getID() {
        return ID;
    }

    public String getEntry_link() {
        return Entry_link;
    }
}
