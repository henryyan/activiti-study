package me.kafeitu.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.springframework.stereotype.Component;

@Component
public class GetFieldOfTaskListener implements TaskListener {
  
  private static final long serialVersionUID = 1L;
  private Expression name;

  @Override
  public void notify(DelegateTask delegateTask) {
    System.out.println(name.getValue(delegateTask));
  }

}
