package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class TakeListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("ssssssss");
	}

}
