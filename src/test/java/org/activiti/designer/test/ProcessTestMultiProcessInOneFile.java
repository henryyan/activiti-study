package org.activiti.designer.test;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMultiProcessInOneFile {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/MultiProcessInOneFile.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule
				.getRepositoryService();
		repositoryService
				.createDeployment()
				.addInputStream("MultiProcessInOneFile.bpmn20.xml",
						new FileInputStream(filename)).deploy();
		long count = repositoryService.createProcessDefinitionQuery().count();
		assertEquals(2, count);
		
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		assertEquals("process1", list.get(0).getKey());
		assertEquals("leave", list.get(1).getKey());
	}
}