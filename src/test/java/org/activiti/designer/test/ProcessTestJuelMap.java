package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestJuelMap {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/qun/juelMap.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("juelMap.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
//		ArrayList<String> value = new ArrayList<String>();
//		value.add("3333");
		Map<String, List<String>> names = new HashMap<String, List<String>>();
		names.put("henry", Arrays.asList("Henry Yan", "咖啡兔"));
		variableMap.put("names", names);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("juelMap", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		HistoryService historyService = activitiRule.getHistoryService();
		System.out.println(historyService.createHistoricVariableInstanceQuery().variableName("a").singleResult().getValue()); ;
	}
}