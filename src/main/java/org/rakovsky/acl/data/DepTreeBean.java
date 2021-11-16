package org.rakovsky.acl.data;

import java.util.Objects;

public class DepTreeBean {
	private String depId;
	private String id;
	private String parentId;
	private String name;

	public DepTreeBean(String depId, String id, String parentId, String name) {
		this.depId = depId;
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepId() {
		return this.depId;
	}
	
	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(depId, id, parentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepTreeBean other = (DepTreeBean) obj;
		return Objects.equals(depId, other.depId) && Objects.equals(id, other.id)
				&& Objects.equals(parentId, other.parentId);
	}

	@Override
	public String toString() {
		return "DepTreeBean [depId=" + depId + ", id=" + id + ", parentId=" + parentId + ", name=" + name + "]";
	}



}
