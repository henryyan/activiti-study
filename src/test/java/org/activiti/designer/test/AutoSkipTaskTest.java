package org.activiti.designer.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: Henry Yan
 */
public class AutoSkipTaskTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-autoskip.xml");

    /**
     * usertask1(user1) --> usertask2(user1,will be skiped) --> usertask3(user2) --> usertask4(empty assignee) --> usertask5(empty assignee)
     * 测试用例过程：完成第一个任务，任务到达usertask3，完成usertask3，给usertask4分配办理人，任务到达usertask5
     */
    @Test
    @Deployment(resources = "diagrams/threeTask.bpmn")
    public void testSkip() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("threeTask");
        System.out.println("已启动流程：" + processInstance.getId());
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("user1", task.getAssignee());
        taskService.complete(task.getId());
        System.out.println("完成任务：usertask1");

        // 查询任务，通过调用
        task = taskService.createTaskQuery().singleResult();
        assertEquals("usertask3", task.getTaskDefinitionKey());

        taskService.complete(task.getId());
        System.out.println("完成任务：usertask3");

        // 任务达到usertask4
        task = taskService.createTaskQuery().singleResult();
        assertEquals("usertask4", task.getTaskDefinitionKey());

        // 为usertask4设置办理人
        taskService.setAssignee(task.getId(), "user1");

        // 任务到达usertask5
        task = taskService.createTaskQuery().singleResult();
        assertEquals("usertask5", task.getTaskDefinitionKey());
    }
}
