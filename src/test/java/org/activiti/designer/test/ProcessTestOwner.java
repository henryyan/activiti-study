package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestOwner {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/Attachment.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		IdentityService identityService = activitiRule.getIdentityService();
		identityService.setAuthenticatedUserId("henryyan");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		TaskService taskService = activitiRule.getTaskService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricProcessInstance> list2 = historyService.createHistoricProcessInstanceQuery().startedBy("henryyan").list();
		assertEquals(false, list2.isEmpty());

		Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
		taskService.complete(task.getId());

		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
		assertEquals(false, list.isEmpty());

		list2 = historyService.createHistoricProcessInstanceQuery().startedBy("henryyan").list();
		assertEquals(false, list2.isEmpty());
	}
}