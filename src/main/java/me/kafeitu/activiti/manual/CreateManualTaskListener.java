package me.kafeitu.activiti.manual;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 
 *
 * @author HenryYan
 */
public class CreateManualTaskListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("on " + execution.getEventName() + " " + execution.getProcessInstanceId());
	}


}
