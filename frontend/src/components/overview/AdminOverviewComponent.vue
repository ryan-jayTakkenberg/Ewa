<script>
import {AdminOverviewAdaptor} from "@/service/admin-overview-adaptor";

export default {

  name: "AdminOverviewComponent",
  inject: ['reportService', 'userService'],

  data() {
    return {
      productsSold: '75',
      ongoingProjects: '0',
      unresolvedReports: '2',
      warehousesLowStock: '3',
      globalTotalStock: '350',
      adminReports: [],
      selectedReports: [],
      reportBody: "",
      reportReceiver: "",
      users: [],

      projects: [
        {title: 'Project Green', team: '1'},
        {title: 'Project Blue', team: '2'},
        {title: 'Project Red', team: '1'},
        {title: 'Project Yellow', team: '3'},
      ],
      projectDescriptions: [
        {title: 'Planned from: 17/10/2023 to 25/10/2023'},
        {title: 'Planned from: 19/10/2023 to 23/10/2023'},
        {title: 'Planned from: 20/10/2023 to 27/10/2023'},
        {title: 'Planned from: 25/10/2023 to 31/10/2023'},
      ],
    }
  },

  mounted() {
    this.fetchAdminReports();
    this.fetchAllUsers();
  },

  methods: {

    async fetchAllUsers() {
      this.users = await this.userService.fetchAllUsers();
    },

    async fetchAdminReports() {
      this.adminReports = await this.reportService.fetchAdminReports();
    },

    async postReport() {

      // Check if the report body is empty
      if (!this.reportBody.trim()) {
        alert('Error: Report cannot be empty');
        return;
      }

      const report = {
        date: new Date().toLocaleDateString(),
        sender: "admin",
        receiver: this.reportReceiver,
        body: this.reportBody,
      };

      await this.reportService.postReport(report);

      this.reportBody = '';
    },

    async deleteReport() {

      for (const report of this.selectedReports) {
        await this.reportService.deleteReport(report.id);

        // Remove the deleted report from the viewerReports array
        const indexToDelete = this.viewerReports.findIndex((r) => r.id === report.id);
        if (indexToDelete !== -1) {
          this.viewerReports.splice(indexToDelete, 1);
        }
      }

      this.selectedReports = [];
    },

    capitalizeFirstLetter(str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    },

    getRandomColor() {
      const colors = ['#00d315', '#ff0000'];
      const randomIndex = Math.floor(Math.random() * colors.length);
      return colors[randomIndex];
    },

    toggleSelected(index) {
      if (this.selectedMessages.includes(index)) {
        this.selectedMessages = this.selectedMessages.filter((item) => item !== index);
      } else {
        this.selectedMessages.push(index);
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
        <div id="textBig"> {{ ongoingProjects }} </div>
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
    <label for="warehouseSelect">Choose a warehouse:</label>
    <select id="warehouseSelect" name="warehouse">
      <option value="warehouse1">Warehouse 1</option>
      <option value="warehouse2">Warehouse 2</option>
      <option value="warehouse3">Warehouse 3</option>
      <option value="warehouse4">Warehouse 4</option>
      <option value="warehouse5">Warehouse 5</option>
    </select>

    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in projects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle"> {{ project.title }}</div>
          <div class="statusWrapper">
            <div class="projectStatus"> Status: </div>
            <div :style="{ backgroundColor: getRandomColor() }" class="statusColor"></div>
          </div>
        </div>
        <div class="projectDescription">{{ projectDescriptions[index].title }}<br>
          Team: {{ project.team }}</div>
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
            <button class="deleteMessage" @click="deleteReport">
              <span class="material-symbols-outlined button">delete</span>
            </button>
            <button class="filterMessage">
              <span class="material-symbols-outlined button">filter_alt</span>
            </button>
          </div>

        </div>
        <div
            class="messageWrapper"
            v-for="(report, index) in adminReports"
            :key="index"
            @click="toggleSelected(index)"
            :class="{ 'selected': selectedReports.some(selectedReport => selectedReport.id === report.id) }">

          <div class="messageHeader">
            <div class="messageSender"> {{ capitalizeFirstLetter(report.sender) }} </div>
            <div class="messageDate"> {{ report.date }} </div>
          </div>

          <div class="message"> {{ report.body }} </div>

        </div>
      </div>

      <div class="sendReportsContainer">
        <div class="wrapper">
          <label>Send a report to:</label>
          <select v-model="reportReceiver" class="reportReceiver">
            <option v-for="user in filteredUsers" :value="user.id" :key="user.id">{{ user.name }}</option>
          </select>
        </div>

        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>

        <button @click="postReport" class="sendReportButton" :class="{ 'disabledButton': !reportReceiver }" :disabled="!reportReceiver">Send</button>
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
  overflow-x: scroll;
  margin-top: 1rem;
}

.projectWrapper {
  min-width: 500px;
  width: auto;
  flex: 0 0 auto;
  background: #f5f5f5;
  border-radius: 5px;
  padding: 1rem;
  cursor: pointer;
}

.projectWrapper:hover {
  outline: 2px solid #222;
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
}

p {
  margin-top: 1rem;
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
  outline: 2px solid #222;
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

button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  background: none;
  outline: none;
  cursor: pointer;
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

