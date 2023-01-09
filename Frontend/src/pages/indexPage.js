import BaseClass from "../util/baseClass";
import UserClient from "../api/userClient";
import RecipeClient from "../api/recipeClient";

let username;
let restrictionsArr = [];
let favoritesArr = [];
let ingredientsCount = 0;
let ingredientsStr = '';
let ingredientsArr = [];
let recipeId;
let recipe;

class IndexPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onLoginSubmitButton', 'onCreateUserSubmit', 'onCreateUserCancel', 'onEditUserAccountLink', 'onUpdateUserSubmit', 'onUpdateUserCancel', 'onLogoutButton', 'onLogoutCancel', 'onDeleteButton', 'onDeleteCancel', 'onFavoritesLink', 'onHomeLink', 'onAddIngredientClick', 'onClearIngredientsClick', 'onSearchRecipesClick'], this);
    }

    async mount() {
        document.getElementById('loginForm').addEventListener('submit', this.onLoginSubmitButton);
        document.getElementById('createUserForm').addEventListener('submit', this.onCreateUserSubmit);
        document.getElementById('createUserCancelButton').addEventListener('click', this.onCreateUserCancel);
        document.getElementById('editUserAccount').addEventListener('click', this.onEditUserAccountLink);
        document.getElementById('updateUserForm').addEventListener('submit', this.onUpdateUserSubmit);
        document.getElementById('updateUserCancelButton').addEventListener('click', this.onUpdateUserCancel);
        document.getElementById('logoutForm').addEventListener('submit', this.onLogoutButton);
        document.getElementById('logoutCancelButton').addEventListener('click', this.onLogoutCancel);
        document.getElementById('deleteForm').addEventListener('submit', this.onDeleteButton);
        document.getElementById('deleteCancelButton').addEventListener('click', this.onDeleteCancel);
        document.getElementById('favorites').addEventListener('click', this.onFavoritesLink);
        document.getElementById('home').addEventListener('click', this.onHomeLink);
        document.getElementById('addIngredient').addEventListener('click', this.onAddIngredientClick);
        document.getElementById('clearIngredients').addEventListener('click', this.onClearIngredientsClick);
        document.getElementById('searchRecipesButton').addEventListener('click', this.onSearchRecipesClick);

        this.userClient = new UserClient();
        this.recipeClient = new RecipeClient();
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onHomeLink() {
        ingredientsCount = 0;
        ingredientsStr = '';
        ingredientsArr = [];

        $('#favoriteRecipes').hide();
        $('#searchRecipes, #searchedRecipes').show();
        $('#ingredient').focus();
    }

    async onAddIngredientClick() {
        if ($('#ingredient').val() === '') return;
        $('#searchRecipesButton').attr('disabled', false);
        if (ingredientsCount === 0) {
            ingredientsStr += ($('#ingredient').val());
        } else {
            ingredientsStr += (',' + $('#ingredient').val());
        }
        ingredientsArr.push($('#ingredient').val());
        ingredientsCount++;
        $('#ingredients').val(ingredientsStr);
        console.log('ingredientsStrAdded: ' + ingredientsStr);
        console.log('ingredientsArrAdded: ' + ingredientsArr);
        $('#ingredient').val('');
        $('#ingredient').focus();
    }

    async onClearIngredientsClick() {
        ingredientsStr = '';
        ingredientsCount = 0;
        ingredientsArr = [];

        $('#ingredients').val(ingredientsStr);
        $('#searchRecipesButton').attr('disabled', true);
        $('#ingredient').focus();
        console.log('ingredientsStrClear: ' + ingredientsStr);
        console.log('ingredientsArrClear: ' + ingredientsArr);
    }

    async onSearchRecipesClick(e) {
        e.preventDefault();
        $('#searchLoadingButton').show();
        $('#recipeWithIngredients').hide();
        $('#searchedRecipes').hide();
        $('#ingredient, #clearIngredients, #addIngredient').attr('disabled', true);
        console.log('restrictionsArrSearch: ' + restrictionsArr);
        console.log('ingredientsArrSearch: ' + ingredientsArr);

        let recipeResults = await this.recipeClient.getRecipesByIngredients(ingredientsArr, restrictionsArr, this.errorHandler);
        if (recipeResults.results.length > 0) {
            $('#searchedRecipesNo').hide();
            $('#searchedRecipesYes').html('');
            for (let i = 0; i < recipeResults.results.length; i++) {
                let recipeSearch = {};
                let recipeImageTdSearch = 'No Image';
                let recipeFavoritesTdLogin = '<i class="fav bi-heart"></i>';
                if (recipeResults.results[i].image !== undefined) {
                    recipeSearch.image = recipeResults.results[i].image;
                    recipeImageTdSearch = '<img src="' + recipeSearch.image + '" height="50" alt="">';
                }
                recipeSearch.title = recipeResults.results[i].title;
                recipeSearch.id = recipeResults.results[i].id;

                let recipeResult = await this.recipeClient.getRecipeById(recipeSearch.id, this.errorHandler);
                recipeSearch.spoonacularSourceUrl = recipeResult.spoonacularSourceUrl;
                $('#searchedRecipesYes').append('<tr><td>' +
                    recipeImageTdSearch + '</td><td id="' + recipeSearch.spoonacularSourceUrl + '" class="randomRecipe" style="cursor: pointer;">' + recipeSearch.title + '</td><td>' + recipeFavoritesTdLogin + '</td></tr>');
            }
        }
        $('.randomRecipe').click(function () {
            window.open($(this).attr('id'));
        });
        $('#searchedRecipes').show();
        $('#recipeWithIngredients').show();
        $('#searchLoadingButton').hide();
        $('#ingredient, #clearIngredients, #addIngredient').attr('disabled', false);
    }

    async onFavoritesLink() {
        $('#searchRecipes, #searchedRecipes').hide();
        $('#favoriteRecipes').show();
    }

    async onLoginSubmitButton(e) {
        e.preventDefault();
        username = $('#loginUsername').val().toUpperCase();
        let userResults = await this.userClient.getUser(username, this.errorHandler);

        $('#loginSubmitButton').hide();
        $('#loginLoadingButton').show();

        if (!userResults) {
            $('#createUsername').val(username);
            $('#createUserModal').modal('show');
        } else {
            restrictionsArr = userResults.dietaryRestrictions;
            favoritesArr = userResults.favoriteRecipes;
            console.log('restrictionsArrLogin: ' + restrictionsArr);
            console.log('favoritesArrLogin: ' + favoritesArr);
            $('#welcome').append('Welcome ' + username).show();
            $('#recipesContainer').show();
        }
        $('#loginModal').modal('hide');
        $('#ingredient').focus();
    }

    async onCreateUserSubmit(e) {
        e.preventDefault();
        $('#createUserButtons').hide();
        $('#createUserSubmittingButton').show();
        favoritesArr = ['testTitle1@testDishType1', 'testTitle2@testDishType2'];
        for (let i = 0; i < 11; i++) {
            $('#createRestriction' + i).attr('disabled', true);
            if ($('#createRestriction' + i).is(':checked')) {
                restrictionsArr.push($('#createRestriction' + i).val());
            }
        }
        console.log('restrictionsArrCreate: ' + restrictionsArr);
        console.log('favoritesArrCreate: ' + favoritesArr);

        const createdUser = await this.userClient.createUser(username, restrictionsArr, favoritesArr, this
            .errorHandler);

        $('#welcome').append('Welcome ' + username).show();
        $('#recipesContainer').show();
        $('#createUserModal').modal('hide');
    }

    async onCreateUserCancel() {
        $('#createUserSubmittingButton').hide();
        $('#createUserButtons').show();
        $('#createUserModal').modal('hide');
        $('#loginUsername').val('');
        $('#loginSubmitButton').show();
        $('#loginLoadingButton').hide();
        $('#loginModal').modal('show');
    }

    async onEditUserAccountLink() {
        for (let i = 0; i < 11; i++) {
            $('#updateRestriction' + i).prop('checked', false).attr('disabled', false);
            for (let j = 0; j < restrictionsArr.length; j++) {
                if ($('#updateRestriction' + i).val() == restrictionsArr[j]) {
                    $('#updateRestriction' + i).prop('checked', true);
                }
            }
        }
        $('#updateUserSubmittingButton').hide();
        $('#updateUserButtons').show();
        $('#updateUserModal').modal('show');
    }

    async onUpdateUserSubmit(e) {
        e.preventDefault();
        $('#updateUserButtons').hide();
        // ingredientsCount = 0;
        // ingredientsStr = '';
        // ingredientsArr = [];
        $('#clearIngredients').click();
        $('#searchedRecipes').hide();
        restrictionsArr = [];
        for (let i = 0; i < 11; i++) {
            $('#updateRestriction' + i).attr('disabled', true);
            if ($('#updateRestriction' + i).is(':checked')) {
                restrictionsArr.push($('#updateRestriction' + i).val());
            }
        }
        console.log('restrictionsArrUpdate: ' + restrictionsArr);
        $('#updateUserSubmittingButton').show();
        const updateUser = await this.userClient.updateUser(username, restrictionsArr, favoritesArr, this
            .errorHandler);

        $('#recipesContainer').show();
        $('#updateUserModal').modal('hide');
    }

    async onUpdateUserCancel() {
        $('#updateUserModal').modal('hide');
    }

    async onLogoutButton(e) {
        e.preventDefault();
        location.reload();
    }

    async onLogoutCancel() {
        $('#logoutModal').modal('hide');
    }

    async onDeleteButton(e) {
        e.preventDefault();
        await this.userClient.deleteUser(username, this.errorHandler);
        location.reload();
    }

    async onDeleteCancel() {
        $('#deleteModal').modal('hide');
    }
}

const main = async () => {
    const indexPage = new IndexPage();
    indexPage.mount();

    $('#loginModal').modal('show');
    $('#loginUsername').focus();
    // prevent spaces in username
    $('#loginUsername').on('keypress', function(e) {
        if (e.which == 32) {
            return false;
        }
    });

    $('#searchRecipesButton').attr('disabled', true);

    // $('#home').click(function() {
    //     $('#favoriteRecipes').hide();
    //     $('#searchRecipes').show();
    // });
    // $('.bi-heart').click(function() {
    //     // window.open($(this).attr('id'));
    //     alert('hi');
    //     $(this).addClass('bi-heart-fill')
    // });
    // ingredientsCount = 0;
    // let ingredientsStr = '';
    // $('#addIngredient').click(function(e) {
    //     // e.preventDefault();
    //     if ($('#ingredient').val() == '') return;
    //     if (ingredientsCount == 0) {
    //         // $('#ingredients').append($('#ingredient').val());
    //         ingredientsStr += ($('#ingredient').val());
    //     } else {
    //         // $('#ingredients').append(', ' + $('#ingredient').val());
    //         ingredientsStr += (',' + $('#ingredient').val());
    //     }
    //     ingredientsCount++;
    //     $('#ingredients').val(ingredientsStr);
    //     console.log('ingredientsStrAdded: ' + ingredientsStr);
    //     $('#ingredient').val('');
    //     $('#ingredient').focus();
    // });
    // $('#clearIngredients').click(function() {
    //     ingredientsStr = '';
    //     $('#ingredients').val(ingredientsStr);
    //     console.log('ingredientsStrClear: ' + ingredientsStr);
    // });
    // $('#searchRecipesButton').click(function() {
    //     $('#searchedRecipes').show();
    // })
};

window.addEventListener('DOMContentLoaded', main);
