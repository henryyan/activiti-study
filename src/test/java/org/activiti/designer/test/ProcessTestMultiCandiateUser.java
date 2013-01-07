package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiCandiateUser {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/MultiCandiateUser.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		assertEquals(1, taskService.createTaskQuery().taskCandidateUser("1").count());
		assertEquals(1, taskService.createTaskQuery().taskCandidateUser("2").count());
		assertEquals(1, taskService.createTaskQuery().taskCandidateUser("3").count());
		Task task = taskService.createTaskQuery().taskCandidateUser("1").list().get(0);
		taskService.claim(task.getId(), "1");
		assertEquals(1, taskService.createTaskQuery().taskAssignee("1").count());
		assertEquals(0, taskService.createTaskQuery().taskCandidateUser("1").count());
		assertEquals(0, taskService.createTaskQuery().taskCandidateUser("2").count());
		assertEquals(0, taskService.createTaskQuery().taskCandidateUser("3").count());
	}
}