package id.co.bspguard.android.bravo.memberdeposit;

public class ListDepositDataset {
  String id, deposit_id, value, deposit_name;

  public ListDepositDataset(){

  }

  public ListDepositDataset(String id, String deposit_id, String value, String deposit_name) {
    this.id = id;
    this.deposit_id = deposit_id;
    this.value = value;
    this.deposit_name = deposit_name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeposit_id() {
    return deposit_id;
  }

  public void setDeposit_id(String deposit_id) {
    this.deposit_id = deposit_id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDeposit_name() {
    return deposit_name;
  }

  public void setDeposit_name(String deposit_name) {
    this.deposit_name = deposit_name;
  }

}
