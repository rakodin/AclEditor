package org.rakovsky.acl.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ConfigurationBean {
	private String id;
	private String name;
	
	public static ConfigurationBean EMPTY = new ConfigurationBean(" ", "---");
}
