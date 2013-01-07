package org.activiti.designer.test;

import static org.junit.Assert.assertFalse;

import java.io.FileInputStream;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestJpdl {

	private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/jpdl/helloworld.jpdl.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("helloworld.jpdl.xml", new FileInputStream(filename)).deploy();
		List<Deployment> list = repositoryService.createDeploymentQuery().list();
		assertFalse(list.isEmpty());
		for (Deployment deployment : list) {
			System.out.println(deployment.getName());
		}
	}
}