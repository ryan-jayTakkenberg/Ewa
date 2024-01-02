import User from "@/models/user";
import {deleteAPI, getAPI, postAPI, putAPI, responseOk} from "@/backend";
import {classToObject} from "@/models/helper";

export class UserAdaptor {
    static ENDPOINT = "/api/users";

    async fetchAll() {
        try {
            const response = await getAPI(UserAdaptor.ENDPOINT);
            // console.log('Users Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }

            return response.data.map(data => new User(data.id, data.email, data.name, data.permissionLevel, data.lastLogin))

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }

    async asyncCreate(user) {
        try {
            const response = await postAPI(UserAdaptor.ENDPOINT, classToObject(user));
            if (!responseOk(response)) return response;

            // Update the clientside user list
            user.injectAttributes(response.data);
            User.users.push(user);

            return response.data;
        } catch (error) {
            console.log(error);
            return {};
        }
    }

    async asyncUpdate(user) {
        console.log(user);
        try {
            const response = await putAPI(UserAdaptor.ENDPOINT, user);
            console.log(response);
            if (!responseOk(response)) return response;

            const data = response.data

            // Update the clientside user list
            user.injectAttributes(data);
            User.users[User.users.findIndex(o => o.id === user.id)] = user;

            return new User(data.id, data.email, data.name, data.permissionLevel, data.lastLogin);
        } catch (error) {
            return {};
        }
    }

    async asyncDeleteById(id) {
        try {
            const response = await deleteAPI(`${UserAdaptor.ENDPOINT}/${id}`);
            if (!responseOk(response)) return response;

            // Update the clientside user list
            User.users = User.users.filter(o => o.id !== id);

            return response.data;
        } catch (error) {
            return {};
        }
    }

}
