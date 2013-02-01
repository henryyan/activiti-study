package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestDymaticForm {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/form/DymaticForm.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("DymaticForm.bpmn20.xml", new FileInputStream(filename)).deploy();
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DymaticForm").latestVersion().singleResult();
		FormService formService = activitiRule.getFormService();
		StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
		assertNull(startFormData.getFormKey());
		
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "HenryYan");
		
		ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
		assertNotNull(processInstance);
		
		// 运行时变量
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
		assertEquals(variables.size(), 1);
		Set<Entry<String, Object>> entrySet = variables.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		
		// 历史记录
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricDetail> list = historyService.createHistoricDetailQuery().formProperties().list();
		assertEquals(1, list.size());
		
		// 获取第一个节点
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("First Step", task.getName());
		
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		assertNotNull(taskFormData);
		assertNull(taskFormData.getFormKey());
		List<FormProperty> taskFormProperties = taskFormData.getFormProperties();
		assertNotNull(taskFormProperties);
		for (FormProperty formProperty : taskFormProperties) {
			System.out.println(ToStringBuilder.reflectionToString(formProperty));
		}
		formProperties = new HashMap<String, String>();
		formProperties.put("setInFirstStep", "01/12/2012");
		formService.submitTaskFormData(task.getId(), formProperties);
		
		// 获取第二个节点
		task = taskService.createTaskQuery().taskName("Second Step").singleResult();
		assertNotNull(task);
		taskFormData = formService.getTaskFormData(task.getId());
		assertNotNull(taskFormData);
		List<FormProperty> formProperties2 = taskFormData.getFormProperties();
		assertNotNull(formProperties2);
		assertEquals(1, formProperties2.size());
		assertNotNull(formProperties2.get(0).getValue());
		assertEquals(formProperties2.get(0).getValue(), "01/12/2012");
		
		List<HistoricDetail> details = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
		for (HistoricDetail historicDetail : details) {
			if (historicDetail instanceof HistoricFormPropertyEntity) {
				HistoricFormPropertyEntity formEntity = (HistoricFormPropertyEntity) historicDetail;
				System.out.println(String.format("form->, key: %s, value: %s", formEntity.getPropertyId(), formEntity.getPropertyValue()));
			} else if (historicDetail instanceof HistoricVariableUpdate) {
				HistoricVariableUpdate varEntity = (HistoricVariableUpdate) historicDetail;
				System.out.println(String.format("variable->, key: %s, value: %s", varEntity.getVariableName(), varEntity.getValue()));
			}
		}
		
		long count = historyService.createHistoricDetailQuery().count();
		System.out.println(count);
	}
}