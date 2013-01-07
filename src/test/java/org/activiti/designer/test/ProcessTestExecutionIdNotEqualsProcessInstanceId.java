package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestExecutionIdNotEqualsProcessInstanceId {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/ExecutionIdNotEqualsProcessInstanceId.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		variableMap.put("users", Arrays.asList("one", "two", "three"));
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		List<Execution> list = runtimeService.createExecutionQuery().processDefinitionKey("process1").list();
		for (Execution execution : list) {
			ExecutionEntity entity = (ExecutionEntity) execution;
			System.out.println("executionId=" + execution.getId() + "\tPID=" + execution.getProcessInstanceId() + "\t"
					+ entity.getProcessDefinitionId() + "\t execution-parnet_id=" + entity.getParentId());
		}

		List<ProcessInstance> list2 = runtimeService.createProcessInstanceQuery().processDefinitionKey("process1").list();
		System.out.println("processInstance.size=" + list2.size());

		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		System.out.println("tid=" + task.getId() + "\t eid=" + task.getExecutionId() + "\t pid=" + task.getProcessInstanceId());
		taskService.complete(task.getId());

		task = taskService.createTaskQuery().singleResult();
		System.out.println("tid=" + task.getId() + "\t eid=" + task.getExecutionId() + "\t pid=" + task.getProcessInstanceId());
		taskService.complete(task.getId());

		// one,two,three
		List<Task> list3 = taskService.createTaskQuery().list();
		assertEquals(3, list3.size());
		for (Task task2 : list3) {
			System.out.println(task2.getAssignee());
		}
/*		assertEquals("one", list3.get(0).getAssignee());
		assertEquals("two", list3.get(1).getAssignee());
		assertEquals("three", list3.get(2).getAssignee());
*/	}
}