package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;


public class MultiInstanceCompleteListener implements TaskListener {

  @Override
  public void notify(DelegateTask delegateTask) {
    System.out.println("任务完成" + delegateTask.getId());
  }

}
