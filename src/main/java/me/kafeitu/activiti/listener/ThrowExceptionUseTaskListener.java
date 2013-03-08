package me.kafeitu.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ThrowExceptionUseTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

  @Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("我来了，故意抛出异常");
		throw new RuntimeException("我就是故意的");
	}

}
