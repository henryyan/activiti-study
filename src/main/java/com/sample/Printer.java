package com.sample;

import java.io.Serializable;

public class Printer implements Serializable {

  private static final long serialVersionUID = 1L;
  
  public void print() {
    System.out.println("no special output");
  }
  
  public void print(String myVar) {
    System.out.println("special output, myVar=" + myVar);
  }

}
