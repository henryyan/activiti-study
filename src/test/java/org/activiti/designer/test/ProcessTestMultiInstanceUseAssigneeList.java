package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiInstanceUseAssigneeList {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/MultiInstanceUseAssigneeList.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment()
				.addInputStream("MultiInstanceUseAssigneeList.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		variableMap.put("assigneeList", Arrays.asList("user1", "user2", "user3"));
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("MultiInstanceUseAssigneeList", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		TaskService taskService = activitiRule.getTaskService();
		long count = taskService.createTaskQuery().count();
		assertEquals(count, 3);
		for (int i = 0; i < 3; i++) {
			long count2 = taskService.createTaskQuery().taskAssignee("user" + (i + 1)).count();
			assertEquals(count2, 1);
		}
	}
}