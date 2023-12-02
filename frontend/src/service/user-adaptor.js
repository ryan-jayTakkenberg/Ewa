import {deleteAPI, getAPI, responseOk} from "@/backend";

export class UserAdaptor {

    async fetchAllUsers() {
        try {
            const response = await getAPI("/api/users");
            console.log('Users Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }


            return response.data

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }

    async fetchUserById(id) {
        try {
            const response = await deleteAPI(`/api/users/${id}`);

            if (!responseOk(response)) {
                console.warn('No user with ID:', response.data);
                return null;
            }

            console.log('Specific Report:', response.data);
            return response.data;

        } catch (error) {
            console.error('An unexpected error occurred while fetching specific user:', error);
            return null;
        }
    }

}