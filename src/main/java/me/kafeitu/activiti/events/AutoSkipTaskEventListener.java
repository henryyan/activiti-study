package me.kafeitu.activiti.events;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.EngineServices;
import org.activiti.engine.ManagementService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.cmd.CompleteTaskCmd;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author: Henry Yan
 */
public class AutoSkipTaskEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        if (event.getType().equals(ActivitiEventType.TASK_ASSIGNED)) {
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;

            Object entity = entityEvent.getEntity();
            if (entity instanceof TaskEntity) {
                TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();

                Map<String, String> skipTasks = new HashMap<>();
                skipTasks.put("usertask2", "user1");
                skipTasks.put("usertask4", "user1");

                // 如果当前节点是需要跳过的，直接调用任务完成命令
                String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
                String userId = skipTasks.get(taskDefinitionKey);
                if (userId != null && taskEntity.getAssignee().equals(userId)) {
                    EngineServices engineServices = entityEvent.getEngineServices();
                    ManagementService managementService = engineServices.getManagementService();

                    // 设置local级别的变量
                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("skip", true);
                    CompleteTaskCmd command = new CompleteTaskCmd(taskEntity.getId(), variables, true);

                    // 执行命令，直接完成当前任务
                    System.out.println("准备跳过任务：" + taskDefinitionKey);
                    managementService.executeCommand(command);
                    System.out.println("已跳过任务：" + taskDefinitionKey);
                }
            }
        } else {
            System.out.println("拦截事件：" + ToStringBuilder.reflectionToString(event));
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
