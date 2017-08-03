package com.codenotfound.model;

public class Bar {

  private String bar;

  public Bar() {}

  public Bar(String bar) {
    this.bar = bar;
  }

  public String getBar() {
    return bar;
  }

  public void setBar(String bar) {
    this.bar = bar;
  }

  @Override
  public String toString() {
    return "Bar [bar=" + bar + "]";
  }
}
