package id.co.bspguard.android.bravo.memberloan;

public class RincianSimpananDataSet {
  String wajib, pokok, sukarela, lainnya;
  public RincianSimpananDataSet(){}

  public RincianSimpananDataSet(String wajib, String pokok, String sukarela, String lainnya) {
    this.wajib = wajib;
    this.pokok = pokok;
    this.sukarela = sukarela;
    this.lainnya = lainnya;
  }

  public String getWajib() {
    return wajib;
  }

  public void setWajib(String wajib) {
    this.wajib = wajib;
  }

  public String getPokok() {
    return pokok;
  }

  public void setPokok(String pokok) {
    this.pokok = pokok;
  }

  public String getSukarela() {
    return sukarela;
  }

  public void setSukarela(String sukarela) {
    this.sukarela = sukarela;
  }

  public String getLainnya() {
    return lainnya;
  }

  public void setLainnya(String lainnya) {
    this.lainnya = lainnya;
  }
}

