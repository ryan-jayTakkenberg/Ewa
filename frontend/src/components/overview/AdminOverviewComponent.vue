<script>
import {AdminOverviewAdaptor} from "@/service/admin-overview-adaptor";
import {getId, getUsername} from "@/data";
import OverviewModal from "@/components/overview/OverviewModal.vue";
import NotificationComponent from "@/components/general/NotificationComponent.vue";
import Project from "@/models/project";

export default {

  name: "AdminOverviewComponent",
  components: {NotificationComponent, OverviewModal},
  inject: ['reportService', 'userService', 'projectService'],

  data() {
    return {
      productsSold: '75',
      unresolvedReports: '2',
      warehousesLowStock: '3',
      globalTotalStock: '350',
      projects: Project.projects,
      reports: [],
      selectedReports: [],
      reportBody: "",
      receiverId: "",
      senderId: getId(),
      senderUsername: getUsername(),
      users: [],
      modal: false,

    }
  },

  mounted() {
    this.fetchAllUsers();
    this.fetchReports();
  },

  methods: {

    async fetchAllUsers() {
      this.users = await this.userService.fetchAllUsers();
    },

    async fetchReports() {
      this.reports = await this.reportService.fetchReports();
      console.log('Fetched reports: ', [...this.reports]);
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
        senderName: this.senderUsername,
        receiverId: this.receiverId,
        body: this.reportBody,
      };

      await this.reportService.postReport(report);

      this.reportBody = '';
      alert('Your report was successfully sent!');
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

          // Notify the user about a successful delete
          const message = this.selectedReports.length > 1 ? 'Reports' : 'Report';
          this.$refs.notificationComponent.createSuccessfulNotification(`${message} successfully deleted`);
        } else {

          // Notify the user about an unsuccessful delete
          console.log('An error occurred when trying to delete the report with id:', report.id);
          this.$refs.notificationComponent.createUnsuccessfulNotification('Unsuccessful delete. Try again');

        }
      }

      this.selectedReports = [];
      this.modal = false;

      console.log('Your current reports after delete: ', [...this.reports]);

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

    async loadOngoingProjectsCount() {
          const adaptor = new AdminOverviewAdaptor();
          const count = await adaptor.fetchOngoingProjectsCount();
          if (count !== null) {
            this.ongoingProjects = count;
          }
    },

  },

  computed: {
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

    filteredUsers() {
      // Filter out users with permissionLevel === 'admin', because you can't send report to yourself
      return this.users.filter(user => user.permissionLevel !== 'ADMIN');
    },

  },

}

</script>

<template>

  <OverviewModal
      v-if="modal"
      @confirm-delete="deleteReport"
      @cancel-delete="closeModal"
      :selectedReports="selectedReports"
  />

  <NotificationComponent ref="notificationComponent" />

  <!--- Persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="welcomeContainer">
      <p class="description">Welcome to the Solar Sedum AdminOverview.</p>
    </div>
    <div class="profilePicContainer">
      <div class="profilePic"></div>
    </div>

  </div>
  <div id="sectionTitles">
    <h2 class="sectionHeading">Analytics</h2>
    <h2 class="sectionHeading">Forecasting</h2>
  </div>

  <!--- Agenda ---------------------------------------------------------------------------------->
  <div class="sectionContainer agenda">

    <div class="dateContainer">
      <p class="dayOfTheWeek">{{ dayOfTheWeek }}</p>
      <p class="dayOfTheWeekNum">{{ numberOfTheDay }}</p>
    </div>

    <div class="insightContainer">
      <p class="medium"> Products sold this week:</p>
      <div class="meetingWrapper">
        <div id="textBigGreen"> {{ productsSold }} </div>
      </div>
    </div>

    <div class="insightContainer">
      <p class="medium">Total Ongoing Projects:</p>
      <div class="meetingWrapper">
<!--        <div id="textBig"> {{ ongoingProjects }} </div>-->
      </div>
    </div>

    <div class="insightContainer">
      <p class="medium">Unresolved Reports:</p>
      <div class="meetingWrapper">
        <div id="textBigRed"> {{ unresolvedReports }} </div>
      </div>
    </div>

    <!--- forecasting ------------------------------------------------------------------------------->
    <div class="insightContainer">
      <p class="medium"> Global Amount of Stock:</p>
      <div class="meetingWrapper">
        <div id="textBig"> {{ globalTotalStock }} </div>
      </div>
    </div>

    <div class="insightContainer">
      <p class="medium">Amount of warehouses with low stock:</p>
      <div class="meetingWrapper">
        <div id="textBigRed"> {{ warehousesLowStock }} </div>
      </div>

    </div>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Ongoing Projects</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in projects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle">{{ project.projectName }}</div>
          <div class="statusWrapper">
            <div class="projectStatus"> Status: </div>
            <div class="statusColor"></div>
          </div>
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
        <p>Click for more details</p>

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
              <span class="material-symbols-outlined button">delete</span>
            </button>
            <button class="filterMessage">
              <span class="material-symbols-outlined button">filter_alt</span>
            </button>
          </div>

        </div>
        <div
            class="messageWrapper"
            v-for="(report, index) in reports"
            :key="index"
            @click="toggleSelected(index)"
            :class="{ 'selected': selectedReports.some(selectedReport => selectedReport.id === report.id) }">

          <div class="messageHeader">
            <div class="messageSender"> {{ capitalizeFirstLetter(report.senderName) }} </div>
            <div class="messageDate"> {{ report.date }} </div>
          </div>

          <div class="message"> {{ report.body }} </div>

        </div>
      </div>

      <div class="sendReportsContainer">
        <div class="wrapper">
          <label>Send a report to:</label>
          <select v-model="receiverId" class="reportReceiver">
            <option v-for="user in filteredUsers" :value="user.id" :key="user.id">{{ user.name }}</option>
          </select>
        </div>

        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>

        <button @click="postReport" class="sendReportButton" :class="{ 'disabledButton': !receiverId }" :disabled="!receiverId">Send</button>
      </div>


    </div>

  </div>


</template>

<style scoped>

.personaContainer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 2rem 3rem 0 3rem;
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
}

.description {
  font-size: 1rem;
  font-weight: 400;
  color: #aaa;
}

.profilePicContainer {
  height: 100px;
  width: 100px;
  background: #f5f5f5;
  border-radius: 50%;
}

.sectionContainer {
  width: 100%;
  padding: 2rem 3rem;
  border-bottom: 2px solid #e5e5e5;
}

.agenda {
  display: flex;
}

.dateContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 150px;
  height: 150px;
  border: 2px solid #e5e5e5;
  border-radius: 15px;
}

.dayOfTheWeek {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 0.8;
  color: #222;
}

.dayOfTheWeekNum {
  font-size: 3rem;
  font-weight: 800;
  line-height: 0.8;
  color: #222;
}

.insightContainer {
  display: flex;
  flex-direction: column;
  padding: 1rem;

}
.insightContainer:nth-child(4) {
  border-right: 2px solid #e5e5e5;
  padding-right: 1rem;
  margin-right: 1rem;
}

.meetingWrapper {
  display: flex;
  align-items: center;
  justify-content: left;
  gap: 0.5rem;
  flex-direction: row;
}

.medium {
  font-weight: 600;
  margin-bottom: 0.5rem;
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

.projectWrapper:hover {
  background: #f5f5f5;
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

.messageHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 50%;
  margin-bottom: 1rem;
}

.messageWrapper {
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
  cursor: pointer;
}

.selected {
  background-color: #e5e5e5;
}

.buttonContainer {
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
  background: #f5f5f5;
  outline: none;
  cursor: pointer;
}

.filterMessage:hover,
.deleteMessage:hover {
  background: #e5e5e5;
}

#textBig, #textBigRed, #textBigGreen {
  font-size: 4rem;
  text-align: center;
  width: 100%;
  font-weight: 500;
  line-height: 0.8;
}

#textBigGreen{
  color: green;
}
#textBigRed{
  color: red;
}
.sectionHeading {
  font-size: 2.5rem;
  font-weight: bold;
  color: #222;
  text-align: center;
  margin: 2rem 0 1rem;
}

#sectionTitles {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.agenda {
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-around;
  align-items: flex-start;
}

.insightContainer {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  flex: 1 0 auto;
  margin: 0.5rem;
  min-width: 250px;
}

#warehouseSelect {
  padding: 0.5rem;
  margin-top: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;
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
  overflow-y: scroll;
}

.reportsHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 50%;
  margin-bottom: 1rem;
}

.messageWrapper {
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
  border: none;
  cursor: pointer;
}

.inboxHeader {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.messageHeader {
  display: flex;
  justify-content: space-between;
  border-bottom: 2px solid #f5f5f5;
  width: 100%;
}

.messageSender {
  font-weight: 600;
  color: #222;
}

.messageDate {
  font-weight: 400;
  color: #aaa;
}

.buttonWrapper {
  display: flex;
  gap: 1rem;
}

.disabledButton {
  background-color: #c5c5c5;
  cursor: not-allowed;
  outline: none;
}

label {
  font-weight: 500;
  width: 25%;
}

.wrapper {
  display: flex;
}

.reportReceiver {
  border-radius: 5px;
  padding: 0 0.5em;
  width: 150px;
}

</style>

