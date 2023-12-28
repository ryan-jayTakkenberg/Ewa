import {deleteAPI, getAPI, postAPI, responseOk} from "@/backend";
import {getId} from "@/data";

export class ReportAdaptor {

    async fetchReports() {
        try {
            const response = await getAPI("/api/reports");
            // console.log('Reports Response Data:', response.data);

            if (!responseOk(response)) {
                // console.warn('Response not OK:', response.data);
                // replaced with error notification in fetch interceptor
                return [];
            }

            // Get the ID of the logged-in user
            const userId = getId();

            // Filter reports based on the receiver_id matching the logged-in user's ID
            return response.data.filter(report => report.receiverId === userId);


        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return [];
        }
    }

    async postReport(report) {
        try {
            const response = await postAPI("/api/reports", report);
            // console.log('Post Report Response Data:', response.data);

            if (!responseOk(response)) {
                // console.warn('Post Report Response not OK:', response.data);
                // replaced with error notification in fetch interceptor
                return null;
            }

            return response;

        } catch (error) {
            console.error('An unexpected error occurred while posting report:', error);
            return null;
        }
    }

    async deleteReport(id) {
        try {
            const response = await deleteAPI(`/api/reports/${id}`);
            // console.log('Report to delete:', id);

            if (!responseOk(response)) {
                // console.warn('Delete Report Response not OK:', response.data);
                // replaced with error notification in fetch interceptor
                return null;
            }

            // console.log('Deleted Report:', response.data);
            return response;

        } catch (error) {
            console.error('An unexpected error occurred while deleting report:', error);
            return null;
        }
    }

}