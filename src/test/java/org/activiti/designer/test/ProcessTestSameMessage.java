package org.activiti.designer.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestSameMessage {

  private String filename = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/Message1.bpmn";
  private String filename2 = "/Users/henryyan/work/projects/activiti/activiti-study/src/test/resources/diagrams/Message2.bpmn";

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  public void startProcess() throws Exception {
    RepositoryService repositoryService = activitiRule.getRepositoryService();
    repositoryService.createDeployment().addInputStream("Message1.bpmn20.xml", new FileInputStream(filename)).deploy();
    try {
      repositoryService.createDeployment().addInputStream("Message2.bpmn20.xml", new FileInputStream(filename2)).deploy();
      fail("should be thrown Cannot deploy process definition 'Message2.bpmn20.xml': there already is a message event subscription for the message with name 'SameMessage'.");
    } catch (ActivitiException e) {
    }
  }
}