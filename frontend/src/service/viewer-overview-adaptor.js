import {deleteAPI, getAPI, postAPI, responseOk} from "@/backend";
import {getJWT} from "@/data";

export class ViewerOverviewAdaptor {

    async fetchViewerReports() {
        try {
            const response = await getAPI("/api/reports");
            console.log('Reports Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }

            // Filter out reports where the sender is "admin"
            return response.data.filter(report => report.sender !== "viewer");

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }

    async postReport(report) {
        try {
            const response = await postAPI("/api/reports", report);
            console.log('Post Report Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Post Report Response not OK:', response.data);
                return null;
            }

            return response.data;

        } catch (error) {
            console.error('An unexpected error occurred while posting report:', error);
            return null;
        }
    }

    async deleteReport(id) {
        try {
            const response = await deleteAPI(`/api/reports/${id}`);

            if (!responseOk(response)) {
                console.warn('Delete Report Response not OK:', response.data);
                return null;
            }

            console.log('Deleted Report:', response.data);
            return response.data;

        } catch (error) {
            console.error('An unexpected error occurred while posting report:', error);
            return null;
        }
    }

    fetchViewerName() {
        try {
            const jwt = getJWT();

            // Check if the JWT is valid
            if (!jwt) {
                console.warn('JWT token not available');
                return null;
            }

            // Log the JWT contents to the console
            console.log('JWT:', jwt);

            return jwt.slice(0, 10);

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return null;
        }
    }

    async fetchViewerProjects() {
        try {
            const response = await getAPI("/api/projects");
            console.log('Projects Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }

            return response.data;

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }






    // async fetchViewerTeam(id) {
    //
    //     try {
    //         const response = await getAPI(`/api/teams/${id}`);
    //         console.log(response.status);
    //
    //         if (response.ok) {
    //             const data = await response.data();
    //             return data.name;
    //         } else {
    //             console.error(`Failed to fetch user name: ${response.statusText}`);
    //         }
    //     } catch (error) {
    //         console.error("Error fetching user name:", error);
    //     }
    // }

}