package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestLeaveqjsq {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/qun/qjsq.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("leave-qjsq.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("user", null);
		variableMap.put("users", Arrays.asList("user2", "user3"));
		variableMap.put("userGroup", new ArrayList<String>());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-qjsq", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		assertEquals(1, taskService.createTaskQuery().count());
		
		List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskService.createTaskQuery().singleResult().getId());
		assertEquals(2, identityLinksForTask.size());
	}
}