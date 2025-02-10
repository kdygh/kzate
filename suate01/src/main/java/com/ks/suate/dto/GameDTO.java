package com.ks.suate.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameDTO {
	private int user_id;
	private int user_point;
	private String user_input_num;
	private String game_secret_num;
	private String game_result;
	private int game_no;
	private int game_end;
	private Timestamp user_play_date;
}
