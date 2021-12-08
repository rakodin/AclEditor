package org.rakovsky.acl.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.icu.impl.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class DepTreeBean {
	private String id;
	private String parentId;
	private String name;
	private String confId;
	
	
	public static List<Pair<DepTreeField, String>> getColumns(DepTreeBean dt) {
		List<Pair<DepTreeField, String>> ret = new ArrayList<>();
		ret.add(Pair.of(DepTreeField.CONFIGUDATION_CD, dt.confId));
		ret.add(Pair.of(DepTreeField.PARENT_DEPARTMENT_CD, convertNull(dt.parentId)));
		ret.add(Pair.of(DepTreeField.FUNC_DEPARTMENT_CD, convertNull(dt.id)));
		ret.add(Pair.of(DepTreeField.DESCR, convertNull(dt.name)));
	
		return Collections.unmodifiableList(ret);
	}
	
	private static String convertNull(String val) {
		return val != null? val : "";
	}
	
	public static DepTreeBean newDepartment(String confId, String parentId) {
		return new DepTreeBean(null, parentId, null, confId);
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
	public int hashCode() {
		return Objects.hash(id);
	}
}
