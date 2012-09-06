package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestAutoClaimForReject {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/main/resources/diagrams/AutoClaimForReject.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("AutoClaimForReject.bpmn20.xml", new FileInputStream(filename))
				.deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("AutoClaimForReject", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("role1").singleResult();
		assertEquals("分配给角色", task.getName());
		taskService.complete(task.getId());
		
		// 驳回请求
		task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
		assertEquals("处理后驳回", task.getName());
		Map<String, Object> vairables = new HashMap<String, Object>();
		vairables.put("reject", true);
		taskService.complete(task.getId(), vairables);
		
		// 再次获取节点-分配给角色
		task = taskService.createTaskQuery().taskCandidateGroup("role1").singleResult();
		assertEquals("分配给角色", task.getName());
		
		
	}
}