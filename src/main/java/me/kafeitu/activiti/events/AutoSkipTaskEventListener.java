package me.kafeitu.activiti.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.EngineServices;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.cmd.CompleteTaskCmd;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author: Henry Yan
 */
public class AutoSkipTaskEventListener implements ActivitiEventListener {

    public static Set<String> skipedTasks = new HashSet<>();

    @Override
    public void onEvent(ActivitiEvent event) {
        if (event.getType().equals(ActivitiEventType.TASK_CREATED)
                || event.getType().equals(ActivitiEventType.TASK_ASSIGNED)) {
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;

            Object entity = entityEvent.getEntity();
            if (entity instanceof TaskEntity) {
                TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();

                Map<String, String> skipTasks = new HashMap<>();
                skipTasks.put("usertask2", "user1"); // 预先设定
                skipTasks.put("usertask4", "user1"); // 动态设定以
                skipTasks.put("usertask5", "user1"); // 候选人
                skipTasks.put("usertask6", "user1"); // 候选组

                EngineServices engineServices = entityEvent.getEngineServices();
                IdentityService identityService = engineServices.getIdentityService();
                TaskService taskService = engineServices.getTaskService();

                String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
                String userId = skipTasks.get(taskDefinitionKey);

                if (userId == null) {
                    return;
                }
                System.out.println("key: " + taskDefinitionKey + ", userId=" + userId);

                // 查询当前人的组
                List<String> groupIds = new ArrayList<>();
                List<Group> list = identityService.createGroupQuery().groupMember(userId).list();
                for (Group group : list) {
                    groupIds.add(group.getId());
                }

                // 如果当前节点是需要跳过的，直接调用任务完成命令
                if (event.getType().equals(ActivitiEventType.TASK_ASSIGNED)) {
                    if (userId != null) {
                        if (taskEntity.getAssignee().equals(userId)) {
                            skipTask(taskEntity, event);
                        }
                    }
                }

                // 创建事件用来处理候选的情况
                if (event.getType().equals(ActivitiEventType.TASK_CREATED) && taskEntity.getAssignee() == null) {
                    Set<IdentityLink> candidates = taskEntity.getCandidates();
                    for (IdentityLink identityLink : candidates) {
                        // 处理候选人
                        if (identityLink.getUserId() != null && identityLink.getUserId().equals(userId)) {
                            // 必须删除link，否则报错
                            taskEntity.deleteUserIdentityLink(identityLink.getUserId(), "candidate");

                            // 签收触发TASK_ASSIGNED
                            taskService.claim(taskEntity.getId(), userId);
                        } else if (groupIds.contains(identityLink.getGroupId())) {
                            // 必须删除link，否则报错
                            taskEntity.deleteGroupIdentityLink(identityLink.getGroupId(), "candidate");

                            // 签收触发TASK_ASSIGNED
                            taskService.claim(taskEntity.getId(), userId);
                        }
                    }
                }
            }
        } else {
            System.out.println("拦截事件：" + ToStringBuilder.reflectionToString(event));
        }
    }

    private void skipTask(TaskEntity taskEntity, ActivitiEvent event) {
        ManagementService managementService = event.getEngineServices().getManagementService();
        TaskService taskService = event.getEngineServices().getTaskService();
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        if (skipedTasks.contains(taskDefinitionKey)) {
            return;
        }
        // 设置变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("skip", true);
        CompleteTaskCmd command = new CompleteTaskCmd(taskEntity.getId(), variables, true);

        taskService.addComment(taskEntity.getId(), taskEntity.getProcessInstanceId(), "自动跳过");

        // 执行命令，直接完成当前任务
//        System.out.println("准备跳过任务：" + taskEntity.getName() + "-->" + taskDefinitionKey);
        managementService.executeCommand(command);
        System.out.println("已跳过任务：" + taskEntity.getName() + "-->" + taskDefinitionKey);

        skipedTasks.add(taskDefinitionKey);
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
