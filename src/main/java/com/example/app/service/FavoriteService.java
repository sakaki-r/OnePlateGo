package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Favorite;
import com.example.app.mapper.FavoriteMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    public boolean addFavorite(Favorite favorite) {
    	
        // 登録済みでない場合のみ追加
        if (!isFavorited(favorite)) {
            favoriteMapper.insert(favorite);
            return true;
        }
        return false;
       
    }

    public void removeFavorite(Favorite favorite) {
        favoriteMapper.delete(favorite);
    }

    public boolean isFavorited(Favorite favorite) {
    	return favoriteMapper.isFavorited(favorite) > 0;
    }

    public List<Favorite> getFavorites(int memberId) {
        return favoriteMapper.findByMemberId(memberId);
    }
}
