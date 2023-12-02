import {deleteAPI, getAPI, postAPI, responseOk} from "@/backend";
import {getId} from "@/data";

export class ReportAdaptor {

    async fetchViewerReports() {
        try {
            const response = await getAPI("/api/reports");
            console.log('Reports Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }

            // Get the ID of the logged-in user
            const userId = getId();

            // TODO replace with backend query when we have a real database
            // Filter reports based on the receiver_id matching the logged-in user's ID
            return response.data.filter(report => report.receiverId === userId);


        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }

    async fetchAdminReports() {
        try {
            const response = await getAPI("/api/reports");
            console.log('Reports Response Data:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return [];
            }

            // Filter out all the reports where the receiver is equal to "admin"
            // TODO replace with backend query when we have a real database
            return response.data.filter(report => report.receiverId === 1); // 1 = admin ID

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

    // async fetchViewerProjects() {
    //     try {
    //         const response = await getAPI("/api/projects");
    //         console.log('Projects Response Data:', response.data);
    //
    //         if (!responseOk(response)) {
    //             console.warn('Response not OK:', response.data);
    //             return [];
    //         }
    //
    //         return response.data;
    //
    //     } catch (error) {
    //         console.error('An unexpected error occurred:', error);
    //         return [];
    //     }
    // }






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