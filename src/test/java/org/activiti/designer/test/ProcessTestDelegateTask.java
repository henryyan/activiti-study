package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

/**
 * 任务委派
 * 
 * @author henryyan
 */
public class ProcessTestDelegateTask {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources = { "diagrams/AutoAssignee.bpmn" })
	public void startProcess() throws Exception {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("AutoAssignee", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		TaskService taskService = activitiRule.getTaskService();

		Task task = taskService.createTaskQuery().singleResult();
		assertNull(task.getAssignee());

		// 签收
		taskService.claim(task.getId(), "user1");
		task = taskService.createTaskQuery().singleResult();
		assertNull(task.getOwner());
		assertNotNull(task.getAssignee());

		// 委派
		taskService.delegateTask(task.getId(), "henryyan");
		
		task = taskService.createTaskQuery().singleResult();
		assertEquals("user1", task.getOwner());

		// 查询被委派的任务
		task = taskService.createTaskQuery().taskAssignee("henryyan").taskDelegationState(DelegationState.PENDING).singleResult();
		assertNotNull(task);

		// 被委派人完成任务
		taskService.resolveTask(task.getId());
		
		// 查询已完成的委派任务
		task = taskService.createTaskQuery().taskDelegationState(DelegationState.RESOLVED).singleResult();
		assertNotNull(task);
		assertEquals("user1", task.getAssignee());
		assertEquals("user1", task.getOwner());
		//		taskService.complete(task.getId(), Collections.singletonMap("taskTwoAssignee", "user2"));
	}
}