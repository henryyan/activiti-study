package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;

/**
 * 请求被驳回时自动把任务分配至上次办理人
 * 
 * @author henryyan
 */
public class ProcessTestAutoAssignee {

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
    Map<String, Object> vars = new HashMap<String, Object>();

    // 签收并完成第一个任务
    Task task = taskService.createTaskQuery().singleResult();
    taskService.claim(task.getId(), "user1");
    vars.put("taskTwoAssignee", "user2");
    taskService.complete(task.getId(), vars);

    // 完成第二个任务
    Task task2 = taskService.createTaskQuery().taskAssignee("user2").singleResult();
    vars = new HashMap<String, Object>();
    vars.put("pass", false);
    taskService.complete(task2.getId(), vars);

    // 验证任务回到第一个节点
    Task taskOne = taskService.createTaskQuery().taskName("Task One").singleResult();
    assertNotNull(taskOne);

    // 自动签收
    autoClaim(processInstance.getId());

    // 验证是否已自动签收
    taskOne = taskService.createTaskQuery().taskName("Task One").taskAssignee("user1").singleResult();
    assertNotNull(taskOne);
  }

  /**
   * 自动签收分配到候选角色、候选组的任务
   * 
   * @param executionId
   *          执行ID
   */
  public void autoClaim(String executionId) {

    TaskService taskService = activitiRule.getTaskService();
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    HistoryService historyService = activitiRule.getHistoryService();

    // 查询最新的任务
    List<Task> newTasks = taskService.createTaskQuery().executionId(executionId).list();
    for (Task task : newTasks) {

      // 查询并验证任务是否已经分配到具体人
      if (task.getAssignee() != null) {
        return;
      }

      // 验证任务是分配到组
      ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(task
              .getProcessDefinitionId());

      // 获得当前任务的所有节点
      List<ActivityImpl> activitiList = processDefinition.getActivities();

      for (ActivityImpl activity : activitiList) {
        // 当前节点
        if (task.getTaskDefinitionKey().equals(activity.getId())) {
          ActivityBehavior activityBehavior = activity.getActivityBehavior();

          // 匹配用户任务
          if (activityBehavior instanceof UserTaskActivityBehavior) {
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateUserIdExpressions = taskDefinition.getCandidateUserIdExpressions();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty() || !candidateUserIdExpressions.isEmpty()) {

              // 查询历史任务中最新的一条同名记录
              List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                      .taskDefinitionKey(task.getTaskDefinitionKey()).executionId(executionId).orderByHistoricTaskInstanceEndTime().desc().list();

              // 没有历史任务时继续下一个循环
              if (historicTaskInstances.isEmpty()) {
                continue;
              }

              // 查询有办理人的历史任务
              HistoricTaskInstance historySameKeyTask = null;
              for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                if (historicTaskInstance.getAssignee() != null) {
                  historySameKeyTask = historicTaskInstance;
                  break;
                }
              }

              // 如果这条任务已经办理过由系统自动签收分配给上一次办理的人
              if (historySameKeyTask != null && StringUtils.isNotBlank(historySameKeyTask.getAssignee())) {
                String lastAssignee = historySameKeyTask.getAssignee();
                taskService.claim(task.getId(), lastAssignee);
              }
              
            }
          }
        }
      }
    }
  }

}