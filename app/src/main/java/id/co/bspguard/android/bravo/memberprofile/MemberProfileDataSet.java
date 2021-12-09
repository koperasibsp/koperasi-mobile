package id.co.bspguard.android.bravo.memberprofile;

public class MemberProfileDataSet {
  String nik, nik_koperasi, nik_bsp, name, posisi, proyek, area, bergabung, kontrak_awal, kontrak_akhir, status, email, alamat, no_telp;

  public MemberProfileDataSet(){

  }

  public MemberProfileDataSet(String nik, String nik_koperasi, String nik_bsp, String name, String posisi, String proyek, String area, String bergabung, String kontrak_awal, String kontrak_akhir, String status, String email, String alamat, String no_telp) {
    this.nik = nik;
    this.nik_koperasi = nik_koperasi;
    this.nik_bsp = nik_bsp;
    this.name = name;
    this.posisi = posisi;
    this.proyek = proyek;
    this.area = area;
    this.bergabung = bergabung;
    this.kontrak_awal = kontrak_awal;
    this.kontrak_akhir = kontrak_akhir;
    this.status = status;
    this.email = email;
    this.alamat = alamat;
    this.no_telp = no_telp;
  }

  public String getNik() {
    return nik;
  }

  public void setNik(String nik) {
    this.nik = nik;
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

  public String getPosisi() {
    return posisi;
  }

  public void setPosisi(String posisi) {
    this.posisi = posisi;
  }

  public String getProyek() {
    return proyek;
  }

  public void setProyek(String proyek) {
    this.proyek = proyek;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getBergabung() {
    return bergabung;
  }

  public void setBergabung(String bergabung) {
    this.bergabung = bergabung;
  }

  public String getKontrak_awal() {
    return kontrak_awal;
  }

  public void setKontrak_awal(String kontrak_awal) {
    this.kontrak_awal = kontrak_awal;
  }

  public String getKontrak_akhir() {
    return kontrak_akhir;
  }

  public void setKontrak_akhir(String kontrak_akhir) {
    this.kontrak_akhir = kontrak_akhir;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAlamat() {
    return alamat;
  }

  public void setAlamat(String alamat) {
    this.alamat = alamat;
  }

  public String getNo_telp() {
    return no_telp;
  }

  public void setNo_telp(String no_telp) {
    this.no_telp = no_telp;
  }
}
