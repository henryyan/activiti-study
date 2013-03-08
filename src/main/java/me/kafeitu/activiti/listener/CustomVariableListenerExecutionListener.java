package me.kafeitu.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 
 * @author HenryYan
 *
 */
public class CustomVariableListenerExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
	 * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ssssssssssssss");
	}

}
