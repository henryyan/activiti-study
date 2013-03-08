package me.kafeitu.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 *
 * @author HenryYan
 */
public class CreateUserTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("CreateUserTaskListener" + ToStringBuilder.reflectionToString(delegateTask));
	}

}
