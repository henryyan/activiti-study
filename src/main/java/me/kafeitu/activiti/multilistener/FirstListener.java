package me.kafeitu.activiti.multilistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class FirstListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

  @Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("first");
	}

}
