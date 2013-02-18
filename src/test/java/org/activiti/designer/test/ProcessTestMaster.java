package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * 需求：一个业务对应多个流程实例
 * 在Activiti的流程表中业务ID是唯一的，有几个场景需要突破这种情况
 * 1、第一次运行流程最后流程取消了，但是要保留历史流程数据，并且要以同一个业务记录启动一个流程实例
 * 2、一个业务根据某种规则拆分多个独立的流程，例如多个申请单，每个申请单需要一个独立的流程处理
 * 
 * @author HenryYan
 */
public class ProcessTestMaster {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/qun/oneBusinessToManyProcess/master.bpmn";
	private String filename1 = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/qun/oneBusinessToManyProcess/business.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		TaskService taskService = activitiRule.getTaskService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		RepositoryService repositoryService = activitiRule.getRepositoryService();

		// 部署两个流程
		repositoryService.createDeployment().addInputStream("master.bpmn20.xml", new FileInputStream(filename)).deploy();
		repositoryService.createDeployment().addInputStream("business.bpmn20.xml", new FileInputStream(filename1)).deploy();

		// 准备变量
		Map<String, Object> variableMap = new HashMap<String, Object>();
		List<String> users = new ArrayList<String>();
		users.add("user1");
		users.add("user2");
		users.add("user3");
		variableMap.put("users", users);

		// 启动时用123456作为业务ID
		String businessKey = "123456";
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("master", businessKey, variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		assertNotNull(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult());

		// 根据主流程的流程实例ID查询子流程（调用活动的多个实例，每一个都是一个独立的流程）
		List<ProcessInstance> subProcessInstances = runtimeService.createProcessInstanceQuery()
				.superProcessInstanceId(processInstance.getProcessInstanceId()).list();
		assertEquals(3, subProcessInstances.size());
		for (ProcessInstance subProcessInstance : subProcessInstances) {
			assertNull(subProcessInstance.getBusinessKey());
		}

		// 验证用户列表中的用户是否都有一个待办任务
		for (String user : users) {
			long count = taskService.createTaskQuery().taskAssignee(user).count();
			assertEquals(1, count);
		}

	}
}