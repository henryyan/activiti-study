package me.kafeitu.activiti.multilistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class SecondListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("second");
	}

}
