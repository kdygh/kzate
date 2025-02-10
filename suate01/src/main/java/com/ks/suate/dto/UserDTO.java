package com.ks.suate.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
	private int user_id;
	private Timestamp user_create_date;
	private String user_pw;
}
