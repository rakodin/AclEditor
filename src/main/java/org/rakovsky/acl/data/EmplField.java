package org.rakovsky.acl.data;

import lombok.Getter;

@Getter
public enum EmplField {
	FUNC_EMPLOYEE_CD("Ид-р"),
	USER_ID("Ид-р пользователя"),
	FUNC_DEPARTMENT_CD("Ид-р департамента"),
	PERSON_ID("Ид-р персоны"),
	PERSON_NAME("ФИО"),
	LOGIN("Логин"),
	START_DT("Дата начала работы"),
	END_DT("Дата окончания работы");
	
	
	private final String descr;
	private EmplField(String descr) {
		this.descr = descr;
	}
}
