package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestGateway {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/main/resources/diagrams/Gateway.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("diagrams/Gateway.bpmn").deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("1");
		List<Task> list = taskAssignee.list();
		for (Task task : list) {
			System.out.println(ToStringBuilder.reflectionToString(task));
		}
		variableMap = new HashMap<String, Object>();
		variableMap.put("pass", false);
		taskService.complete(list.get(0).getId(), variableMap);
		System.out.println("A completed");
		taskAssignee = taskService.createTaskQuery().taskAssignee("1");
		list = taskAssignee.list();
		assertEquals(1, list.size());
		assertEquals("C", list.get(0).getName());
	}
	
	@Test
	public void findOutGateWary() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("1");
		List<Task> list = taskAssignee.list();
		assertEquals(1, list.size());
		Task task = list.get(0);
		System.out.println(task.getAssignee());
		
	}
	
}