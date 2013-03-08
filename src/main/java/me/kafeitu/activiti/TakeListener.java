package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class TakeListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

  @Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("ssssssss");
	}

}
