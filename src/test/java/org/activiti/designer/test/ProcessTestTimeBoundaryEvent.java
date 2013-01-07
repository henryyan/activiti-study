package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class ProcessTestTimeBoundaryEvent {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/TimeBoundaryEvent.bpmn";

	public void startProcessWithJob() throws Exception {
		ProcessEngineConfiguration createStandaloneInMemProcessEngineConfiguration = StandaloneInMemProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		createStandaloneInMemProcessEngineConfiguration.setJobExecutorActivate(true);
		ProcessEngine processEngine = createStandaloneInMemProcessEngineConfiguration.buildProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addInputStream("TimeBoundaryEvent.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.SECOND, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
		variableMap.put("datetime", sdf.format(ca.getTime()) + "T" + sdft.format(ca.getTime()));
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TimeBoundaryEvent", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		assertNotNull(task);
		
		taskService.complete(task.getId());
		Thread.sleep(2000);
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception {
		new ProcessTestTimeBoundaryEvent().startProcessWithJob();
	}
}