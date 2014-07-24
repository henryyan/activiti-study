package me.kafeitu.activiti.executionlistenerintask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ExecutionListenerInTaskListenerForTask implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("output in ExecutionListenerInTaskListenerForTask");
	}

}
