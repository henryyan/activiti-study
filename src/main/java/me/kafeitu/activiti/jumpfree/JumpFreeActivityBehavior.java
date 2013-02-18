package me.kafeitu.activiti.jumpfree;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;


/**
 * @author henryyan
 */
public class JumpFreeActivityBehavior implements ActivityBehavior {

  @Override
  public void execute(ActivityExecution execution) throws Exception {
    PvmTransition transition = execution.getActivity().findOutgoingTransition("flow1");
    execution.take(transition);
  }

}
