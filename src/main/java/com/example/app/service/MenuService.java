package com.example.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.app.domain.Meal;
import com.example.app.domain.Menu;
import com.example.app.mapper.MealMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MealMapper mealMapper;
    
     // 初回の献立生成
    public List<Meal> getMeals(Menu req) {
        List<Meal> result = new ArrayList<>();

        List<Meal> meats = mealMapper.findMeat(req.getMeatCount());
        meats.forEach(m -> {
            m.setCategory(1);
            m.setIngredients(mealMapper.findMeatIngredients(m.getId()));
            m.setImagePath("/images/meals/" + convertToFileName(m.getMaindish()) + ".png");

        });

        List<Meal> fishes = mealMapper.findFish(req.getFishCount());
        fishes.forEach(m -> {
            m.setCategory(2);
            m.setIngredients(mealMapper.findFishIngredients(m.getId()));
            m.setImagePath("/images/meals/" + convertToFileName(m.getMaindish()) + ".png");
        });

        List<Meal> others = mealMapper.findOther(req.getOtherCount());
        others.forEach(m -> {
            m.setCategory(3);
            m.setIngredients(mealMapper.findOtherIngredients(m.getId()));
            m.setImagePath("/images/meals/" + convertToFileName(m.getMaindish()) + ".png");
        });

        result.addAll(meats);
        result.addAll(fishes);
        result.addAll(others);
        return result;
    }

    // 材料（人数×）で集約
    public List<String> getIngredientsWithCount(List<Meal> meals, int people) {
        Map<String, Integer> countMap = new LinkedHashMap<>();

        for (Meal meal : meals) {
            if (meal.getIngredients() == null) continue;
            for (String item : meal.getIngredients()) {
                countMap.put(item, countMap.getOrDefault(item, 0) + people);
            }
        }

        return countMap.entrySet().stream()
                .map(e -> e.getKey() + " × " + e.getValue())
                .collect(Collectors.toList());
    }

    // 再作成ロジック（カテゴリ付きID対応）
    public List<Meal> reselectMealsWithCategory(List<String> keepIds, Menu req) {
        List<Meal> result = new ArrayList<>();
        Set<String> existingKeys = new HashSet<>();

        for (String key : keepIds) {
            String[] parts = key.split("-");
            if (parts.length != 2) continue;

            int category = Integer.parseInt(parts[0]);
            int id = Integer.parseInt(parts[1]);
            Meal m = null;

            if (category == 1) m = mealMapper.findMeatById(id);
            else if (category == 2) m = mealMapper.findFishById(id);
            else if (category == 3) m = mealMapper.findOtherById(id);

            if (m != null) {
                m.setCategory(category);
                switch (category) {
                    case 1 -> m.setIngredients(mealMapper.findMeatIngredients(id));
                    case 2 -> m.setIngredients(mealMapper.findFishIngredients(id));
                    case 3 -> m.setIngredients(mealMapper.findOtherIngredients(id));
                }
                m.setImagePath("/images/meals/" + convertToFileName(m.getMaindish()) + ".png");
                result.add(m);
                existingKeys.add(key);
            }
        }

        // 不足分を埋める（重複しないように）
        result.addAll(getRemainingMeals(mealMapper.findMeat(10), 1, req.getMeatCount(), result, existingKeys));
        result.addAll(getRemainingMeals(mealMapper.findFish(10), 2, req.getFishCount(), result, existingKeys));
        result.addAll(getRemainingMeals(mealMapper.findOther(10), 3, req.getOtherCount(), result, existingKeys));

        return result;
    }

    private List<Meal> getRemainingMeals(List<Meal> candidates, int category, int requiredCount,
                                         List<Meal> currentMeals, Set<String> existingKeys) {
        long currentCount = currentMeals.stream().filter(m -> m.getCategory() == category).count();
        int remaining = Math.max(0, requiredCount - (int) currentCount);

        return candidates.stream()
                .filter(m -> !existingKeys.contains(category + "-" + m.getId()))
                .limit(remaining)
                .peek(m -> {
                    m.setCategory(category);
                    switch (category) {
                        case 1 -> m.setIngredients(mealMapper.findMeatIngredients(m.getId()));
                        case 2 -> m.setIngredients(mealMapper.findFishIngredients(m.getId()));
                        case 3 -> m.setIngredients(mealMapper.findOtherIngredients(m.getId()));
                    }
                    m.setImagePath("/images/meals/" + convertToFileName(m.getMaindish()) + ".png");
                })
                .collect(Collectors.toList());
    }
    
    private String convertToFileName(String maindish) {
        Map<String, String> nameToFile = Map.ofEntries(
            Map.entry("親子丼", "oyakodon"),
            Map.entry("カツ丼", "katsudon"),
            Map.entry("牛丼", "gyudon"),
            Map.entry("チャーハン", "chahan"),
            Map.entry("手巻き寿司", "temaki"),
            Map.entry("餃子", "gyoza"),
            Map.entry("ラーメン", "ramen"),
            Map.entry("皿うどん", "saraudon"),
            Map.entry("酢豚", "subuta"),
            Map.entry("焼きそば", "yakisoba"),
            Map.entry("鯛のホイル焼き", "tai-foil"),
            Map.entry("鯛の塩焼き", "tai-shioyaki"),
            Map.entry("アジフライ", "ajifurai"),
            Map.entry("刺身盛り合わせ", "sashimi"),
            Map.entry("サバの味噌煮", "saba-misoni"),
            Map.entry("アジの開き", "aji-hiraki"),
            Map.entry("カツオのたたき", "katsuo-tataki"),
            Map.entry("ブリの照り焼き", "buri-teriyaki"),
            Map.entry("サバみりん干し", "saba-mirin"),
            Map.entry("イワシ梅煮", "iwashi-umeni"),
            Map.entry("ステーキ", "steak"),
            Map.entry("豚カツ", "tonkatsu"),
            Map.entry("鶏のトマト煮", "chicken-tomato"),
            Map.entry("肉じゃが", "nikujaga"),
            Map.entry("ビーフシチュー", "beef-stew"),
            Map.entry("ハンバーグ", "hamburg"),
            Map.entry("生姜焼き", "shogayaki"),
            Map.entry("ポークチャップ", "pork-chop"),
            Map.entry("ピーマン肉詰め", "stuffed-pepper"),
            Map.entry("鶏の唐揚げ", "karage")
        );

        return nameToFile.getOrDefault(maindish, "default");
    }
}
