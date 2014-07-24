package me.kafeitu.activiti.executionlistenerintask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ExecutionListenerInTaskListenerForExecution implements
		ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("output in ExecutionListenerInTaskListenerForExecution");
	}

}
