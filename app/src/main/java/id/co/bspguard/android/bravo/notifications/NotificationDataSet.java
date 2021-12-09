package id.co.bspguard.android.bravo.notifications;

import org.json.JSONObject;

public class NotificationDataSet {
  String id, title, description,  nextUrl, type, read_at;
  JSONObject object;

  public NotificationDataSet(){

  }

  public NotificationDataSet(String id, String title, String description, String nextUrl, String type, String read_at, JSONObject object) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.nextUrl = nextUrl;
    this.type = type;
    this.read_at = read_at;
    this.object = object;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getNextUrl() {
    return nextUrl;
  }

  public void setNextUrl(String nextUrl) {
    this.nextUrl = nextUrl;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRead_at() {
    return read_at;
  }

  public void setRead_at(String read_at) {
    this.read_at = read_at;
  }

  public JSONObject getObject() {
    return object;
  }

  public void setObject(JSONObject object) {
    this.object = object;
  }
}
