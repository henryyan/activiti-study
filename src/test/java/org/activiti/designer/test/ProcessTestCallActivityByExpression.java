package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ProcessTestCallActivityByExpression {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/CallActivityByExpression.bpmn";
	private String filename1 = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/Gateway.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("CallActivityByExpression.bpmn20.xml", new FileInputStream(filename))
		.addInputStream("gateway.bpmn20.xml", new FileInputStream(filename1))
				.deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("activityName", "gateway");
		
		IdentityService identityService = activitiRule.getIdentityService();
		identityService.setAuthenticatedUserId("henryyan");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("CallActivityByExpression", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		Task singleResult = taskService.createTaskQuery().singleResult();
		identityService.setAuthenticatedUserId("henryyan");
		taskService.complete(singleResult.getId());
		
		long count = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().count();
		assertEquals(2, count);
		
		List<HistoricProcessInstance> list = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			assertEquals("henryyan", historicProcessInstance.getStartUserId());
		}
	}
}