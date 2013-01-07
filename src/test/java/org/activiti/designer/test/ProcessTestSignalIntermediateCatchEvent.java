package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestSignalIntermediateCatchEvent {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/SignalIntermediateCatchEvent.bpmn";
	private String filename1 = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/SignalIntermediateCatchEvent1.bpmn";

	private String filenameForThrow = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/SignalIntermediateThrowEvent.bpmn";
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("SignalIntermediateCatchEvent.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		
		repositoryService.createDeployment().addInputStream("SignalIntermediateCatchEvent1.bpmn20.xml",
				new FileInputStream(filename1)).deploy();
		
		repositoryService.createDeployment().addInputStream("filenameForThrow.bpmn20.xml",
				new FileInputStream(filenameForThrow)).deploy();
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SignalIntermediateCatchEvent", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		ProcessInstance processInstance1 = runtimeService.startProcessInstanceByKey("SignalIntermediateCatchEvent1", variableMap);
		assertNotNull(processInstance1.getId());
		System.out.println("id " + processInstance1.getId() + " "
				+ processInstance1.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		// 验证有两个用户任务
		List<Task> list = taskService.createTaskQuery().list();
		assertEquals(2, list.size());
		
		// 完成两个用户任务
		taskService.complete(list.get(0).getId());
		taskService.complete(list.get(1).getId());
		
		// 验证有两个正常运行的实例
		List<HistoricProcessInstance> list2 = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().unfinished().list();
		assertEquals(2, list2.size());
		
		// 启动信号抛出事件的流程
		ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("SignalIntermediateThrowEvent", variableMap);
		assertNotNull(processInstance2.getId());
		System.out.println("id " + processInstance2.getId() + " "
				+ processInstance2.getProcessDefinitionId());
		
		list2 = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().unfinished().list();
		assertEquals(0, list2.size());
	}
}