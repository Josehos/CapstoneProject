import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserClient from "../api/userClient";
import RecipeClient from "../api/recipeClient";

let username;
// let restrictionsCreate = [];
let restrictionsArr = [];
let favoritesArr = [];
// let tagsStr;

class IndexPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderRandomRecipes', 'renderRecipesByIngredients', 'onLoginSubmitButton', 'onCreateUserSubmit', 'onCreateUserCancel', 'onEditUserAccountLink', 'onUpdateUserSubmit', 'onUpdateUserCancel', 'onLogoutButton', 'onLogoutCancel', 'onDelete', 'onGetRecipeById', 'onGetRecipesByIngredients', 'onFavoritesLink'], this);
        this.userDataStore = new DataStore();
    }

    async mount() {
        document.getElementById('loginForm').addEventListener('submit', this.onLoginSubmitButton);
        document.getElementById('createUserForm').addEventListener('submit', this.onCreateUserSubmit);
        document.getElementById('createUserCancelButton').addEventListener('click', this.onCreateUserCancel);
        document.getElementById('editUserAccount').addEventListener('click', this.onEditUserAccountLink);
        document.getElementById('updateUserForm').addEventListener('submit', this.onUpdateUserSubmit);
        document.getElementById('updateUserCancelButton').addEventListener('click', this.onUpdateUserCancel);
        document.getElementById('logoutForm').addEventListener('submit', this.onLogoutButton);
        // document.getElementById('logoutButton').addEventListener('click', this.onLogoutButton);
        document.getElementById('logoutCancelButton').addEventListener('click', this.onLogoutCancel);
        document.getElementById('deleteUserAccount').addEventListener('click', this.onDelete);
        document.getElementById('getRecipeById').addEventListener('click', this.onGetRecipeById);
        document.getElementById('getRecipesByIngredients').addEventListener('click', this.onGetRecipesByIngredients);
        document.getElementById('favorites').addEventListener('click', this.onFavoritesLink);

        this.userClient = new UserClient();
        this.recipeClient = new RecipeClient();
    }

    async onFavoritesLink() {



        $('#randomRecipes').hide();
        $('#favoriteRecipes').show();
    }

    async onGetRecipeById() {
        let recipeResults = await this.recipeClient.getRecipeById('716429', this.errorHandler);
    }

    async onGetRecipesByIngredients() {
        let recipeResults = await this.recipeClient.getRecipesByIngredients(["onions", "tomatoes"], ["dairy"], this.errorHandler);
    }
    // Render Methods ---------------------------------------------------------------------------------------------------

    async renderRandomRecipes() {
    }

    async renderRecipesByIngredients() {
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

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
            console.log('restrictionsArr: ' + restrictionsArr);
            console.log('favoritesArr: ' + favoritesArr);

            let tagsStrLogin = restrictionsArr.toString();
            console.log('tagsStrLogin: ' + tagsStrLogin);
            let randomRecipesLogin;
            $.ajax({
                url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrLogin + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
                type: "GET",
                async: false,
                headers: {
                    "accept": "application/json;odata=verbose"
                },
                success: function (data) {
                    randomRecipesLogin = data.recipes;
                    console.log(data);
                },
                error: function (error) {
                    alert(JSON.stringify(error));
                }
            });
            if (randomRecipesLogin.length > 0) {
                $('#randomRecipesNo').hide();
                for (let i = 0; i < randomRecipesLogin.length; i++) {
                    let recipeLogin = {};
                    let recipeImageTdLogin = 'No Image';
                    let recipeDishTypesTdLogin = 'None Specified';
                    let recipeFavoritesTdLogin ='<i class="fav bi-heart"></i>';
                    if (randomRecipesLogin[i].image !== undefined) {
                        recipeLogin.image = randomRecipesLogin[i].image;
                        recipeImageTdLogin = '<img src="' + recipeLogin.image + '" height="50" alt="">';
                    }
                    if (randomRecipesLogin[i].dishTypes.length !== 0) {
                        recipeLogin.dishTypes = randomRecipesLogin[i].dishTypes.join(', ');
                        recipeDishTypesTdLogin = recipeLogin.dishTypes;
                    }

                    recipeLogin.title = randomRecipesLogin[i].title;
                    // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
                    recipeLogin.spoonacularSourceUrl = randomRecipesLogin[i].spoonacularSourceUrl;
                    $('#randomRecipesYes').append('<tr><td>' +
                        recipeImageTdLogin + '</td><td id="' + recipeLogin.spoonacularSourceUrl + '" class="randomRecipe" style="cursor: pointer;">' + recipeLogin.title + '</td><td>' + recipeDishTypesTdLogin + '</td><td>' + recipeFavoritesTdLogin + '</td></tr>');
                }
                $('.randomRecipe').click(function() {
                    window.open($(this).attr('id'));
                });
                $('.fav').click(function() {
                    // window.open($(this).attr('id'));
                    // alert('hi');
                    if ($(this).hasClass('bi-heart')) {
                        $(this).removeClass('bi-heart').addClass('bi-heart-fill');
                    } else {
                        $(this).removeClass('bi-heart-fill').addClass('bi-heart');
                    }
                    // $(this).toggleClass('bi-heart-fill');
                });
                $('#recipesContainer').show();
            }
            $('#welcome').append('Welcome ' + username).show();
        }
        $('#loginModal').modal('hide');
    }

    async onCreateUserSubmit(e) {
        e.preventDefault();
        $('#createUserButtons').hide();
        $('#createUsername, #createUsernameSubmitButton').attr('disabled', true);
        // let restrictionsCreate = [];
        let favoritesCreate = ['testTitle1@testDishType1', 'testTitle2@testDishType2'];
        for (let i = 0; i < 12; i++) {
            $('#createRestriction' + i).attr('disabled', true);
            if ($('#createRestriction' + i).is(':checked')) {
                restrictionsArr.push($('#createRestriction' + i).val());
            }
        }
        console.log('restrictionsArr: ' + restrictionsArr);
        $('#createUserSubmittingButton').show();
        const createdUser = await this.userClient.createUser(username, restrictionsArr, favoritesCreate, this
            .errorHandler);
        // let favStrings = createdUser.favoriteRecipes[0].split('@');
        // console.log(favStrings);
        $('#welcome').append('Welcome ' + username).show();
        // let restrictionsCreate = createdUser.dietaryRestrictions;
        // console.log('restrictionsCreate: ' + restrictionsCreate);
        // this.randomRecipes();
        let tagsStrCreate = restrictionsArr.toString();
        console.log('tagsStrCreate: ' + tagsStrCreate);
        let randomRecipesCreate;
        $.ajax({
            url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrCreate + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
            type: "GET",
            async: false,
            headers: {
                "accept": "application/json;odata=verbose"
            },
            success: function (data) {
                randomRecipesCreate = data.recipes;
                console.log('randomRecipesCreate: ' + data);
            },
            error: function (error) {
                alert(JSON.stringify(error));
            }
        });
        if (randomRecipesCreate.length > 0) {
            $('#randomRecipesNo').hide();
            for (let i = 0; i < randomRecipesCreate.length; i++) {
                let recipeCreate = {};
                let recipeImageTdCreate = 'No Image';
                let recipeDishTypesTdCreate = 'None Specified';
                if (randomRecipesCreate[i].image !== undefined) {
                    recipeCreate.image = randomRecipesCreate[i].image;
                    recipeImageTdCreate = '<img src="' + recipeCreate.image + '" height="50" alt="">';
                }
                if (randomRecipesCreate[i].dishTypes.length !== 0) {
                    recipeCreate.dishTypes = randomRecipesCreate[i].dishTypes.join(', ');
                    recipeDishTypesTdCreate = recipeCreate.dishTypes;
                }
                recipeCreate.title = randomRecipesCreate[i].title;
                // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
                recipeCreate.spoonacularSourceUrl = randomRecipesCreate[i].spoonacularSourceUrl;
                $('#randomRecipesYes').append('<tr id="' + recipeCreate.spoonacularSourceUrl + '" class="randomRecipe"><td>' +
                    recipeImageTdCreate + '</td><td>' + recipeCreate.title + '</td><td>' + recipeDishTypesTdCreate + '</td></tr>');
            }
            $('.randomRecipe').click(function() {
                window.open($(this).attr('id'));
            });
            $('#recipesContainer').show();
        }
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
        console.log('restrictionsArr: ' + restrictionsArr);
        for (let i = 0; i < 12; i++) {
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
        // $('#createUsername, #createUsernameSubmitButton').attr('disabled', true);
        // let restrictionsCreate = [];
        // let favoritesCreate = ['testTitle1@testDishType1', 'testTitle2@testDishType2'];
        restrictionsArr = [];
        for (let i = 0; i < 12; i++) {
            $('#updateRestriction' + i).attr('disabled', true);
            if ($('#updateRestriction' + i).is(':checked')) {
                restrictionsArr.push($('#updateRestriction' + i).val());
            }
        }
        console.log('restrictionsArr: ' + restrictionsArr);
        $('#updateUserSubmittingButton').show();
        const updateUser = await this.userClient.updateUser(username, restrictionsArr, favoritesArr, this
            .errorHandler);
        // let favStrings = createdUser.favoriteRecipes[0].split('@');
        // console.log(favStrings);
        // $('#welcome').append('Welcome ' + username).show();
        // let restrictionsCreate = createdUser.dietaryRestrictions;
        // console.log('restrictionsCreate: ' + restrictionsCreate);
        // this.randomRecipes();
        let tagsStrUpdate = restrictionsArr.toString();
        console.log('tagsStrUpdate: ' + tagsStrUpdate);
        let randomRecipesUpdate;
        $.ajax({
            url: 'https://api.spoonacular.com/recipes/random?number=5&tags=' + tagsStrUpdate + '&apiKey=24c9edcac34c4beeab2ec786f687f711',
            type: "GET",
            async: false,
            headers: {
                "accept": "application/json;odata=verbose"
            },
            success: function (data) {
                randomRecipesUpdate = data.recipes;
                console.log('randomRecipesUpdate: ' + data.recipes.length);
            },
            error: function (error) {
                alert(JSON.stringify(error));
            }
        });
        if (randomRecipesUpdate.length > 0) {
            $('#randomRecipesNo').hide();
            $('#randomRecipesYes').html(' ');
            for (let i = 0; i < randomRecipesUpdate.length; i++) {
                let recipeUpdate = {};
                let recipeImageTdUpdate = 'No Image';
                let recipeDishTypesTdUpdate = 'None Specified';
                let recipeFavoritesTdUpdate ='<i class="bi-heart"></i>';
                if (randomRecipesUpdate[i].image !== undefined) {
                    recipeUpdate.image = randomRecipesUpdate[i].image;
                    recipeImageTdUpdate = '<img src="' + recipeUpdate.image + '" height="50" alt="">';
                }
                if (randomRecipesUpdate[i].dishTypes.length !== 0) {
                    recipeUpdate.dishTypes = randomRecipesUpdate[i].dishTypes.join(', ');
                    recipeDishTypesTdUpdate = recipeUpdate.dishTypes;
                }
                recipeUpdate.title = randomRecipesUpdate[i].title;
                // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
                recipeUpdate.spoonacularSourceUrl = randomRecipesUpdate[i].spoonacularSourceUrl;
                $('#randomRecipesYes').append('<tr><td>' +
                    recipeImageTdUpdate + '</td><td id="' + recipeUpdate.spoonacularSourceUrl + '" class="randomRecipe" style="cursor: pointer;">' + recipeUpdate.title + '</td><td>' + recipeDishTypesTdUpdate + '</td><td>' + recipeFavoritesTdUpdate + '</td></tr>');
            }
            $('.randomRecipe').click(function() {
                window.open($(this).attr('id'));
            });
            $('#recipesContainer').show();
        }
        $('#updateUserModal').modal('hide');
    }

    async onUpdateUserCancel() {
        $('#updateUserModal').modal('hide');
    }

    async onLogoutLink(e) {
        // e.preventDefault();
        // location.reload();
    }

    async onLogoutButton(e) {
        e.preventDefault();
        location.reload();
    }

    async onLogoutCancel() {
        $('#logoutModal').modal('hide');
    }

    async randomRecipes() {
        alert('hi');
    }

    async getRestrictions() {

    }

    async onDelete() {
        await this.userClient.deleteUser(username, this.errorHandler);
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
    $('#home').click(function() {
        $('#favoriteRecipes').hide();
        $('#randomRecipes').show();
    });
    // $('.bi-heart').click(function() {
    //     // window.open($(this).attr('id'));
    //     alert('hi');
    //     $(this).addClass('bi-heart-fill')
    // });
};

window.addEventListener('DOMContentLoaded', main);
