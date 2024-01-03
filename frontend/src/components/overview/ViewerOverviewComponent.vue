<template>

  <OverviewModal
      v-if="modal"
      @confirm-delete="deleteReport"
      @cancel-delete="closeModal"
      :selectedReports="selectedReports"
  />

  <!--- persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="header">
      <h1>Hi, {{ capitalizeFirstLetter(username) }}</h1>

      <div class="dateContainer">
        <div class="dayOfTheWeek">{{ dayOfTheWeek }}</div>
        <div class="dayOfTheWeekNum">{{ numberOfTheDay }}</div>
      </div>
    </div>

    <div class="welcomeContainer">

      <div class="infoContainer">

        <div class="infoTitle">
          <div class="medium">Currently working in:</div>
          <div class="medium">Warehouse:</div>
          <div class="medium">Ongoing projects:</div>
          <div class="medium">Reports:</div>
        </div>

        <div class="infoValue">
          <div class="bold">{{ currentTeam }}</div>
          <div class="bold">{{ currentWarehouse }}</div>
          <div class="bold">{{ Project.projects.length }}</div>
          <div class="bold"> {{ reports.length }}</div>
        </div>

      </div>

    </div>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Ongoing Projects for: {{capitalizeFirstLetter(username)}}</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in projects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle">Project: {{ project.projectName }}</div>
        </div>

        <div class="projectDescription">
          <div class="infoTitle">
            <div class="descriptionTitle">Client:</div>
            <div class="descriptionTitle">Install Team:</div>
            <div class="descriptionTitle">Install Date:</div>
          </div>
          <div class="infoValue">
            <div class="descriptionValue">{{ project.clientName }}</div>
            <div class="descriptionValue">{{ project.team.name }}</div>
            <div class="descriptionValue">{{ project.installDate }}</div>
          </div>
        </div>

      </div>
    </div>
  </div>

  <!--- reports ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <div class="reportsHeader">
      <h1 class="sectionTitle">Reports</h1>
      <div class="buttonContainer">
      </div>
    </div>

    <div class="reportsContainerWrapper">

      <div class="reportsContainer">

        <div class="inboxHeader">
          <div class="containerTitle">Inbox</div>

          <div class="buttonWrapper">
            <button class="deleteMessage" @click="showModal">
              <span class="material-symbols-outlined">delete</span>
            </button>
            <button class="filterMessage">
              <span class="material-symbols-outlined">filter_alt</span>
            </button>
          </div>
        </div>

        <div class="actualReports">
          <div
              class="reportWrapper"
              v-for="(report, index) in reports"
              :key="index"
              @click="toggleSelected(index)"
              :class="{ 'selected': selectedReports.some(selectedReport => selectedReport.id === report.id) }">

            <div class="reportHeader">
              <div class="reportSender"> {{ capitalizeFirstLetter(report.senderName) }} </div>
              <div class="reportDate"> {{ report.date }} </div>
            </div>

            <div class="reportBody"> {{ report.body }} </div>

          </div>
        </div>

      </div>

      <div class="sendReportsContainer">
        <div class="containerTitle">Send a report to the admin</div>
        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>
        <button @click="postReport" class="sendReportButton">Send</button>

      </div>

    </div>

  </div>


</template>

<script>

import {getId, getUsername} from "@/data";
import Project from "@/models/project";
import OverviewModal from "@/components/overview/OverviewModal.vue";
import Team from "../../models/team";

export default {

  name: "UserOverviewComponent",
  inject: ['reportService', 'projectService', 'teamsService'],
  components: {
    OverviewModal,
  },

  data() {
    return {
      username: getUsername(),
      currentTeam: null,
      currentWarehouse: null,
      projects: Project.projects,
      reports: [],
      selectedReports: [],
      reportBody: "",
      senderId: getId(),
      senderName: getUsername(),
      receiverId: 1, // admin id

      modal: false,
    }
  },

  mounted() {
    this.fetchReports();
    this.getLoggedInUserTeam();
    // this.getLoggedInUserTeamAPI();
    this.getLoggedInUserWarehouse();
  },

  methods: {

    async fetchReports() {
      this.reports = await this.reportService.fetchReports();
      // console.log('Fetched reports: ', [...this.reports]);
    },

    async postReport() {

      // Check if the report body is empty
      if (!this.reportBody.trim()) {
        alert('Error: Report cannot be empty');
        return;
      }

      const report = {
        date: new Date().toLocaleDateString(),
        senderId: this.senderId,
        senderName: this.senderName,
        receiverId: this.receiverId,
        body: this.reportBody,
      };

      const postedReport = await this.reportService.postReport(report);

      // Check if delete was successful (HTTP status code 201)
      if (postedReport.status === 201) {
        console.log('Successfully posted report:', postedReport.data);
      } else {
        console.log('An error occurred when trying to post the report:', report);
      }

      this.reportBody = '';
    },

    async deleteReport() {

      // Send delete request per selected report to the adaptor
      for (let i = 0; i < this.selectedReports.length; i++) {

        const report = this.selectedReports[i];
        const deletedReport = await this.reportService.deleteReport(report.id);

        // Check if delete was successful (HTTP status code 200)
        if (deletedReport.status === 200) {

          console.log('Successfully deleted report:', deletedReport);

          // Remove the deleted report from the reports array
          const indexToDelete = this.reports.findIndex((r) => r.id === report.id);
          if (indexToDelete !== -1) {
            this.reports.splice(indexToDelete, 1);
          }
        } else {
          console.log('An error occurred when trying to delete the report with id:', report.id);
        }
      }

      this.selectedReports = [];
      this.modal = false;

      console.log('Your current reports after delete: ', [...this.reports]);
    },

    getLoggedInUserTeam() {
      const loggedInUserId = getId();
      const team = Team.teams.find(team => team.id === loggedInUserId);
      return this.currentTeam = team ? team.name : "Currently not in a team";
    },

    getLoggedInUserTeamAPI() {
      return this.currentTeam = this.teamsService.asyncFindById(getId());
    },

    getLoggedInUserWarehouse() {
      const loggedInUserId = getId();
      const team = Team.teams.find(team => team.id === loggedInUserId);
      return this.currentWarehouse = team ? team.warehouse.name : "Currently not in a warehouse";
    },

    showModal() {

      if (this.selectedReports.length === 0) {
        alert("Please select a report to delete");
        return;
      }
      this.modal = true;
    },

    closeModal() {
      this.modal = false;
      this.selectedReports = [];
    },

    capitalizeFirstLetter(str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    },

    toggleSelected(index) {
      const selectedReportIndex = this.selectedReports.findIndex((report) => report.id === this.reports[index].id);

      if (selectedReportIndex === -1) {
        // If not already selected, add to the selectedReports array
        this.selectedReports.push(this.reports[index]);
      } else {
        // If already selected, remove from the selectedReports array
        this.selectedReports.splice(selectedReportIndex, 1);
      }
    },
  },

  computed: {
    Team() {
      return Team
    },
    Project() {
      return Project
    },
    dayOfTheWeek() {
      const today = new Date();
      today.setDate(today.getDate());
      const options = { weekday: 'short' };

      return today.toLocaleDateString(undefined, options);
    },
    numberOfTheDay() {
      const today = new Date();
      return today.getUTCDate()
    },
  },

}

</script>

<style scoped>

.personaContainer {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 3rem 3rem 2rem 3rem;
  border-bottom: 2px solid #e5e5e5;
}

.header {
  display: flex;
  justify-content: space-between;
}

.welcomeContainer {
  display: flex;
  align-items: flex-start;
  flex-direction: column;
}

h1 {
  font-size: 2rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 2rem;
}

.infoContainer {
  display: flex;
  gap: 1rem;
}

.medium {
  font-size: 1rem;
  font-weight: 400;
  color: #aaa;
  height: 25px;
}

.bold {
  font-size: 1rem;
  font-weight: 600;
  color: #222;
  height: 25px;
}

.sectionContainer {
  width: 100%;
  padding: 2rem 3rem;
  border-bottom: 2px solid #e5e5e5;
}

.dateContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100px;
  height: 100px;
  background: #f5f5f5;
  border-radius: 10px;
}

.dayOfTheWeek {
  font-size: 1.2rem;
  font-weight: 700;
  color: #222;
}

.dayOfTheWeekNum {
  font-size: 2rem;
  font-weight: 800;
  color: #222;
}

.sectionTitle {
  text-align: left;
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
}

.projectContainer {
  display: flex;
  gap: 1rem;
  width: 100%;
  height: auto;
  padding: 1rem 0.15rem;
  overflow-x: auto;
  margin-top: 1rem;
}

.projectContainer::-webkit-scrollbar {
  width: 5px;
}

.projectContainer::-webkit-scrollbar-thumb {
  background-color: #e5e5e5;
  border-radius: 10px;
}

.projectContainer::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 10px;
}

.descriptionTitle {
  font-weight: 600;
}

.projectWrapper {
  min-width: 500px;
  width: auto;
  flex: 0 0 auto;
  background: #fff;
  border: 2px solid #ccc;
  border-radius: 5px;
  padding: 1rem;
  cursor: pointer;
}

.projectHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.projectTitle {
  font-size: 1.2rem;
  font-weight: 700;
  color: #222;
}

.projectDescription {
  display: flex;
  gap: 2rem;
}

.descriptionValue {
  font-weight: 300;
}

.statusWrapper {
  display: flex;
  align-items: center;
}

.projectStatus {
  font-size: 1.2rem;
  font-weight: 400;
  color: #222;
}

.statusColor {
  height: 20px;
  width: 20px;
  border-radius: 50%;
  margin-left: 0.5rem;
  background: #5DDB88;
}

p {
  margin-top: 2rem;
  font-weight: 300;
  color: #222;
}

.reportsContainerWrapper {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  width: 100%;
}

.reportsContainer {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  height: 500px;
  width: 100%;
  background: #f5f5f5;
  border-radius: 10px;
}

.reportsHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 50%;
  margin-bottom: 1rem;
}

.reportWrapper {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  min-height: 150px;
  height: auto;
  flex: 0 0 auto;
  background: #fff;
  padding: 1rem;
  border-radius: 5px;
  border: 1px solid #e5e5e5;
  cursor: pointer;
}

.selected {
  outline: 2px solid #222;
}

.sendReportsContainer {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  height: 500px;
  width: 100%;
  background: #f5f5f5;
  border-radius: 10px;
}

.containerTitle {
  font-size: 1em;
  font-weight: 600;
  color: #222;
}

.reportInput {
  height: 80%;
  padding: 0.5rem;
  border-radius: 5px;
  resize: vertical;
  min-height: 20%;
  max-height: 70%;
}

.sendReportButton {
  width: 100px;
  height: 50px;
  background: #c5ce2c;
  color: #fff;
  font-size: 1em;
  font-weight: 600;
  border-radius: 5px;
  outline: none;
  cursor: pointer;
}

.inboxHeader {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 10px;
  padding: 0 0 0 1rem;
  background: #fff;
}

.actualReports {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  height: 500px;
  width: 100%;
  border-radius: 10px;
  overflow-y: scroll;
}

.actualReports::-webkit-scrollbar {
  width: 10px;
}

.actualReports::-webkit-scrollbar-thumb {
  background-color: #e5e5e5;
  border-radius: 10px;
}

.actualReports::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 10px;
}

.reportHeader {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e5e5e5;
  width: 100%;
}

.reportSender {
  font-weight: 600;
  color: #222;
}

.reportDate {
  font-weight: 400;
  color: #c5c5c5;
}

.reportBody {
  font-weight: 400;
  color: #222;
}

.buttonWrapper {
  display: flex;
  gap: 1rem;
}

.filterMessage,
.deleteMessage {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  background: none;
  outline: none;
  cursor: pointer;
}

.filterMessage:hover,
.deleteMessage:hover {
  background: #c5ce2c;
  color: #fff;
}

.material-symbols-outlined {
 font-variation-settings:
     'FILL' 0,
     'wght' 500,
     'GRAD' 0,
     'opsz' 40
}

</style>