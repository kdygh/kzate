package com.ks.suate01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ks.suate01.dao.UserDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDAO userDAO;
	private final MessageService messageService;
	
	public int find(int user_id, String user_pw)
	{
		int result = userDAO.find(user_id, user_pw);
		
		if(result <= 0)
		{
			messageService.logToFile(user_id, 
					messageService.getMessage("91", "001"));
		}
		
		return result;
	}
}
