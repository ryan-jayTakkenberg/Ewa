<template>

  <overview-modal
      v-if="modal"
      @confirm-delete="deleteReport"
      @cancel-delete="closeModal"
      :selectedReports="selectedReports"
  />

  <!--- user ---------------------------------------------------------------------------------->
  <div class="container">

    <div class="header">
      <h1 class="hiText">Hi, {{ capitalizeFirstLetter(username) }}</h1>

      <div class="dateContainer">
        <div class="dayOfTheWeek">{{ dayOfTheWeek }}</div>
        <div class="dayOfTheWeekNum">{{ numberOfTheDay }}</div>
      </div>
    </div>

    <div class="userInfoContainer">

      <div class="infoTitleContainer">
        <div class="infoTitle">Currently working in:</div>
        <div class="infoTitle">Ongoing projects:</div>
        <div class="infoTitle">Reports:</div>
      </div>

      <div class="infoValueContainer">
        <div class="infoValue">{{ currentTeam?.name ?? "Currently not in a team" }}</div>
        <div class="infoValue">{{ this.userProjects?.length }}</div>
        <div class="infoValue"> {{ reports.length }}</div>
      </div>

    </div>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="container">

    <h1 class="sectionTitle">Ongoing Projects for: {{capitalizeFirstLetter(username)}}</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in userProjects" :key="index">
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
  <div class="container">

    <h1 class="sectionTitle">Reports</h1>

    <div class="reportsContainerWrapper">

      <div class="inboxReportsContainer">

        <div class="inboxHeader">
          <div class="reportContainerTitle">Inbox</div>

          <div class="buttonWrapper">
            <button class="replyReport" @click="replyReport">
              <span class="material-symbols-outlined">reply</span>
            </button>
            <button class="deleteReport" @click="showModal">
              <span class="material-symbols-outlined">delete</span>
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

        <div class="sendReportInputWrapper">
          <div class="sendReportInput">Send a report to:</div>
          <select v-model="receiverId" class="reportReceiverSelect" ref="selectUser">
            <option v-for="user in availableUsers" :value="user.id" :key="user.id">{{ capitalizeFirstLetter(user.name) }}</option>
          </select>
        </div>

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
import User from "@/models/user";
import {getAPI, responseOk} from "@/backend";

export default {

  name: "UserOverviewComponent",
  inject: ['reportService', 'projectService', 'teamsService', 'userService'],
  components: {
    "overview-modal": OverviewModal,
  },

  data() {
    return {
      username: getUsername(),
      userId: getId(),
      currentTeam: null,
      currentWarehouse: null,
      allProjects: [],
      userProjects: [],
      reports: [],
      selectedReports: [],
      reportBody: "",
      senderId: getId(),
      senderName: getUsername(),
      receiverId: "",
      users: [],
      modal: false,
    }
  },

  mounted() {
    this.fetchAllUsers();
    this.fetchReports();
    this.fetchProjects();
    this.getLoggedInUserTeam();
    this.getLoggedInUserProjects();
  },

  methods: {

    async fetchAllUsers() {
      this.users = await this.userService.fetchAll();
      // console.log('Fetched users: ', [...this.users]);
    },

    async fetchReports() {
      this.reports = await this.reportService.fetchReports();
      // console.log('Fetched reports: ', [...this.reports]);
    },

    async fetchProjects() {
      this.allProjects = await this.projectService.asyncFindAll();
      // console.log('Fetched projects: ', [...this.allProjects]);
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

    replyReport() {

      if (this.selectedReports.length !== 1) {
        alert("Please select exactly one report to reply to.");
        return;
      }

      this.receiverId = this.selectedReports[0].senderId;
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
      const loggedInUser = User.users[0]; // Assuming there's only one user in the response

      if (loggedInUser && loggedInUser.team) {
        return this.currentTeam = loggedInUser.team;
      } else {
        return "";
      }
    },

    getLoggedInUserProjects() {
      this.userProjects = this.currentTeam && this.allProjects
          ? this.allProjects.filter(project => project.team.id === this.currentTeam.id)
          : [];
      console.log(this.userProjects);
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

    User() {
      return User
    },
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

    availableUsers() {
      // Filter out users with the same id as you, because you can't send a report to yourself
     return this.users.filter(user => user.id !== this.userId);
    },
  },

}

</script>

<style scoped>

.container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 2rem 3rem;
  border-bottom: 2px solid var(--col-border);
}

.sectionTitle {
  text-align: left;
  font-size: var(--font-size-medium);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .hiText {
    font-size: var(--font-size-large);
    font-weight: var(--font-weight-bold);
    color: var(--col-black);
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

}

.userInfoContainer {
  display: flex;
  align-items: flex-start;
  gap: 1rem;

  .infoTitle {
    font-size: var(--font-size-small);
    font-weight: var(--font-weight-medium);
    color: var(--col-grey);
    height: 25px;
  }

  .infoValue {
    font-size: var(--font-size-small);
    font-weight: var(--font-weight-bold);
    color: var(--col-black);
    height: 25px;
  }

}

.projectContainer {
  display: flex;
  gap: 1rem;
  width: 100%;
  height: auto;
  padding: 1rem 0.15rem;
  overflow-x: auto;
  margin-top: 1rem;

  .projectContainer::-webkit-scrollbar {
    width: 5px;
  }

  .projectContainer::-webkit-scrollbar-thumb {
    background-color: var(--col-border);
    border-radius: 10px;
  }

  .projectContainer::-webkit-scrollbar-track {
    background-color: var(--col-light-grey);
    border-radius: 10px;
  }

  .projectWrapper {
    min-width: 500px;
    width: auto;
    flex: 0 0 auto;
    background: var(--col-white);
    border: 2px solid var(--col-black);
    border-radius: 5px;
    padding: 1rem;
  }

  .projectHeader {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
  }

  .projectTitle {
    font-size: 1.2rem;
    font-weight: var(--font-weight-bold);
    color: var(--col-black);
  }

  .projectDescription {
    display: flex;
    gap: 2rem;
  }

  .descriptionTitle {
    font-weight: var(--font-weight-bold);
  }

  .descriptionValue {
    font-weight: var(--font-weight-small);
  }

}

.reportsContainerWrapper {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  width: 100%;
  margin-top: 2rem;

  .inboxReportsContainer {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 2rem;
    height: 500px;
    width: 100%;
    background: #f5f5f5;
    border-radius: 10px;
  }

  .selected {
    outline: 2px solid var(--col-black);
  }

  .sendReportsContainer {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 2rem;
    height: 500px;
    width: 100%;
    background: var(--col-light-grey);
    border-radius: 10px;
  }

  .sendReportInputWrapper {
    display: flex;
    align-items: center;
    gap: 1rem;
    height: 40px;
  }

  .sendReportInput {
    font-size: var(--font-size-small);
    font-weight: var(--font-weight-bold);
    color: var(--col-black);
    width: 200px;
  }

  .reportReceiverSelect {
    width: 100%;
    border-radius: 5px;
    padding: 10px;
  }

  .reportInput {
    height: 50%;
    padding: 0.5rem;
    border-radius: 5px;
    resize: vertical;
    min-height: 20%;
    max-height: 75%;
  }

  .sendReportButton {
    width: 150px;
    height: 50px;
    background: var(--col-solar);
    color: var(--col-white);
    font-size: var(--font-size-small);
    font-weight: var(--font-weight-bold);
    border-radius: 5px;
    outline: none;
    cursor: pointer;
  }

  .inboxHeader {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 10px;
    padding: 0 1rem;
    background: var(--col-white);
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

  .reportWrapper {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    width: 100%;
    min-height: 150px;
    height: auto;
    flex: 0 0 auto;
    background: var(--col-white);
    padding: 1rem;
    border-radius: 5px;
    border: 1px solid var(--col-border);
    cursor: pointer;
  }

  .reportHeader {
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid var(--col-border);
    width: 100%;
  }

  .reportSender {
    font-weight: var(--font-weight-bold);
    color: var(--col-black);
  }

  .reportDate {
    font-weight: var(--font-weight-medium);
    color: var(--col-grey);
  }

  .reportBody {
    font-weight: var(--font-weight-medium);
    color: var(--col-black);
  }

  .buttonWrapper {
    display: flex;
    gap: 1rem;
  }

  .replyReport,
  .deleteReport {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
    width: 50px;
    background: none;
    outline: none;
    cursor: pointer;
  }

  .replyReport:hover,
  .deleteReport:hover {
    background: var(--col-solar);
    color: var(--col-white);
  }

}

.material-symbols-outlined {
 font-variation-settings:
     'FILL' 0,
     'wght' 500,
     'GRAD' 0,
     'opsz' 40
}

</style>