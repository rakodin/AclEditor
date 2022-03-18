package org.rakovsky.acl.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.icu.impl.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
 
@Data
@AllArgsConstructor
@ToString
public class EmplBean {
	private String id;
	private String suUserId;
	private String personId;
	private String funcDepartmentId;
	private String name;
	private String login;
	private Date startDate;
	private Date endDate;
	private static final SimpleDateFormat sdf = Utils.RUDATE;
	
	public static List<Pair<EmplField,String>> getColumns(EmplBean em) {
		List<Pair<EmplField,String>> ret = new ArrayList<>();
		ret.add(Pair.of(EmplField.FUNC_EMPLOYEE_CD,em.id));
		ret.add(Pair.of(EmplField.USER_ID,em.suUserId));
		ret.add(Pair.of(EmplField.FUNC_DEPARTMENT_CD,em.funcDepartmentId));
		ret.add(Pair.of(EmplField.PERSON_ID, em.personId));
		ret.add(Pair.of(EmplField.PERSON_NAME, em.name));
		ret.add(Pair.of(EmplField.LOGIN,em.login));
		ret.add(Pair.of(EmplField.START_DT,sdf.format(em.startDate)));
		ret.add(Pair.of(EmplField.END_DT,sdf.format(em.endDate)));
		return Collections.unmodifiableList(ret);
	}
}
