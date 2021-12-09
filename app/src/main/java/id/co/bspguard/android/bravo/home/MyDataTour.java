package id.co.bspguard.android.bravo.home;

public class MyDataTour {

    String id, image, deskripsi, judul, harga, rate, include, exclude, term, harga_family, harga_group;

    public MyDataTour(){

    }

    public MyDataTour(String id, String image, String deskripsi, String judul, String harga, String rate, String include, String exclude, String term, String harga_family, String harga_group) {
        this.id = id;
        this.image = image;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.harga = harga;
        this.rate = rate;
        this.include = include;
        this.exclude = exclude;
        this.term = term;
        this.harga_family = harga_family;
        this.harga_group = harga_group;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getHarga_family() {
        return harga_family;
    }

    public void setHarga_family(String harga_family) {
        this.harga_family = harga_family;
    }

    public String getHarga_group() {
        return harga_group;
    }

    public void setHarga_group(String harga_group) {
        this.harga_group = harga_group;
    }
}
