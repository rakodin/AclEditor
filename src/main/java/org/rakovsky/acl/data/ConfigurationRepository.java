package org.rakovsky.acl.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationRepository {
	public static List<ConfigurationBean> getConfigurations() {
		Connection c = AppDataSource.getInstance().getConnection();
		List<ConfigurationBean> ret = new ArrayList<>();
		try {
			PreparedStatement s = c.prepareStatement("SELECT CONFIGURATION_CD, DESCR FROM CFG_CONFIGURATION");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ret.add(new ConfigurationBean(rs.getString("CONFIGURATION_CD"), rs.getString("DESCR")));
			}
			rs.close();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.getInstance().closeConnection(c);
		}
		return ret;
	}

	public static List<DepTreeBean> getDepartments(String configId) {
		Connection c = AppDataSource.getInstance().getConnection();
		List<DepTreeBean> ret = new ArrayList<>();
		try {
			String sql = "WITH DEPS AS (SELECT D.FUNC_DEPARTMENT_CD, D.PARENT_DEPARTMENT_CD, D.DESCR "
					+ "FROM CFG_FUNC_DEPARTMENT D "
					+ "WHERE D.CONFIGURATION_CD = ?)\n"
					+ "SELECT DP.* FROM  DEPS DP\n"
					+ "START WITH PARENT_DEPARTMENT_CD IS NULL CONNECT BY PRIOR FUNC_DEPARTMENT_CD = PARENT_DEPARTMENT_CD";
			PreparedStatement s = c.prepareStatement(sql);
			s.setString(1, configId);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ret.add(new DepTreeBean(rs.getString("FUNC_DEPARTMENT_CD"), rs.getString("PARENT_DEPARTMENT_CD"), rs.getString("DESCR")));
			}
			rs.close();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.getInstance().closeConnection(c);
		}

		return ret;
	}

	public static List<EmplBean> getEmployeeList(String depId) {
		Connection c = AppDataSource.getInstance().getConnection();
		List<EmplBean> ret = new ArrayList<>();
		try {
			String sql = "select E.FUNC_EMPLOYEE_CD, E.PERSON_ID, E.FUNC_DEPARTMENT_CD, E.START_DT, E.END_DT, S.LOGIN, S.USER_SECOND_NAME, "
					+ "S.USER_NAME, S.USER_PATRONYMIC_NAME "
					+ "FROM CFG_FUNC_EMPLOYEE E INNER JOIN PER_PERSON PP ON PP.PERSON_ID =  E.PERSON_ID \n"
					+ "INNER JOIN SU_USER S ON S.USER_ID = PP.LINKED_USER_ID WHERE E.FUNC_DEPARTMENT_CD = ?";
			PreparedStatement s = c.prepareStatement(sql);
			s.setString(1, depId);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ret.add(new EmplBean(rs.getString("FUNC_EMPLOYEE_CD"), rs.getString("PERSON_ID"),
						rs.getString("FUNC_DEPARTMENT_CD"), String.format("%s %s %s", rs.getString("USER_SECOND_NAME"),
								rs.getString("USER_NAME"), rs.getString("USER_PATRONYMIC_NAME")),
						rs.getString("LOGIN"), rs.getDate("START_DT"), rs.getDate("END_DT")));

			}
			rs.close();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.getInstance().closeConnection(c);
		}
		return ret;
	}
}
