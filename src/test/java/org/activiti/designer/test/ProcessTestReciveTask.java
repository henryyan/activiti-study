package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestReciveTask {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/ReciveTask.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("ReciveTask.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ReciveTask", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().list();
		assertEquals(0, list.size());
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		// 读取所有的activity
		List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery().executionId(processInstance.getId()).list();
		for (HistoricActivityInstance historicActivityInstance : activityList) {
			System.out.println("task of :" + historicActivityInstance.getActivityName() + "\t" + historicActivityInstance.getActivityType());
		}
		
		// 只读取receive task
		activityList = historyService.createHistoricActivityInstanceQuery().activityType("receiveTask").executionId(processInstance.getId()).list();
		assertEquals(1, activityList.size());
		
		// 触发receive task
		System.out.println("begin invoke receive task...");
		runtimeService.signal(processInstance.getId());
		
		// 验证是否已经结束
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}
	
}