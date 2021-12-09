package id.co.bspguard.android.bravo.accounts;

public class PemotonganDataSet {

    String id_pemotongan, name_pemotongan;

    public PemotonganDataSet() {
    }

    public PemotonganDataSet(String id_pemotongan, String name_pemotongan) {
        this.id_pemotongan = id_pemotongan;
        this.name_pemotongan = name_pemotongan;
    }

    public String getId_pemotongan() {
        return id_pemotongan;
    }

    public void setId_pemotongan(String id_pemotongan) {
        this.id_pemotongan = id_pemotongan;
    }

    public String getName_pemotongan() {
        return name_pemotongan;
    }

    public void setName_pemotongan(String name_pemotongan) {
        this.name_pemotongan = name_pemotongan;
    }
}
