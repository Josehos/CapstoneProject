import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserClient from "../api/userClient";

let username;

class IndexPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onLoginSubmitButton'], this);
        this.userDataStore = new DataStore();
    }

    async mount() {
        document.getElementById('loginForm').addEventListener('submit', this.onLoginSubmitButton);

        this.userClient = new UserClient();
    }

    // Render Methods ---------------------------------------------------------------------------------------------------

//    async renderRandomRecipes() {
//    }

//    async renderRecipesByIngredients() {
//    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoginSubmitButton(event) {
        event.preventDefault();

//        $('#loginSubmitButton').hide();
//        $('#loginLoadingButton').show();

        await this.userDataStore.set('users', null);

        username = $('#loginUsername').val().toUpperCase();
        let userResults = await this.userClient.getUser(username, this.errorHandler);
//        $('#createUsername').val(username);
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
