package me.kafeitu.activiti.listener;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 
 *
 * @author HenryYan
 */
public class JavaServiceTaskDelegate implements JavaDelegate, Serializable {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("JavaServiceTaskDelegate=" + execution.getProcessInstanceId());
		System.out.println("returnedValue=" + execution.getVariable("returnedValue"));
	}

}
