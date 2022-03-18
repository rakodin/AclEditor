package org.rakovsky.acl.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationRepository {
	/*
	 * private static final String DEP_TABLE = "CFG_FUNC_DEP_TMP"; private static
	 * final String EMP_TABLE = "CFG_FUNC_EMP_TMP";
	 */

	public static List<ConfigurationBean> getConfigurations() {
		Connection c = AppDataSource.get();
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
			AppDataSource.close(c);
		}
		return ret;
	}

	public static List<DepTreeBean> getDepartments(String configId) {
		Connection c = AppDataSource.get();
		List<DepTreeBean> ret = new ArrayList<>();
		try {
			String sql = "WITH DEPS AS (SELECT D.FUNC_DEPARTMENT_CD, D.PARENT_DEPARTMENT_CD, D.DESCR " + "FROM "
					+ AppDataSource.getDepartmentTableName() + " D " + "WHERE D.CONFIGURATION_CD = ?)\n" + "SELECT DP.* FROM  DEPS DP\n"
					+ "START WITH PARENT_DEPARTMENT_CD IS NULL CONNECT BY PRIOR FUNC_DEPARTMENT_CD = PARENT_DEPARTMENT_CD";
			PreparedStatement s = c.prepareStatement(sql);
			s.setString(1, configId);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ret.add(new DepTreeBean(rs.getString("FUNC_DEPARTMENT_CD"), rs.getString("PARENT_DEPARTMENT_CD"),
						rs.getString("DESCR"), configId));
			}
			rs.close();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.close(c);
		}

		return ret;
	}

	public static List<EmplBean> getEmployeeList(String depId) {
		Connection c = AppDataSource.get();
		List<EmplBean> ret = new ArrayList<>();
		try {
			String sql = "select E.FUNC_EMPLOYEE_CD, S.USER_ID, E.PERSON_ID, E.FUNC_DEPARTMENT_CD, E.START_DT, E.END_DT, S.LOGIN, S.USER_SECOND_NAME, "
					+ "S.USER_NAME, S.USER_PATRONYMIC_NAME FROM " 
					+ AppDataSource.getEmployeeTableName()
					+ " E INNER JOIN PER_PERSON PP ON PP.PERSON_ID =  E.PERSON_ID \n"
					+ "INNER JOIN SU_USER S ON S.USER_ID = PP.LINKED_USER_ID WHERE E.FUNC_DEPARTMENT_CD = ?\n"
					+ "ORDER BY S.USER_SECOND_NAME, S.USER_NAME, S.USER_PATRONYMIC_NAME";
			PreparedStatement s = c.prepareStatement(sql);
			s.setString(1, depId);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ret.add(new EmplBean(rs.getString("FUNC_EMPLOYEE_CD"), rs.getString("USER_ID"),
						rs.getString("PERSON_ID"), rs.getString("FUNC_DEPARTMENT_CD"),
						String.format("%s %s %s", rs.getString("USER_SECOND_NAME"), rs.getString("USER_NAME"),
								rs.getString("USER_PATRONYMIC_NAME")),
						rs.getString("LOGIN"), rs.getDate("START_DT"), rs.getDate("END_DT")));

			}
			rs.close();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.close(c);
		}
		return ret;
	}

	public static String insertDepartment(DepTreeBean newDep) {
		Connection c = AppDataSource.get();
		String id = null;
		try {
			id = getId(12, c);
			PreparedStatement s = c.prepareStatement("INSERT INTO " 
			+ AppDataSource.getDepartmentTableName() 
			+ "(FUNC_DEPARTMENT_CD, PARENT_DEPARTMENT_CD, CONFIGURATION_CD, DESCR) "
					+ "VALUES(?,?,?,?)");
			s.setString(1, id);
			if (newDep.getParentId() == null) {
				s.setNull(2, Types.VARCHAR);
			} else {
				s.setString(2, newDep.getParentId());
			}
			s.setString(3, newDep.getConfId());
			s.setString(4, newDep.getName());
			s.executeUpdate();
			s.close();
		} catch (SQLException e) {	
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.close(c);
		}
		return id;
	}
	
	private static String getId(int num, Connection c) throws SQLException {
		PreparedStatement s = c.prepareStatement("SELECT CRM_CS_ACCESS_UTILS.GEN_RAND_ID(?) FROM DUAL");
		s.setInt(1, num);
		ResultSet rs = s.executeQuery();
		rs.next();
		String ret = rs.getString(1);
		rs.close();
		s.close();
		return ret;		
	}

	public static void updateDepartment(DepTreeBean newDep) {
		Connection c = AppDataSource.get();
		try {
			PreparedStatement s = c.prepareStatement("UPDATE " + AppDataSource.getDepartmentTableName() 
			+ " SET PARENT_DEPARTMENT_CD=?, CONFIGURATION_CD=?, DESCR=? WHERE FUNC_DEPARTMENT_CD=?");
			if (newDep.getParentId() == null) {
				s.setNull(1, Types.VARCHAR);
			} else {
				s.setString(1, newDep.getParentId());
			}
			s.setString(2, newDep.getConfId());
			s.setString(3, newDep.getName());
			s.setString(4, newDep.getId());
			s.executeUpdate();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.close(c);
		}
		
	}
	
	public static void removeDepartmentWithChilds(List<String> departmentIds) {
		Connection c = AppDataSource.get();
		try {
			PreparedStatement s = buildRemoveEmployeeFromDepsQuery(departmentIds, c);
			s.executeUpdate();
			s.close();
			s = buildRemoveDepartmentQuery(departmentIds, c);
			s.executeUpdate();
			s.close();
		} catch (SQLException e) {
			throw new RuntimeException("Can't execute SQL", e);
		} finally {
			AppDataSource.close(c);
		}
	}
	
	private static PreparedStatement buildRemoveDepartmentQuery(List<String> ids, Connection c) throws SQLException {
		final StringBuilder query = new StringBuilder("DELETE FROM ")
				.append(AppDataSource.getDepartmentTableName())
				.append(" WHERE FUNC_DEPARTMENT_CD IN (");
		return doBuildListQuery(query, ids, c);
	}
	
	private static PreparedStatement buildRemoveEmployeeFromDepsQuery(List <String> depIds, Connection c) throws SQLException {
		final StringBuilder query = new StringBuilder("DELETE FROM ")
				.append(AppDataSource.getEmployeeTableName())
				.append(" WHERE FUNC_DEPARTMENT_CD IN(");
		return doBuildListQuery(query, depIds, c);
	}
	
	private static PreparedStatement doBuildListQuery(StringBuilder query, List<String> ids, Connection c) throws SQLException {
		query.append('?');
		String first = ids.remove(0);
		ids.forEach(id-> query.append(",").append("?"));
		query.append(")");
		PreparedStatement s = c.prepareStatement(query.toString());
		int i=0;
		s.setString(++i, first);
		for (String id: ids) {
			s.setString(++i, id);
		}
		ids.add(first);
		return s;
	}
}
