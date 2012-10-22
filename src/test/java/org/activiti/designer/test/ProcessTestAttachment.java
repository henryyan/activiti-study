package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestAttachment {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = { "diagrams/Attachment.bpmn" })
  public void startProcess() throws Exception {
    TaskService taskService = activitiRule.getTaskService();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    Task singleResult = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
    String url = "http://labs.mop.com/apache-mirror//ant/binaries/apache-ant-1.8.3-bin.zip";
    String attachmentDescription = "ant bin package";
    taskService.createAttachment("zip", singleResult.getId(), processInstance.getId(), "apache-ant-1.8.3-bin.zip", attachmentDescription, url);
    taskService.complete(singleResult.getId());

    List<Attachment> taskAttachments = taskService.getTaskAttachments(singleResult.getId());
    assertEquals(1, taskAttachments.size());

    HistoryService historyService = activitiRule.getHistoryService();
    List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
    assertEquals(false, list.isEmpty());
  }

  @Test
  @Deployment(resources = { "diagrams/Attachment.bpmn" })
  public void testCandidateUsers() throws Exception {
    TaskService taskService = activitiRule.getTaskService();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    assertNotNull(taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult());
    assertNotNull(taskService.createTaskQuery().taskCandidateUser("kafeitu").singleResult());

    Task singleResult = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
    taskService.complete(singleResult.getId());

    assertNull(taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult());
    assertNull(taskService.createTaskQuery().taskCandidateUser("kafeitu").singleResult());
  }

  @Test
  @Deployment(resources = { "diagrams/Attachment.bpmn" })
  public void testCandidateUsersAddUserRuntime() throws Exception {
    TaskService taskService = activitiRule.getTaskService();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    assertNotNull(taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult());
    assertNotNull(taskService.createTaskQuery().taskCandidateUser("kafeitu").singleResult());

    String taskId = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult().getId();
    taskService.addCandidateUser(taskId, "runtimeUser");

    assertNotNull(taskService.createTaskQuery().taskCandidateUser("runtimeUser").singleResult());

    Task singleResult = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
    taskService.complete(singleResult.getId());

    assertNull(taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult());
    assertNull(taskService.createTaskQuery().taskCandidateUser("kafeitu").singleResult());
  }

}