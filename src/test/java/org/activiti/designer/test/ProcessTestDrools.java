package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import com.sample.DroolsTest.Message;

public class ProcessTestDrools {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/drools.bpmn";
	private String ruleFile = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/rules/hello.drl";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule
				.getRepositoryService();
		repositoryService
				.createDeployment()
				.addInputStream("drools.bpmn20.xml",
						new FileInputStream(filename))
				.addInputStream("hello.drl", new FileInputStream(ruleFile))
				.deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();

		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);

		variableMap.put("m", message);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("drools", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());

		/*
		 // 从运行中查询
		 Object variable = runtimeService.getVariable(processInstance.getId(),
				"rulesOutput");
		assertNotNull(variable);
		List<Object> varList = (List<Object>) variable;
		assertEquals(1, varList.size());
		Message ruledMessage = (Message) varList.get(0);
		assertEquals(1, ruledMessage.getStatus());*/

		HistoryService historyService = activitiRule.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery()
				.finished().count();
		assert count == 1;

		// 从history查询变量
		List<HistoricDetail> list = historyService.createHistoricDetailQuery()
				.processInstanceId(processInstance.getId()).list();
		for (HistoricDetail historicDetail : list) {
			HistoricVariableUpdate variableDetail = (HistoricVariableUpdate) historicDetail;
			System.out.println(variableDetail.getVariableName() + " = "
					+ variableDetail.getValue());
			if (variableDetail.getVariableName().equals("rulesOutput")) {
				@SuppressWarnings("unchecked")
				List<Object> varList = (List<Object>) variableDetail.getValue();
				assertEquals(1, varList.size());
				Message ruledMessage = (Message) varList.get(0);
				assertEquals(1, ruledMessage.getStatus());
			}
		}
	}
}