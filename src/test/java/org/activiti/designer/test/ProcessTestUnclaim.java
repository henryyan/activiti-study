package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;

/**
 * 反签收原理：任务创建时把候选人、组插入到ACT_RU_IDENTITYLINK中，当任务被签收后候选人、组数据依然保留并不是删除
 * 如此当设置任务的assigee属性为null的时候又可以根据候选人查询到任务
 * 
 * @author henryyan
 */
public class ProcessTestUnclaim {

  private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/Unclaim.bpmn";

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  public void startProcess() throws Exception {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    repositoryService.createDeployment().addInputStream("Unclaim.bpmn20.xml", new FileInputStream(filename)).deploy();
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("name", "Activiti");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Unclaim", variableMap);
    assertNotNull(processInstance.getId());
    System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

    TaskService taskService = activitiRule.getTaskService();
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user1").count(), 1);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user2").count(), 1);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user3").count(), 1);

    Task task = taskService.createTaskQuery().singleResult();
    taskService.claim(task.getId(), "user1");

    assertEquals(taskService.createTaskQuery().taskCandidateUser("user1").count(), 0);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user2").count(), 0);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user3").count(), 0);

    ManagementService managementService = activitiRule.getManagementService();
    TablePage listPage = managementService.createTablePageQuery().tableName(managementService.getTableName(IdentityLinkEntity.class)).listPage(0, 100);
    List<Map<String, Object>> rows = listPage.getRows();
    for (Map<String, Object> map : rows) {
      Set<Entry<String, Object>> entrySet = map.entrySet();
      for (Entry<String, Object> entry : entrySet) {
        // System.out.println("key=" + entry.getKey() + ", value=" +
        if (entry.getKey().equals("TYPE_")) {
          assertTrue(ArrayUtils.contains(new String[] { "user1", "user2", "user3" }, map.get("USER_ID_").toString()));
        }
      }
    }

    // unclaim
    taskService.setAssignee(task.getId(), null);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user1").count(), 1);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user2").count(), 1);
    assertEquals(taskService.createTaskQuery().taskCandidateUser("user3").count(), 1);
  }
}