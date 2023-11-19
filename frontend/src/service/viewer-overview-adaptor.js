import {getAPI} from "@/backend";

export class ViewerOverviewAdaptor {

    resourcesURL;

    constructor(resourcesURL) {
        this.resourcesUrl = resourcesURL;
    }

    async fetchViewerName(id) {

        try {
            const response = await getAPI(`/api/users/${id}`);
            console.log(response.status);

            if (response.ok) {
                const data = await response.data();
                return data.name;
            } else {
                console.error(`Failed to fetch user name: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error fetching user name:", error);
        }
    }


    async fetchViewerTeam(id) {

        try {
            const response = await getAPI(`/api/teams/${id}`);
            console.log(response.status);

            if (response.ok) {
                const data = await response.data();
                return data.name;
            } else {
                console.error(`Failed to fetch user name: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error fetching user name:", error);
        }
    }

    async fetchViewerProjects(id) {

        try {
            const response = await getAPI(`/api/projects/${id}`);
            console.log(response.status);

            if (response.ok) {
                return await response.data();
            } else {
                console.error(`Failed to fetch user name: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error fetching user name:", error);
        }
    }

    async fetchViewerReports(id) {

        try {
            const response = await getAPI(`/api/projects/${id}`);
            console.log(response.status);

            if (response.ok) {
                return await response.data();
            } else {
                console.error(`Failed to fetch user name: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error fetching user name:", error);
        }
    }

    async postViewerReports(id, reportData) {

        try {
            let response = await fetch(`${this.resourcesUrl}/reports/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Include additional headers, such as authentication headers
                },
                body: JSON.stringify(reportData),
            });

            console.log(response.status);

            if (response.ok) {
                return await response.json();
            } else {
                console.error('Failed to post viewer report:', response.statusText);
            }
        } catch (error) {
            console.error(error);
        }
    }

    async postOrders(id, orderData) {

        try {
            let response = await fetch(`${this.resourcesUrl}/orders/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Include additional headers, such as authentication headers
                },
                body: JSON.stringify(orderData),
            });

            console.log(response.status);

            if (response.ok) {
                return await response.json();
            } else {
                console.error('Failed to post viewer report:', response.statusText);
            }
        } catch (error) {
            console.error(error);
        }
    }

}