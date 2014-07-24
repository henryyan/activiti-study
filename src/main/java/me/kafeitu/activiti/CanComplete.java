package me.kafeitu.activiti;

import org.activiti.engine.runtime.Execution;


public class CanComplete {

  public boolean can(int a) {
    System.out.println("判断是否可以完成");
    return a == 3;
  }
  
}
