package com.ks.suate01.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ks.suate01.service.MessageService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDAO {

	private final SqlSessionTemplate sql;
	private final MessageService messageService;
	
	public int find(int user_id, String user_pw)
	{
		try 
		{
			if(sql.getConnection() != null && !sql.getConnection().isClosed())
			{
				messageService.logToFile(user_id, 
						messageService.getMessage("91", "001"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("user_id", user_id);
		param.put("user_pw", user_pw);
		
		return sql.selectOne("User.findUser", param);
	}
}
