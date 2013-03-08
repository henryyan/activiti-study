package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 
 *
 * @author HenryYan
 */
public class ThrowExceptionOnCreate implements TaskListener {

	private static final long serialVersionUID = 1L;

  @Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("啊哈哈");
		throw new RuntimeException("故意的");
	}

}
