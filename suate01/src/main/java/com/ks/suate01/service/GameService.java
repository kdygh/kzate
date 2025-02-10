package com.ks.suate01.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ks.suate.dto.GameDTO;
import com.ks.suate01.dao.GameDAO;
import com.ks.suate01.dao.UserDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

	private final GameDAO gameDAO;
	private final MessageService messageService;
	
	private GameDTO find(int user_id)
	{
		return gameDAO.find(user_id);
	}
	
	private int reset(int user_id, String game_secret_num)
	{
		int result = gameDAO.reset(user_id, game_secret_num);
		
		if(result < 0)
		{
			messageService.logToFile(user_id, 
					messageService.getMessage("91", "002"));
		}
		
		return result;
	}

	private int update(int user_id, String user_input_num, String game_result) {
		// TODO Auto-generated method stub
		
		int result = gameDAO.update(user_id, user_input_num, game_result);
				
		if(result < 0)
		{
			messageService.logToFile(user_id, 
					messageService.getMessage("91", "002"));
		}
		
		return result;
	}

	private int end(int user_id, int user_point) {
		// TODO Auto-generated method stub
		
		int result = gameDAO.end(user_id, user_point);
		
		if(result < 0)
		{
			messageService.logToFile(user_id, 
					messageService.getMessage("91", "002"));
		}
		
		return result;
	}
	
	private boolean isPlayDateYesterday(GameDTO gameDTO)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		// 시스템의 기본 시간대(Local Time) 적용
	    sdf.setTimeZone(TimeZone.getDefault());
		
		String date1 = sdf.format(gameDTO.getUser_play_date());
		String date2 = sdf.format(new Date());
		
		return date2.compareTo(date1) > 0;
	}
	
	private void setGameRecord(HttpSession session, Model model, GameDTO GameDTO)
	{
		session.removeAttribute("input_num_list");
		session.removeAttribute("game_result_list");
		
		String inputNum = GameDTO.getUser_input_num();
		List<String> inputNumList = new ArrayList<String>();
		
		if(inputNum != null && inputNum.equals("") == false)
		{
			String temp = "";
			for(int i = 0; i < inputNum.length(); i++)
			{						
				//3桁の数字をセーブする
				if(inputNum.charAt(i) == ',')
				{
					inputNumList.add(temp);
					temp = "";
					continue;
				}
				
				temp += inputNum.charAt(i);
			}
			
			inputNumList.add(temp);
			
			session.setAttribute("input_num_list", inputNumList);
		}
		
		String result = GameDTO.getGame_result();
		List<String> gameResultList = new ArrayList<>();
		
		if(result != null && result.equals("") == false)
		{
			String temp = "";
			
			for(int i = 0; i < result.length(); i++)
			{
				//3桁の数字をセーブする
				if(result.charAt(i) == ',')
				{
					gameResultList.add(temp);
					temp = "";
					continue;
				}
				
				temp += result.charAt(i);
			}	
			
			gameResultList.add(temp);
			
			session.setAttribute("game_result_list", gameResultList);
		}
		
		session.setAttribute("user_point", GameDTO.getUser_point());
		session.setAttribute("game_secret_num", GameDTO.getGame_secret_num());
		session.setAttribute("game_no", GameDTO.getGame_no());
	}
	
	private boolean isCleared(HttpSession session, String result, String inputNum, int game_no)
	{
		if(result.equals("当たり"))
		{		
			int point = 0;
			
			if(game_no <= 5)
			{
				point = 1000;
			}
			else if(game_no >= 6 && game_no <= 7)
			{
				point = 500;
			}
			else
			{
				point = 200;
			}
			
			int userPoint = (int)session.getAttribute("user_point");
			session.setAttribute("user_playable", 0);
			session.setAttribute("user_point", userPoint + point);
			session.setAttribute("result_point", point);
			session.setAttribute("gameResultMessage", messageService.getMessage("92", "001"));
			
			update((int)session.getAttribute("user_id"), inputNum, result);
			end((int)session.getAttribute("user_id"), point);
			
			return true;
		}
		
		return false;
	}
	
	private void finishGame(HttpSession session, String result, String inputNum, int game_no)
	{	
		int point = 0;
		
		update((int)session.getAttribute("user_id"), inputNum, result);
		end((int)session.getAttribute("user_id"), point);
	
		session.setAttribute("user_playable", 0);
		session.setAttribute("result_point", 0);
		session.setAttribute("gameResultMessage", messageService.getMessage("92", "002"));
	}
	
	private String initRandomNum()
	{
		String randomNum = "";
		
		while(true)
		{
			if(randomNum.length() >= 3)
			{
				break;
			}
			
			char num = (char)((Math.random() * 10) + 48);
			boolean isNumExists = false;
			
			if(randomNum.isEmpty() == true && num == '0')
			{
				continue;
			}
			
			for(int i = 0; i < randomNum.length(); i++)
			{
				if(randomNum.charAt(i) == num)
				{
					isNumExists = true;
					break;
				}
			}
			
			if(isNumExists == false)
			{
				randomNum += num;
			}
		}
		
		return randomNum;
	}
	
	private String gameResult(String answerNum, String inputNum)
	{
		int strike = 0;
		int ball = 0;
		String result = "";
		StringBuilder tempAnswer = new StringBuilder(answerNum);
		
		for(int i = 0; i < inputNum.length(); i++)
		{
			char temp = inputNum.charAt(i);
			
			for(int j = 0; j < tempAnswer.length(); j++)
			{
				if(temp == tempAnswer.charAt(j))
				{
					if(i == j)
					{
						tempAnswer.setCharAt(j, 's');
						strike++;
						break;
					}
					else
					{
						tempAnswer.setCharAt(j, 'b');
						ball++;
						break;
					}
				}
			}
		}
		
		if(strike <= 0 && ball <= 0)
		{
			result = "はずれ";
			return result;
		}
		
		if(strike >= 3)
		{
			result = "当たり";
			return result;
		}

		if(strike <= 0 && ball >= 1)
		{
			result = ball + "B";
		}
		else if(strike >= 1 && ball <= 0)
		{
			result = strike + "S";
		}
		else
		{
			result = strike + "S" + ball + "B";
		}
		
		return result;
	}

	public String playGame(HttpSession session, String inputNum) {
		// TODO Auto-generated method stub
		String result = gameResult((String)session.getAttribute("game_secret_num"), inputNum);
		
		int game_no = (int)session.getAttribute("game_no");
		
		//クリアしたか
		if(isCleared(session, result, inputNum, game_no) == false)
		{
			//ゲームがまだ終わってない時
			if(game_no < 10)
			{
				inputNum += ",";
				result += ",";
				game_no++;
			}
			else
			{
				finishGame(session, result, inputNum, game_no);
				return "redirect:/popup";
			}	
		}
		else
		{
			return "redirect:/popup";
		}
		
		update((int)session.getAttribute("user_id"), inputNum, result);
		session.setAttribute("game_no", game_no);
		
	    return "redirect:/game";  // 결과 페이지로 이동
	}
	
	public void initGame(HttpSession session, Model model)
	{
		GameDTO gameDTO = find((int)session.getAttribute("user_id"));
		
		if(gameDTO == null)
		{
			System.out.println("회원이 없다");
			messageService.logToFile((int)session.getAttribute("user_id"), 
					messageService.getMessage("91", "002"));
		}
		else
		{	
			//0 - ゲーム終了状態　１－ゲーム未終了状態
			//ゲームが終了状態、あるいはゲームをプレイしてから一日が経過した場合
			if(gameDTO.getGame_end() <= 0) 
			{
				model.addAttribute("finishedFlag", true);
				model.addAttribute("resultMessage", messageService.getMessage("91", "004"));
			}
			else
			{
				//昨日のゲームデータをリセットする。
				// ゲーム終了状態は毎日午前０時に　１（ゲーム未終了状態）に変わる
				if(isPlayDateYesterday(gameDTO) == true)
				{
					reset((int)session.getAttribute("user_id"), initRandomNum());
					gameDTO = find((int)session.getAttribute("user_id"));
				}
				
				model.addAttribute("finishedFlag", false);
			}
			
			setGameRecord(session, model, gameDTO);
		}
	}
}
