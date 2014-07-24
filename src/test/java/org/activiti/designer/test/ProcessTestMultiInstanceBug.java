package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

public class ProcessTestMultiInstanceBug {

  private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/bugs/MultiInstance.bpmn";

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  public void startProcess() throws Exception {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    repositoryService.createDeployment().addInputStream("MultiInstanceBug.bpmn20.xml", new FileInputStream(filename)).deploy();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("users", Arrays.asList(new Object[] {"user1", "user2", "user3"}));
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("MultiInstanceBug", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
    
    TaskService taskService = activitiRule.getTaskService();
    List<Task> list = taskService.createTaskQuery().list();
    
    taskService.complete(list.get(0).getId());
    taskService.complete(list.get(1).getId());
    taskService.complete(list.get(2).getId());
  }
}