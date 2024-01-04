//
// import Team from "@/models/team";
// export class TeamsAdaptor {
//     resourcesUrl;
//
//     constructor(resourcesURL) {
//         this.resourcesUrl = resourcesURL;
//     }
//
//     async fetchJson(url, options = null) {
//         try {
//             let response = await fetch(url, options);
//
//             console.log(response); // Log the response for debugging
//
//             if (response.ok) {
//                 return await response.json();
//             } else {
//                 console.log(response, !response.bodyUsed ? await response.text() : "");
//                 return null;
//             }
//         } catch (error) {
//             console.error("Error fetching JSON:", error);
//             return null;
//         }
//     }
//
//     async asyncFindAll() {
//         const teamsData = await this.fetchJson(this.resourcesUrl);
//         if (Array.isArray(teamsData)) {
//             return teamsData.map(data => new Team(data.id, data.name, data.warehouse, data.project));
//         } else {
//             console.error('Geen geldige JSON-array ontvangen.');
//             return [];
//         }
//     }
//
//     async asyncFindById(id) {
//         return this.fetchJson(this.resourcesUrl + "/" + id);
//     }
//
//     async asyncSaveTeam(team) {
//         console.log(team)
//         const url = `${this.resourcesUrl}`;
//         const options = {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(team)
//         };
//         const response = await this.fetchJson(url, options);
//
//         if (response.id) {
//             team.id = response.id;
//         }
//
//         return response;
//     }
//
//
//     async asyncUpdateTeam(team) {
//         try {
//             const url = `${this.resourcesUrl}/${team.id}`;
//             const options = {
//                 method: 'PUT',
//                 headers: {
//                     'Content-Type': 'application/json',
//                 },
//                 body: JSON.stringify(team),
//             };
//
//             const response = await this.fetchJson(url, options);
//
//             if (!response) {
//                 throw new Error('Failed to update team. Empty response.');
//             }
//
//             // Assuming the response contains the updated team information
//             const updatedTeam = new Team(response.id, response.name, response.warehouse, response.project);
//
//             return updatedTeam;
//         } catch (error) {
//             console.error('Error updating team:', error);
//             throw error; // Rethrow the error to handle it in the calling code
//         }
//     }
//
//
//
//
//     async asyncDeleteById(id) {
//         return this.fetchJson(this.resourcesUrl + "/" + id,
//             {
//                 method: 'DELETE'
//             })
//     }
// }