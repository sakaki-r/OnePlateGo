package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Meal;
import com.example.app.domain.Menu;
import com.example.app.service.MenuService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final MenuService mealService;

	@GetMapping("/")
	public String showHome(Model model) {
		model.addAttribute("mealRequest", new Menu());
		return "home";
	}
	
	@PostMapping("/result")
	public String showResult(@ModelAttribute Menu mealRequest, Model model) {
	    List<Meal> meals = mealService.getMeals(mealRequest); 
	    model.addAttribute("selectedMeals", meals);
	    model.addAttribute("ingredients", mealService.getIngredientsWithCount(meals, mealRequest.getMealCount()));
	    model.addAttribute("mealRequest", mealRequest);
	    model.addAttribute("keepIds",new ArrayList<String>()); //初回はチェック入れない
	    return "menu";
	}

	
	@PostMapping("/reselect")
	public String reselect(@RequestParam(value = "keepIds", required = false) List<String> keepIds,
	                       @ModelAttribute Menu menuRequest, Model model) {
	    if (keepIds == null) keepIds = new ArrayList<>();
	    
	    List<Meal> newMeals = mealService.reselectMealsWithCategory(keepIds, menuRequest);
	    
	    model.addAttribute("selectedMeals", newMeals);
	    model.addAttribute("ingredients", mealService.getIngredientsWithCount(newMeals, menuRequest.getMealCount()));
	    model.addAttribute("mealRequest", menuRequest);
	    model.addAttribute("keepIds", keepIds);
	    return "menu";
	}

}
