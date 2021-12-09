package id.co.bspguard.android.bravo.accounts;

import java.util.List;

public class infoKerjaDataSet {
  private String regionName;
  private List<String> region, project, location, branch;

  public infoKerjaDataSet(String regionName, List<String> region, List<String> project, List<String> location, List<String> branch) {
    this.region = region;
    this.project = project;
    this.location = location;
    this.branch = branch;
    this.regionName = regionName;

  }

  public List<String> getRegion() {
    return region;
  }

  public void setRegion(List<String> region) {
    this.region = region;
  }

  public List<String> getProject() {
    return project;
  }

  public void setProject(List<String> project) {
    this.project = project;
  }

  public List<String> getLocation() {
    return location;
  }

  public void setLocation(List<String> location) {
    this.location = location;
  }

  public List<String> getBranch() {
    return branch;
  }

  public void setBranch(List<String> branch) {
    this.branch = branch;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }
}
