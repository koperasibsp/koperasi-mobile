package id.co.bspguard.android.bravo.memberloan;

public class ListLoanDataSet {
  String id, loan_number, value, start_date, end_date, period, in_period, approval, loan_name, logo;

  public ListLoanDataSet(){

  }

  public ListLoanDataSet(String id, String loan_number, String value, String start_date, String end_date, String period, String in_period, String approval, String loan_name, String logo) {
    this.id = id;
    this.loan_number = loan_number;
    this.value = value;
    this.start_date = start_date;
    this.end_date = end_date;
    this.period = period;
    this.in_period = in_period;
    this.approval = approval;
    this.loan_name = loan_name;
    this.logo = logo;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLoan_number() {
    return loan_number;
  }

  public void setLoan_number(String loan_number) {
    this.loan_number = loan_number;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getStart_date() {
    return start_date;
  }

  public void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  public String getEnd_date() {
    return end_date;
  }

  public void setEnd_date(String end_date) {
    this.end_date = end_date;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getIn_period() {
    return in_period;
  }

  public void setIn_period(String in_period) {
    this.in_period = in_period;
  }

  public String getApproval() {
    return approval;
  }

  public void setApproval(String approval) {
    this.approval = approval;
  }

  public String getLoan_name() {
    return loan_name;
  }

  public void setLoan_name(String loan_name) {
    this.loan_name = loan_name;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }
}

