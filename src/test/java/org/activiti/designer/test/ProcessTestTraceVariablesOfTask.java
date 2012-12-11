package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

/**
 * 从历史中读取任务并关联输出变量
 * @author henryyan
 */
public class ProcessTestTraceVariablesOfTask {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = { "diagrams/TraceVariablesOfTask.bpmn" })
  public void startProcess() throws Exception {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TraceVariablesOfTask", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    TaskService taskService = activitiRule.getTaskService();
    // 执行第一个任务
    Task task = taskService.createTaskQuery().singleResult();
    variableMap = new HashMap<String, Object>();
    for (int i = 0; i < 5; i++) {
      variableMap.put("task1-variable" + (i + 1), "task1-value" + (i + 1));
    }
    taskService.complete(task.getId(), variableMap);

    // 执行第二个任务
    task = taskService.createTaskQuery().singleResult();
    variableMap = new HashMap<String, Object>();
    for (int i = 0; i < 5; i++) {
      variableMap.put("task2-variable" + (i + 1), "task2-value" + (i + 1));
    }
    taskService.complete(task.getId(), variableMap);

    // Map<历史活动ID, Map<变量名称, 变量值>>
    Map<String, Map<String, Object>> activiy_name_value = new HashMap<String, Map<String, Object>>();

    // 从历史中读取变量
    HistoryService historyService = activitiRule.getHistoryService();
    List<HistoricDetail> list = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
    for (HistoricDetail historicDetail : list) {
      HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
      // System.out.println(variable.getName() + " = " + variable.getValue());
      String activityInstanceId = variable.getActivityInstanceId();

      // 初始化
      Map<String, Object> tempVariableMap = activiy_name_value.get(activityInstanceId);
      if (tempVariableMap == null) {
        tempVariableMap = new HashMap<String, Object>();
        activiy_name_value.put(activityInstanceId, tempVariableMap);
      }

      tempVariableMap.put(variable.getVariableName(), variable.getValue());

    }

    // 从历史读取用户任务的活动记录
    List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery().activityType("userTask").list();
    for (HistoricActivityInstance historicActivityInstance : activityList) {
      String activityName = historicActivityInstance.getActivityName();
      System.out.println(activityName);
      Map<String, Object> map = activiy_name_value.get(historicActivityInstance.getId());
      Set<Entry<String, Object>> entrySet = map.entrySet();
      for (Entry<String, Object> entry : entrySet) {
        System.out.println(String.format("\t- %s = %s", entry.getKey(), entry.getValue()));
      }
    }

  }
}