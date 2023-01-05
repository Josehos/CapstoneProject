package com.kenzie.appserver.controller;

import com.kenzie.appserver.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{includeIngredients}")
    public String getRecipesByIngredients(@PathVariable("includeIngredients") List<String> includedIngredients) {

        return recipeService.getRecipesByIngredients(includedIngredients);
    }

    @GetMapping("/{ID}")
    public String getRecipeById(@PathVariable("id") String recipeId) {

        return recipeService.getRecipeById(recipeId);
    }

}
