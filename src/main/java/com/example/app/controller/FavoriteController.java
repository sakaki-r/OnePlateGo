package com.example.app.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Favorite;
import com.example.app.domain.Member;
import com.example.app.security.MemberDetails;
import com.example.app.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myfavorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * お気に入り一覧表示
     */
    @GetMapping
    public String showFavorites(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        if (memberDetails == null || memberDetails.getMember() == null) {
            return "redirect:/login";
        }

        Member member = memberDetails.getMember();
        List<Favorite> favorites = favoriteService.getFavorites(member.getId());
        model.addAttribute("favorites", favorites);
        return "favorite/list";
    }

    /**
     * お気に入り追加（menuに戻る）
     */
   
    @PostMapping("/add")
    public String addFavorite(@RequestParam("dishId") int dishId,
                              @RequestParam("dishType") String dishType,
                              @AuthenticationPrincipal MemberDetails memberDetails,
                              RedirectAttributes redirectAttributes) {

        if (memberDetails == null || memberDetails.getMember() == null) {
            return "redirect:/login";
        }

        Member member = memberDetails.getMember();

        Favorite favorite = new Favorite();
        favorite.setMemberId(member.getId());
        favorite.setDishId(dishId);
        favorite.setDishType(dishType);

        boolean added = favoriteService.addFavorite(favorite);

        if (!added) {
            redirectAttributes.addFlashAttribute("message", "⚠ すでにお気に入りに登録されています。");
        } else {
            redirectAttributes.addFlashAttribute("message", "✅ お気に入りに追加しました！");
        }

        return "redirect:/result"; // リダイレクト後も message を一時的に保持できます
    }
    
    /**
     * お気に入り追加（API専用：fetch用）
     */
    @PostMapping("/add-json")
    @ResponseBody
    public String addFavoriteJson(@RequestParam("dishId") int dishId,
                                  @RequestParam("dishType") String dishType,
                                  @AuthenticationPrincipal MemberDetails memberDetails) {

        if (memberDetails == null || memberDetails.getMember() == null) {
            return "{\"status\":\"unauthorized\"}";
        }

        Member member = memberDetails.getMember();

        Favorite favorite = new Favorite();
        favorite.setMemberId(member.getId());
        favorite.setDishId(dishId);
        favorite.setDishType(dishType);

        boolean added = favoriteService.addFavorite(favorite);

        if (added) {
            return "{\"status\":\"added\"}";
        } else {
            return "{\"status\":\"duplicate\"}";
        }
    }
    

    /**
     * お気に入りから削除
     */
    @PostMapping("/delete")
    public String deleteFavorite(@RequestParam("dishId") int dishId,
                                 @RequestParam("dishType") String dishType,
                                 @AuthenticationPrincipal MemberDetails memberDetails) {

        if (memberDetails == null || memberDetails.getMember() == null) {
            return "redirect:/login";
        }

        Member member = memberDetails.getMember();

        Favorite favorite = new Favorite();
        favorite.setMemberId(member.getId());
        favorite.setDishId(dishId);
        favorite.setDishType(dishType);

        favoriteService.removeFavorite(favorite);
        return "redirect:/myfavorites";
    }
}
