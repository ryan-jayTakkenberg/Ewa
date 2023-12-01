import { getAPI, responseOk } from "@/backend";

export class AdminOverviewAdaptor {

    async fetchOngoingProjectsCount() {
        try {
            const response = await getAPI("/api/adminview/count");
            console.log('Ongoing Projects Count:', response.data);

            if (!responseOk(response)) {
                console.warn('Response not OK:', response.data);
                return null; // Return null or an appropriate default value
            }

            return response.data; // Return the data for the component to use

        } catch (error) {
            console.error('An unexpected error occurred:', error);
            return null; // Return null or an appropriate error value
        }
    }

    async fetchReports() {
        try {
            const response = await getAPI("/api/adminview/viewreports");
            if (!responseOk(response)) {
                console.error('Response not OK:', response.data);
                return [];
            }
            return response.data;
        } catch (error) {
            console.error('Error fetching reports:', error);
            return [];
        }
    }

}