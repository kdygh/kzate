package com.ks.suate01.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ks.suate.dto.GameDTO;
import com.ks.suate01.service.MessageService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameDAO {

	private final SqlSessionTemplate sql;
	private final MessageService messageService;
	
	private void testconn(int user_id)
	{
		try 
		{
			if(sql.getConnection() != null && !sql.getConnection().isClosed())
			{
				messageService.logToFile(user_id, 
						messageService.getMessage("91", "001"));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GameDTO find(int user_id)
	{
		testconn(user_id);
		
		return sql.selectOne("Game.findGameRecord", user_id); 
	}

	public int reset(int user_id, String game_secret_num) {
		// TODO Auto-generated method stub
		
		testconn(user_id);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", user_id);
		params.put("game_secret_num", game_secret_num);
		
		return sql.update("Game.resetGameRecord", params);
	}

	public int update(int user_id, String user_input_num, String game_result) {
		// TODO Auto-generated method stub
		testconn(user_id);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", user_id);
		params.put("user_input_num", user_input_num);
		params.put("game_result", game_result);
		
		return sql.update("Game.updateGameRecord", params);
	}

	public int end(int user_id, int user_point) {
		// TODO Auto-generated method stub
		testconn(user_id);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", user_id);
		params.put("user_point", user_point);
		
		return sql.update("Game.endGameRecord", params);
	}
}
