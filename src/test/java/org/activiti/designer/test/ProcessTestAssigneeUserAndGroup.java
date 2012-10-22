package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestAssigneeUserAndGroup {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = {"diagrams/AssigneeUserAndGroup.bpmn"})
  public void startProcess() throws Exception {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "process1", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " "
            + processInstance.getProcessDefinitionId());
    
    TaskService taskService = activitiRule.getTaskService();
    long count = taskService.createTaskQuery().taskCandidateUser("kafeitu").count();
    assertEquals(1, count);
    
    count = taskService.createTaskQuery().taskCandidateUser("henryyan").count();
    assertEquals(1, count);
    
    count = taskService.createTaskQuery().taskCandidateGroup("group1").count();
    assertEquals(1, count);
    
    count = taskService.createTaskQuery().taskCandidateGroup("group2").count();
    assertEquals(1, count);
  }
}