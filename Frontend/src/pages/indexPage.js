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

    // async onGetRecipeById(id) {
    //     recipe = await this.recipeClient.getRecipeById(id, this.errorHandler);
    // }

    // async onGetRecipesByIngredients() {
    //     let recipeResults = await this.recipeClient.getRecipesByIngredients(["carrot,beef"], ["wheat,egg,dairy"], this.errorHandler);
    // }
    // Render Methods ---------------------------------------------------------------------------------------------------

    // async renderRandomRecipes() {
    // }
    //
    // async renderRecipesByIngredients() {
    // }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onHomeLink() {
        $('#favoriteRecipes').hide();
        $('#searchRecipes, #searchedRecipes').show();
        $('#ingredient').focus();

        ingredientsCount = 0;
        ingredientsStr = '';
        ingredientsArr = [];
    }

    async onAddIngredientClick() {
        if ($('#ingredient').val() === '') return;
        $('#searchRecipesButton').attr('disabled', false);
        if (ingredientsCount === 0) {
            // $('#ingredients').append($('#ingredient').val());
            ingredientsStr += ($('#ingredient').val());
        } else {
            // $('#ingredients').append(', ' + $('#ingredient').val());
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

        // $('#searchRecipesButton').hide();
        $('#searchLoadingButton').show();
        $('#recipeWithIngredients').hide();
        $('#searchedRecipes').hide();
        $('#ingredient, #clearIngredients, #addIngredient').attr('disabled', true);
        // console.log('ingredientsStrSearch: ' + ingredientsStr);
        console.log('restrictionsArrSearch: ' + restrictionsArr);
        console.log('ingredientsArrSearch: ' + ingredientsArr);

        let recipeResults = await this.recipeClient.getRecipesByIngredients(ingredientsArr, restrictionsArr, this.errorHandler);
        // console.log('recipeResults: ' + recipeResults.results.length);
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
        // $('#searchRecipesButton').show();
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
        // await this.userDataStore.set('users', null);
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

            // let tagsStrLogin = restrictionsArr.toString();
            // console.log('tagsStrLogin: ' + tagsStrLogin);
            // let randomRecipesLogin;
            // $.ajax({
            //     url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrLogin + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
            //     type: "GET",
            //     async: false,
            //     headers: {
            //         "accept": "application/json;odata=verbose"
            //     },
            //     success: function (data) {
            //         randomRecipesLogin = data.recipes;
            //         console.log(data);
            //     },
            //     error: function (error) {
            //         alert(JSON.stringify(error));
            //     }
            // });
            // if (randomRecipesLogin.length > 0) {
            //     $('#randomRecipesNo').hide();
            //     for (let i = 0; i < randomRecipesLogin.length; i++) {
            //         let recipeLogin = {};
            //         let recipeImageTdLogin = 'No Image';
            //         let recipeDishTypesTdLogin = 'None Specified';
            //         let recipeFavoritesTdLogin ='<i class="fav bi-heart"></i>';
            //         if (randomRecipesLogin[i].image !== undefined) {
            //             recipeLogin.image = randomRecipesLogin[i].image;
            //             recipeImageTdLogin = '<img src="' + recipeLogin.image + '" height="50" alt="">';
            //         }
            //         if (randomRecipesLogin[i].dishTypes.length !== 0) {
            //             recipeLogin.dishTypes = randomRecipesLogin[i].dishTypes.join(', ');
            //             recipeDishTypesTdLogin = recipeLogin.dishTypes;
            //         }
            //
            //         recipeLogin.title = randomRecipesLogin[i].title;
            //         // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
            //         recipeLogin.spoonacularSourceUrl = randomRecipesLogin[i].spoonacularSourceUrl;
            //         $('#randomRecipesYes').append('<tr><td>' +
            //             recipeImageTdLogin + '</td><td id="' + recipeLogin.spoonacularSourceUrl + '" class="randomRecipe" style="cursor: pointer;">' + recipeLogin.title + '</td><td>' + recipeDishTypesTdLogin + '</td><td>' + recipeFavoritesTdLogin + '</td></tr>');
            //     }
            //     $('.randomRecipe').click(function() {
            //         window.open($(this).attr('id'));
            //     });
            //     $('.fav').click(function() {
            //         // window.open($(this).attr('id'));
            //         // alert('hi');
            //         if ($(this).hasClass('bi-heart')) {
            //             $(this).removeClass('bi-heart').addClass('bi-heart-fill');
            //         } else {
            //             $(this).removeClass('bi-heart-fill').addClass('bi-heart');
            //         }
            //         // $(this).toggleClass('bi-heart-fill');
            //     });
            //     $('#recipesContainer').show();
            // }
            $('#welcome').append('Welcome ' + username).show();
            $('#recipesContainer').show();
        }
        $('#loginModal').modal('hide');
        $('#ingredient').focus();
    }

    async onCreateUserSubmit(e) {
        e.preventDefault();
        $('#createUserButtons').hide();
        // $('#createUsername, #createUsernameSubmitButton').attr('disabled', true);
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

        // let tagsStrCreate = restrictionsArr.toString();
        // console.log('tagsStrCreate: ' + tagsStrCreate);
        // let randomRecipesCreate;
        // $.ajax({
        //     url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrCreate + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
        //     type: "GET",
        //     async: false,
        //     headers: {
        //         "accept": "application/json;odata=verbose"
        //     },
        //     success: function (data) {
        //         randomRecipesCreate = data.recipes;
        //         console.log('randomRecipesCreate: ' + data);
        //     },
        //     error: function (error) {
        //         alert(JSON.stringify(error));
        //     }
        // });
        // if (randomRecipesCreate.length > 0) {
        //     $('#randomRecipesNo').hide();
        //     for (let i = 0; i < randomRecipesCreate.length; i++) {
        //         let recipeCreate = {};
        //         let recipeImageTdCreate = 'No Image';
        //         let recipeDishTypesTdCreate = 'None Specified';
        //         if (randomRecipesCreate[i].image !== undefined) {
        //             recipeCreate.image = randomRecipesCreate[i].image;
        //             recipeImageTdCreate = '<img src="' + recipeCreate.image + '" height="50" alt="">';
        //         }
        //         if (randomRecipesCreate[i].dishTypes.length !== 0) {
        //             recipeCreate.dishTypes = randomRecipesCreate[i].dishTypes.join(', ');
        //             recipeDishTypesTdCreate = recipeCreate.dishTypes;
        //         }
        //         recipeCreate.title = randomRecipesCreate[i].title;
        //         // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
        //         recipeCreate.spoonacularSourceUrl = randomRecipesCreate[i].spoonacularSourceUrl;
        //         $('#randomRecipesYes').append('<tr id="' + recipeCreate.spoonacularSourceUrl + '" class="randomRecipe"><td>' +
        //             recipeImageTdCreate + '</td><td>' + recipeCreate.title + '</td><td>' + recipeDishTypesTdCreate + '</td></tr>');
        //     }
        //     $('.randomRecipe').click(function() {
        //         window.open($(this).attr('id'));
        //     });
        // }
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

        // let tagsStrUpdate = restrictionsArr.toString();
        // console.log('tagsStrUpdate: ' + tagsStrUpdate);
        // let randomRecipesUpdate;
        // $.ajax({
        //     url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrUpdate + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
        //     type: "GET",
        //     async: false,
        //     headers: {
        //         "accept": "application/json;odata=verbose"
        //     },
        //     success: function (data) {
        //         randomRecipesUpdate = data.recipes;
        //         console.log('randomRecipesUpdate: ' + data.recipes.length);
        //     },
        //     error: function (error) {
        //         alert(JSON.stringify(error));
        //     }
        // });
        // if (randomRecipesUpdate.length > 0) {
        //     $('#randomRecipesNo').hide();
        //     $('#randomRecipesYes').html(' ');
        //     for (let i = 0; i < randomRecipesUpdate.length; i++) {
        //         let recipeUpdate = {};
        //         let recipeImageTdUpdate = 'No Image';
        //         let recipeDishTypesTdUpdate = 'None Specified';
        //         let recipeFavoritesTdUpdate ='<i class="bi-heart"></i>';
        //         if (randomRecipesUpdate[i].image !== undefined) {
        //             recipeUpdate.image = randomRecipesUpdate[i].image;
        //             recipeImageTdUpdate = '<img src="' + recipeUpdate.image + '" height="50" alt="">';
        //         }
        //         if (randomRecipesUpdate[i].dishTypes.length !== 0) {
        //             recipeUpdate.dishTypes = randomRecipesUpdate[i].dishTypes.join(', ');
        //             recipeDishTypesTdUpdate = recipeUpdate.dishTypes;
        //         }
        //         recipeUpdate.title = randomRecipesUpdate[i].title;
        //         // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
        //         recipeUpdate.spoonacularSourceUrl = randomRecipesUpdate[i].spoonacularSourceUrl;
        //         $('#randomRecipesYes').append('<tr><td>' +
        //             recipeImageTdUpdate + '</td><td id="' + recipeUpdate.spoonacularSourceUrl + '" class="randomRecipe" style="cursor: pointer;">' + recipeUpdate.title + '</td><td>' + recipeDishTypesTdUpdate + '</td><td>' + recipeFavoritesTdUpdate + '</td></tr>');
        //     }
        //     $('.randomRecipe').click(function() {
        //         window.open($(this).attr('id'));
        //     });
        //
        // }
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

    // console.log('ingredientsVal: ' + $('#ingredients').val());
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
