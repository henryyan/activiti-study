package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestBilling {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/billing.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("billing.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
		taskService.claim(task.getId(), "aa");
		
		variableMap = new HashMap<String, Object>();
		variableMap.put("approved", true);
		taskService.complete(task.getId(), variableMap);
		
		HistoryService historyService = activitiRule.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}
	
	@Test
	public void startProcess11() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("billing.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
		taskService.claim(task.getId(), "aa");
		
		variableMap = new HashMap<String, Object>();
		variableMap.put("approved", false);
		variableMap.put("applyer", "bb");
		taskService.complete(task.getId(), variableMap);
		
		task = taskService.createTaskQuery().taskAssignee("bb").singleResult();
		variableMap = new HashMap<String, Object>();
		variableMap.put("reApply", false);
		taskService.complete(task.getId(), variableMap);
		
		HistoryService historyService = activitiRule.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}
	
	@Test
	public void startProcess22() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("billing.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("billing", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
		taskService.claim(task.getId(), "aa");
		
		variableMap = new HashMap<String, Object>();
		variableMap.put("approved", false);
		variableMap.put("applyer", "bb");
		taskService.complete(task.getId(), variableMap);
		
		task = taskService.createTaskQuery().taskAssignee("bb").singleResult();
		variableMap = new HashMap<String, Object>();
		variableMap.put("reApply", true);
		taskService.complete(task.getId(), variableMap);
		
		task = taskService.createTaskQuery().taskCandidateGroup("drawer").singleResult();
		assertNotNull(task);
	}
	
}