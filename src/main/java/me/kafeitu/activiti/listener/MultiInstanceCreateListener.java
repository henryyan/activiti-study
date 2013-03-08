package me.kafeitu.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MultiInstanceCreateListener implements TaskListener {

	private static final long serialVersionUID = 1L;

  @Override
	public void notify(DelegateTask delegateTask) {
		System.out.println(String.format("nrOfInstances=%s", delegateTask.getVariable("nrOfInstances")));
		System.out.println(String.format("nrOfActiveInstances=%s", delegateTask.getVariable("nrOfActiveInstances")));
		System.out.println(String.format("nrOfCompletedInstances=%s", delegateTask.getVariable("nrOfCompletedInstances")));
		System.out.println(String.format("loopCounter=%s", delegateTask.getVariable("loopCounter")));
		System.out.println("-------------");
	}

}
