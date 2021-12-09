package id.co.bspguard.android.bravo.home;

public class TopLoanDataSet {

  String id, loan_name, rate_of_interest, description, tenor, logo, plafon;
  int biaya_admin, batas_pinjaman, biaya_transfer;
  double bunga, bunga_berjalan, provisi;

  public TopLoanDataSet(){

  }

  public TopLoanDataSet(String id, String loan_name, String rate_of_interest, String description, String tenor, String logo, String plafon, int biaya_admin, int batas_pinjaman, int biaya_transfer, double bunga, double bunga_berjalan, double provisi) {
    this.id = id;
    this.loan_name = loan_name;
    this.rate_of_interest = rate_of_interest;
    this.description = description;
    this.tenor = tenor;
    this.logo = logo;
    this.plafon = plafon;
    this.biaya_admin = biaya_admin;
    this.batas_pinjaman = batas_pinjaman;
    this.biaya_transfer = biaya_transfer;
    this.bunga = bunga;
    this.bunga_berjalan = bunga_berjalan;
    this.provisi = provisi;
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

  public String getRate_of_interest() {
    return rate_of_interest;
  }

  public void setRate_of_interest(String rate_of_interest) {
    this.rate_of_interest = rate_of_interest;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTenor() {
    return tenor;
  }

  public void setTenor(String tenor) {
    this.tenor = tenor;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getPlafon() {
    return plafon;
  }

  public void setPlafon(String plafon) {
    this.plafon = plafon;
  }

  public int getBiaya_admin() {
    return biaya_admin;
  }

  public void setBiaya_admin(int biaya_admin) {
    this.biaya_admin = biaya_admin;
  }

  public int getBatas_pinjaman() {
    return batas_pinjaman;
  }

  public void setBatas_pinjaman(int batas_pinjaman) {
    this.batas_pinjaman = batas_pinjaman;
  }

  public int getBiaya_transfer() {
    return biaya_transfer;
  }

  public void setBiaya_transfer(int biaya_transfer) {
    this.biaya_transfer = biaya_transfer;
  }

  public double getBunga() {
    return bunga;
  }

  public void setBunga(double bunga) {
    this.bunga = bunga;
  }

  public double getBunga_berjalan() {
    return bunga_berjalan;
  }

  public void setBunga_berjalan(double bunga_berjalan) {
    this.bunga_berjalan = bunga_berjalan;
  }

  public double getProvisi() {
    return provisi;
  }

  public void setProvisi(double provisi) {
    this.provisi = provisi;
  }
}
