package lk.cmb.app;

public class NewsItem {
    private String title;
    private String description;
    private String imageurl; // must match Firebase field name
    private String date;

    public NewsItem() {}

    public String getTitle() { return title != null ? title.replaceAll("^\"|\"$", "") : ""; }
    public String getDescription() { return description != null ? description.replaceAll("^\"|\"$", "") : ""; }
    public String getImageurl() { return imageurl != null ? imageurl : ""; }
    public String getDate() { return date != null ? date.replaceAll("^\"|\"$", "") : ""; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setImageurl(String imageurl) { this.imageurl = imageurl; }
    public void setDate(String date) { this.date = date; }
}
