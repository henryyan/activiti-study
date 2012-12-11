package me.kafeitu.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DeleteInstanceEndListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println(execution.getEventName());
		System.out.println(ToStringBuilder.reflectionToString(execution));
	}

}
