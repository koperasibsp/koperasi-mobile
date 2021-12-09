package id.co.bspguard.android.bravo.loan;

public class ApprovalLoanDataSet {
  String id, loan_name, is_reject, is_approve, is_revision, is_waiting, status, loan_number, value, period, start_date, end_date, approval, nik_koperasi, nik_bsp, name;

  public ApprovalLoanDataSet(){}

  public ApprovalLoanDataSet(String id, String loan_name, String is_reject, String is_approve, String is_revision, String is_waiting, String status, String loan_number, String value, String period, String start_date, String end_date, String approval, String nik_koperasi, String nik_bsp, String name) {
    this.id = id;
    this.loan_name = loan_name;
    this.is_reject = is_reject;
    this.is_approve = is_approve;
    this.is_revision = is_revision;
    this.is_waiting = is_waiting;
    this.status = status;
    this.loan_number = loan_number;
    this.value = value;
    this.period = period;
    this.start_date = start_date;
    this.end_date = end_date;
    this.approval = approval;
    this.nik_koperasi = nik_koperasi;
    this.nik_bsp = nik_bsp;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLoan_name() {
    return loan_name;
  }

  public void setLoan_name(String loan_name) {
    this.loan_name = loan_name;
  }

  public String getIs_reject() {
    return is_reject;
  }

  public void setIs_reject(String is_reject) {
    this.is_reject = is_reject;
  }

  public String getIs_approve() {
    return is_approve;
  }

  public void setIs_approve(String is_approve) {
    this.is_approve = is_approve;
  }

  public String getIs_revision() {
    return is_revision;
  }

  public void setIs_revision(String is_revision) {
    this.is_revision = is_revision;
  }

  public String getIs_waiting() {
    return is_waiting;
  }

  public void setIs_waiting(String is_waiting) {
    this.is_waiting = is_waiting;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
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

  public String getApproval() {
    return approval;
  }

  public void setApproval(String approval) {
    this.approval = approval;
  }

  public String getNik_koperasi() {
    return nik_koperasi;
  }

  public void setNik_koperasi(String nik_koperasi) {
    this.nik_koperasi = nik_koperasi;
  }

  public String getNik_bsp() {
    return nik_bsp;
  }

  public void setNik_bsp(String nik_bsp) {
    this.nik_bsp = nik_bsp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
