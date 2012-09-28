package me.kafeitu.activiti;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ErrorBoundaryEventAttachToTaskService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO throw error for error boundary event
		throw new BpmnError("errorOne");
	}

}
