package com.sample;

import org.activiti.engine.EngineServices;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class AutoListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		EngineServices engineServices = execution.getEngineServices();
		RuntimeService runtimeService = engineServices.getRuntimeService();
		runtimeService.signal(execution.getId());
	}

}
