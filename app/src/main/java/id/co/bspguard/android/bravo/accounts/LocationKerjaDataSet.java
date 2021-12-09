package id.co.bspguard.android.bravo.accounts;

import java.util.List;

public class LocationKerjaDataSet {

    String id_kerja, name_kerja;
    private List<String> cities;


    public LocationKerjaDataSet(){}

    public LocationKerjaDataSet(String id_kerja, String name_kerja){
        this.id_kerja = id_kerja;
        this.name_kerja = name_kerja;
    }

    public String getId_kerja() {
        return id_kerja;
    }

    public void setId_kerja(String id_kerja) {
        this.id_kerja = id_kerja;
    }

    public String getName_kerja() {
        return name_kerja;
    }

    public void setName_kerja(String name_kerja) {
        this.name_kerja = name_kerja;
    }
}
