package me.kafeitu.activiti;

import java.io.Serializable;
import java.util.Date;

public class JavaServiceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String print() {
		System.out.println("print by JavaServiceBean, current time" + new Date().toString());
		return "the value will be returned";
	}
	
}
