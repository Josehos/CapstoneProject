import BaseClass from "../util/baseClass";
import axios from 'axios';

export default class UserClient extends BaseClass {
    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getUser'];
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

    async getUser(username, errorCallback) {
        try {
            const response = await this.client.get(`/user/${username}`);
            // console.log('getUser');
            // console.log(response.data);
            return response.data;
        } catch (error) {
            this.handleError("getUser", error, errorCallback)
        }
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
