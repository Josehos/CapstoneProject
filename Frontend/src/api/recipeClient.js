import BaseClass from "../util/baseClass";
import axios from 'axios';

export default class RecipeClient extends BaseClass {
    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getRecipeById', 'getRecipesByIngredients'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getRecipeById(recipeId, errorCallback) {
        try {
            const response = await this.client.get(`/recipes/byId/${recipeId}`);
            console.log('getRecipeById');
            console.log(response.data);
            return response.data;
        } catch (error) {
            // this.handleError("getUser", error, errorCallback);
            console.log('recipe not found');
        }
        return null;
    }

    async getRecipesByIngredients(includedIngredients, errorCallback) {
        try {
            const response = await this.client.get(`/recipes/${includedIngredients}`);
            console.log('getRecipesByIngredients');
            console.log(response.data);
            return response.data;
        } catch (error) {
            // this.handleError("getUser", error, errorCallback);
            console.log('recipes not found');
        }
        return null;
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
