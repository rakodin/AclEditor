package org.rakovsky.acl.data;

import lombok.Getter;

@Getter
public enum DepTreeField {
	FUNC_DEPARTMENT_CD("Департамент"),
	PARENT_DEPARTMENT_CD("Вышестоящий департамент"),
	CONFIGUDATION_CD("Конфигурация"),
	DESCR("Наименование");
	
	private final String descr;
	private DepTreeField(String descr) {
		this.descr = descr;
	}
}
