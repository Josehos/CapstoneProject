import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserClient from "../api/userClient";

let username;
let restrictionsCreate = [];
// let restrictionsLogin;
let favorites;
let tagsStr;

class IndexPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderRandomRecipes', 'renderRecipesByIngredients', 'onLoginSubmitButton', 'onCreateUserSubmit', 'onCreateUserCancel', 'onEditUserAccountLink', 'onUpdateUserSubmit', 'onUpdateUserCancel', 'onLogoutButton', 'onLogoutCancel', 'onDelete'], this);
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

        this.userClient = new UserClient();
    }

    // Render Methods ---------------------------------------------------------------------------------------------------

    async renderRandomRecipes() {
    }

    async renderRecipesByIngredients() {
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoginSubmitButton(e) {
        e.preventDefault();
        $('#loginSubmitButton').hide();
        $('#loginLoadingButton').show();

        // await this.userDataStore.set('users', null);

        username = $('#loginUsername').val().toUpperCase();
        // try {
        //     let userResults = await this.userClient.getUser(username, this.errorHandler);
        // } catch (e) {
        //     // console.log(e);
        // }
        let userResults = await this.userClient.getUser(username, this.errorHandler);

        if (!userResults) {
            $('#createUsername').val(username);
            $('#createUserModal').modal('show');
        } else {
            $('#welcome').append('Welcome ' + username).show();
            let restrictionsLogin = userResults.dietaryRestrictions;
            console.log('restrictionsLogin: ' + restrictionsLogin);
            // this.randomRecipes();
            let tagsStrLogin = restrictionsLogin.toString();
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
                    const recipe = {};
                    let recipeImageTd = 'No Image';
                    let recipeDishTypesTd = 'None Specified';
                    if (randomRecipesLogin[i].image !== undefined) {
                        recipe.image = randomRecipesLogin[i].image;
                        recipeImageTd = '<img src="' + recipe.image + '" height="50" alt="">';
                    }
                    if (randomRecipesLogin[i].dishTypes.length !== 0) {
                        recipe.dishTypes = randomRecipesLogin[i].dishTypes.join(', ');
                        recipeDishTypesTd = recipe.dishTypes;
                    }
                    recipe.title = randomRecipesLogin[i].title;
                    // recipe.dishTypes = randomRecipes[i].dishTypes.join(', ');
                    recipe.spoonacularSourceUrl = randomRecipesLogin[i].spoonacularSourceUrl;
                    $('#randomRecipesYes').append('<tr id="' + recipe.spoonacularSourceUrl + '" class="randomRecipe"><td>' +
                        recipeImageTd + '</td><td>' + recipe.title + '</td><td>' + recipeDishTypesTd + '</td>></tr>');
                }
                $('.randomRecipe').click(function() {
                    window.open($(this).attr('id'));
                });
                $('#recipesContainer').show();
            }
        }
        $('#loginModal').modal('hide');
    }

    async onCreateUserSubmit(e) {
        e.preventDefault();
        $('#createUserButtons').hide();
        $('#createUsername, #createUsernameSubmitButton').attr('disabled', true);
        for (let i = 0; i < 12; i++) {
            $('#createRestriction' + i).attr('disabled', true);
            if ($('#createRestriction' + i).is(':checked')) {
                restrictionsCreate.push($('#createRestriction' + i).val());
            }
        }
        console.log(restrictionsCreate);
        $('#createUserSubmittingButton').show();
        tagsStr = restrictionsCreate.toString();
        console.log(tagsStr);
        const createdUser = await this.userClient.createUser(username, restrictionsCreate, favorites, this
            .errorHandler);
        $('#welcome').append('Welcome ' + username).show();
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

    async onEditUserAccountLink(e) {
        e.preventDefault();
    }

    async onUpdateUserSubmit(e) {
        e.preventDefault();
    }

    async onUpdateUserCancel() {
    }

    async onLogoutLink(e) {
        // e.preventDefault();
        // location.reload();
    }

    async onLogoutButton() {
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
};

window.addEventListener('DOMContentLoaded', main);
