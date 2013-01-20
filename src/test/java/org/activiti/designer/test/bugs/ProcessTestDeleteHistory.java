package org.activiti.designer.test.bugs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestDeleteHistory {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources = { "diagrams/EventGateway.bpmn", "diagrams/Gateway.bpmn" })
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		assertEquals(2, list.size());
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("EventGateway", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		variableMap = new HashMap<String, Object>();
		variableMap.put("userId", "henryyan");
		ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("gateway", variableMap);
		System.out.println("id " + processInstance2.getId() + " " + processInstance2.getProcessDefinitionId());
		
		HistoryService historyService = activitiRule.getHistoryService();
		long count = historyService.createHistoricVariableInstanceQuery().count();
		assertEquals(2, count);
		
		//historyService.deleteHistoricProcessInstance(processInstance2.getId());
		runtimeService.deleteProcessInstance(processInstance2.getId(), "test");
		count = historyService.createHistoricVariableInstanceQuery().count();
		assertEquals(2, count);
		System.out.println("==============");
		historyService.deleteHistoricProcessInstance(processInstance2.getId());
		count = historyService.createHistoricVariableInstanceQuery().count();
		assertEquals(1, count);
	}

}