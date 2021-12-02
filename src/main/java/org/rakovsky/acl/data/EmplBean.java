package org.rakovsky.acl.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	public List<Pair<String,String>> getColumns() {
		List<Pair<String,String>> data = new ArrayList<>();
		data.add(Pair.of("FUNC_EMPLOYEE_CD",id));
		data.add(Pair.of("USER_ID",suUserId));
		data.add(Pair.of("FUNC_DEPARTMENT_CD",funcDepartmentId));
		data.add(Pair.of("PERSON_ID", personId));
		data.add(Pair.of("PERSON_NAME", name));
		data.add(Pair.of("LOGIN",login));
		data.add(Pair.of("START_DT",sdf.format(startDate)));
		data.add(Pair.of("END_DT",sdf.format(endDate)));
		return data;
	}
}
