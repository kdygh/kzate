package com.ks.suate01.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ks.suate.dto.GameDTO;
import com.ks.suate01.service.GameService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GameController {
	
	private final GameService gameService;
	
	@GetMapping("/popup")
	public String popup()
	{
		return "popup";
	}
	
	@GetMapping("/game")
	public String initGame(HttpSession session, Model model)
	{
		gameService.initGame(session, model);
		
		return "game";
	}
	
	@PostMapping("/input")
	public String input(@RequestParam("first") String first,
            @RequestParam("second") String second,
            @RequestParam("third") String third,
            HttpSession session,
            Model model)
	{
		String inputNum = first + second + third;
		
		return gameService.playGame(session, inputNum);
	}

}
