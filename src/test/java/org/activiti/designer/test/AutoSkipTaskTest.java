package org.activiti.designer.test;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
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

        // 建立用户与组的关系
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user1");
        identityService.saveUser(user1);
        User user2 = identityService.newUser("user2");
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        identityService.createMembership("user1", "group1");

        // 启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("threeTask");
        System.out.println("已启动流程：" + processInstance.getId());

        // 完成第一个任务
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

        // 任务到达usertask7
        task = taskService.createTaskQuery().singleResult();
        // taskService.claim(task.getId(), "user1");
        assertEquals("目的地", task.getName());

        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstance.getProcessInstanceId());
        assertEquals(4, processInstanceComments.size());
    }
}
