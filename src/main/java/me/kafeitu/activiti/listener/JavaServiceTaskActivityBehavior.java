package me.kafeitu.activiti.listener;

import java.util.List;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class JavaServiceTaskActivityBehavior implements ActivityBehavior {

	private static final long serialVersionUID = 1L;

  @Override
	public void execute(ActivityExecution execution) throws Exception {
		PvmActivity activity = execution.getActivity();
		List<PvmTransition> outgoingTransitions = activity.getOutgoingTransitions();
		for (PvmTransition pvmTransition : outgoingTransitions) {
			System.out.println(pvmTransition.getId());
		}
	}

}
