package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestListenerAndDelegate {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/listenerAndDelegate.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		TaskService taskService = activitiRule.getTaskService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		ProcessInstance processInstance = null;
		try {
			processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		} catch (RuntimeException e) {
			System.err.println("cache runtimeexception");
			return;
		}
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("henryyan");
		System.out.println(ToStringBuilder.reflectionToString(taskAssignee));
	}
}