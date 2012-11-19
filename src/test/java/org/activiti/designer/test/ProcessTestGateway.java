package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestGateway {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = { "diagrams/Gateway.bpmn" })
  public void startProcess() throws Exception {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("gateway", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    TaskService taskService = activitiRule.getTaskService();
    TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("1");
    List<Task> list = taskAssignee.list();
    for (Task task : list) {
      System.out.println(ToStringBuilder.reflectionToString(task));
    }
    variableMap = new HashMap<String, Object>();
    variableMap.put("pass", false);
    taskService.complete(list.get(0).getId(), variableMap);
    System.out.println("A completed");
    taskAssignee = taskService.createTaskQuery().taskAssignee("1");
    list = taskAssignee.list();
    assertEquals(1, list.size());
    assertEquals("C", list.get(0).getName());
  }

  @Test
  @Deployment(resources = { "diagrams/Gateway.bpmn" })
  public void findOutGateWary() throws Exception {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("gateway", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    TaskService taskService = activitiRule.getTaskService();
    TaskQuery taskAssignee = taskService.createTaskQuery().taskAssignee("1");
    List<Task> list = taskAssignee.list();
    assertEquals(1, list.size());
    Task task = list.get(0);
    System.out.println(task.getAssignee());

  }

}