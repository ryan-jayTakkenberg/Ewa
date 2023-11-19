export class ViewerOverviewAdaptor {

    resourcesURL;

    constructor(resourcesURL) {
        this.resourcesUrl = resourcesURL;
    }

    async fetchViewerTeam(id) {

        try {
            let response = await fetch(this.resourcesUrl`/${id}`);
            console.log(response.status);
            return await response.json();
        } catch (error) {
            console.error(error);
        }
    }

    async fetchViewerProjects(id) {

        try {
            let response = await fetch(this.resourcesUrl`/${id}`);
            console.log(response.status);
            return await response.json();
        } catch (error) {
            console.error(error);
        }
    }

    async fetchViewerReports(id) {

        try {
            let response = await fetch(this.resourcesUrl`/${id}/reports`);
            console.log(response.status);
            return await response.json();
        } catch (error) {
            console.error(error);
        }
    }

    async postViewerReports(id, reportData) {

        try {
            let response = await fetch(`${this.resourcesUrl}/${id}/reports`, {
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
}