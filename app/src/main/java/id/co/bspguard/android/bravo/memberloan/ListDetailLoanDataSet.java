package id.co.bspguard.android.bravo.memberloan;

public class ListDetailLoanDataSet {
  String id, value, service, pay_date, approval, in_period, period, sisa_pinjaman;

  public ListDetailLoanDataSet() {

  }

  public ListDetailLoanDataSet(String id, String value, String service, String pay_date, String approval, String in_period, String period, String sisa_pinjaman) {
    this.id = id;
    this.value = value;
    this.service = service;
    this.pay_date = pay_date;
    this.approval = approval;
    this.in_period = in_period;
    this.period = period;
    this.sisa_pinjaman = sisa_pinjaman;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getPay_date() {
    return pay_date;
  }

  public void setPay_date(String pay_date) {
    this.pay_date = pay_date;
  }

  public String getApproval() {
    return approval;
  }

  public void setApproval(String approval) {
    this.approval = approval;
  }

  public String getIn_period() {
    return in_period;
  }

  public void setIn_period(String in_period) {
    this.in_period = in_period;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getSisa_pinjaman() {
    return sisa_pinjaman;
  }

  public void setSisa_pinjaman(String sisa_pinjaman) {
    this.sisa_pinjaman = sisa_pinjaman;
  }
}
