package org.rakovsky.acl.data;

import java.util.Objects;

public class DepTreeBean {
	private String id;
	private String parentId;
	private String name;

	public DepTreeBean(String id, String parentId, String name) {
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return String.format("DepTreeBean [id=%s, parentId=%s, name=%s]", id, parentId, name);
	}

}
