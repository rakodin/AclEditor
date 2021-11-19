package org.rakovsky.acl.data;

import java.sql.Date;

public class EmplBean {
	private String id;
	private String personId;
	private String fdId;
	private String name;
	private String login;
	private Date startDate;
	private Date endDate;

	public EmplBean(String id, String personId, String fdId, String name, String login, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.personId = personId;
		this.fdId = fdId;
		this.name = name;
		this.login = login;
		this.setStartDate(startDate);
		this.endDate = endDate;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return String.format("EmplBean [id=%s, personId=%s, fdId=%s, name=%s, login=%s, startDate=%s, endDate=%s]", id,
				personId, fdId, name, login, startDate, endDate);
	}

}
