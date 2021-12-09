package id.co.bspguard.android.bravo.accounts;

public class PositionDataset {

    String id_position, name_position;

    public PositionDataset(){}

    public PositionDataset(String id_position, String name_position){
        this.id_position = id_position;
        this.name_position = name_position;
    }

    public String getId_position() {
        return id_position;
    }

    public void setId_position(String id_position) {
        this.id_position = id_position;
    }

    public String getName_position() {
        return name_position;
    }

    public void setName_position(String name_position) {
        this.name_position = name_position;
    }
}
