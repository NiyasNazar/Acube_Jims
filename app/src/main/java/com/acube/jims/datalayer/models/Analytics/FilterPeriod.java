package com.acube.jims.datalayer.models.Analytics;

public class FilterPeriod {
  private   int Id;
  private String value;
  public FilterPeriod(int Id,String value){
      this.Id=Id;
      this.value=value;
  }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return value;
    }
}
