package org.rakovsky.acl.data;

public class EmplBean {
	private String id;
	private String personId;
	private String fdId;
	private String name;
	private String login;
	public EmplBean(String id, String personId, String fdId, String name, String login) {
		super();
		this.id = id;
		this.personId = personId;
		this.fdId = fdId;
		this.name = name;
		this.login = login;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getFdId() {
		return fdId;
	}
	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@Override
	public String toString() {
		return "EmplBean [id=" + id + ", personId=" + personId + ", fdId=" + fdId + ", name=" + name + ", login="
				+ login + "]";
	}
	
}
