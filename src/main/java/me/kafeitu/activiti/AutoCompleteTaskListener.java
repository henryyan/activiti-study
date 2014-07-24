package me.kafeitu.activiti;

import java.util.List;

import org.activiti.engine.EngineServices;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.task.IdentityLink;

public class AutoCompleteTaskListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		EngineServices engineServices = delegateTask.getExecution()
				.getEngineServices();
		TaskService taskService = engineServices.getTaskService();
		List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(delegateTask.getId());
		for (IdentityLink identityLink : identityLinksForTask) {
			taskService.deleteCandidateGroup(delegateTask.getId(), identityLink.getGroupId());
		}
		taskService.complete(delegateTask.getId());
	}

	// @Override
	// public void execute(ActivityExecution execution) throws Exception {
	// TaskService taskService = execution.getEngineServices().getTaskService();
	// taskService.complete(execution.getId());
	// }

}
