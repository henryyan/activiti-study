package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiInstance {

  private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/MultiInstance.bpmn";

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  public void startProcess() throws Exception {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    repositoryService.createDeployment().addInputStream("process1.bpmn20.xml", new FileInputStream(filename)).deploy();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
    TaskService taskService = activitiRule.getTaskService();
    assertEquals(3, taskService.createTaskQuery().taskAssignee("henryyan").count());
    Task task = taskService.createTaskQuery().taskAssignee("henryyan").listPage(0, 1).get(0);

    ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
            .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
    List<ActivityImpl> activitiList = processDefinition.getActivities();

    for (ActivityImpl activity : activitiList) {
      ActivityBehavior activityBehavior = activity.getActivityBehavior();
      boolean multiinstance = false;
      if (activityBehavior instanceof MultiInstanceActivityBehavior) {
        multiinstance = true;
      }
      System.out.println(String.format("activiti id: %s, behavior type: %s, multiinstance: %b", activity.getId(), activityBehavior, multiinstance));
    }

    taskService.complete(task.getId());

    task = taskService.createTaskQuery().taskAssignee("henryyan").listPage(0, 1).get(0);
    taskService.complete(task.getId());
  }
}