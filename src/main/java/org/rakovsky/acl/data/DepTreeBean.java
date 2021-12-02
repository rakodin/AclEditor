package org.rakovsky.acl.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DepTreeBean {
	private String id;
	private String parentId;
	private String name;
}
