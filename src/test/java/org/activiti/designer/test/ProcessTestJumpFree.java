package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestJumpFree {

  private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/JumpFee.bpmn";

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  public void startProcess() throws Exception {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    repositoryService.createDeployment().addInputStream("JumpFree.bpmn20.xml", new FileInputStream(filename)).deploy();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("JumpFree", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
    
    TaskService taskService = activitiRule.getTaskService();
    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull(task);
    assertEquals("usertask1", task.getTaskDefinitionKey());
  }
}