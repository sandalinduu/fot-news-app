package lk.cmb.app;

public class NewsItem {
    public String title;
    public String description;
    public String date;
    public String imageUrl; // Optional, can be null

    public NewsItem() {
        // Default constructor required for calls to DataSnapshot.getValue(NewsItem.class)
    }

    public NewsItem(String title, String description, String date, String imageUrl) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
}