package org.activiti.designer.test.bugs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FixFlowTextInDiagram {
	private static Logger logger = LoggerFactory.getLogger(FixFlowTextInDiagram.class);
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	//	@Deployment(resources = { "diagrams/bugs/leave.bpmn" })
	public void startProcess1111() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService
				.createDeployment()
				.addInputStream(
						"aaa.bpmn20.xml",
						new FileInputStream(
								"/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/bugs/leave.bpmn"))
				.deploy();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getDiagramResourceName());

		byte[] b = new byte[resourceAsStream.available()];

		resourceAsStream.read(b, 0, b.length);

		// create file if not exist
		String diagramDir = "/Users/henryyan";
		File diagramDirFile = new File(diagramDir);
		if (!diagramDirFile.exists()) {
			diagramDirFile.mkdirs();
		}
		String diagramPath = diagramDir + "/leave.png";
		File file = new File(diagramPath);

		// 文件存在退出
		if (file.exists()) {
			// 文件大小相同时直接返回否则重新创建文件(可能损坏)
			logger.debug("diagram exist, ignore... : {}", diagramPath);
		} else {
			file.createNewFile();
		}

		logger.debug("export diagram to : {}", diagramPath);

		// wirte bytes to file
		FileUtils.writeByteArrayToFile(file, b);
	}

}