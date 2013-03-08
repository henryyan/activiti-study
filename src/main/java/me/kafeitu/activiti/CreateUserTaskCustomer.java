package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class CreateUserTaskCustomer implements ExecutionListener {

	private static final long serialVersionUID = 1L;

  public void hello(String eventName) {
		System.out.println("hello " + eventName);
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("notify " + execution.getEventName());
	}
	
}
