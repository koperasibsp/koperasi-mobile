package id.co.bspguard.android.bravo.news;

public class NewsDataSet {
    String id, image, title, description, timestamp;

    public NewsDataSet(){}

    public NewsDataSet(String id, String image, String title, String description, String timestamp) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
