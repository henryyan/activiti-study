package com.sample;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DoCheck implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < 1000; i++) {
			System.out.println("处理第 " + i + " 条消息。。。");
			Thread.sleep(500);
		}
	}

}
