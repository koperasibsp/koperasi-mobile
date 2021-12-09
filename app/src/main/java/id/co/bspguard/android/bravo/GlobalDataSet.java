package id.co.bspguard.android.bravo;

public class GlobalDataSet {
    String id_global, name_global, description_global, content_global;

    public GlobalDataSet(){}

    public GlobalDataSet(String id_global, String name_global, String description_global, String content_global){
        this.id_global = id_global;
        this.name_global = name_global;
        this.description_global = description_global;
        this.content_global = content_global;
    }

    public String getid_global() {
        return id_global;
    }

    public void setid_global(String id_global) {
        this.id_global = id_global;
    }

    public String getname_global() {
        return name_global;
    }

    public void setname_global(String name_global) {
        this.name_global = name_global;
    }

    public String getDescription_global() {
        return description_global;
    }

    public void setDescription_global(String description_global) {
        this.description_global = description_global;
    }

    public String getContent_global() {
        return content_global;
    }

    public void setContent_global(String content_global) {
        this.content_global = content_global;
    }
}

