package org.activiti.designer.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:applicationContext-test.xml")
public class GetFieldOfTaskListenerWithSpring extends SpringActivitiTestCase {
  
  @Autowired
  RuntimeService runtimeService;
  
  @Autowired
  RepositoryService repositoryService;

//  @Deployment(resources = "diagrams/qun/GetFieldOfTaskListener.bpmn")
  public void testField() {
    long count = repositoryService.createProcessDefinitionQuery().count();
    assertEquals(2, count);
//    runtimeService.startProcessInstanceByKey("GetFieldOfTaskListener");
  }

}
