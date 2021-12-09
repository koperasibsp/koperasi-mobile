package id.co.bspguard.android.bravo.memberdeposit;

public class ListDetailDepositDataSet {

  String id, deposit_number, type, deposit_type, status, value, cut_date, desc;

  public ListDetailDepositDataSet(){

  }

  public ListDetailDepositDataSet(String id, String deposit_number, String type, String deposit_type, String status, String value, String cut_date, String desc) {
    this.id = id;
    this.deposit_number = deposit_number;
    this.type = type;
    this.deposit_type = deposit_type;
    this.status = status;
    this.value = value;
    this.cut_date = cut_date;
    this.desc = desc;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeposit_number() {
    return deposit_number;
  }

  public void setDeposit_number(String deposit_number) {
    this.deposit_number = deposit_number;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDeposit_type() {
    return deposit_type;
  }

  public void setDeposit_type(String deposit_type) {
    this.deposit_type = deposit_type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCut_date() {
    return cut_date;
  }

  public void setCut_date(String cut_date) {
    this.cut_date = cut_date;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
