<script>
import {getId, getUsername, getUserTeam} from "@/data";
import Project from "@/models/project";

export default {

  name: "UserOverviewComponent",
  inject: ['reportService', 'projectService'],

  data() {
    return {
      viewerName: getUsername(),
      viewerTeam: getUserTeam(), // TODO
      viewerProjects: Project.projects,
      viewerReports: [],
      selectedReports: [],
      reportBody: "",
      senderId: getId(),
      senderName: getUsername(),
      receiverId: 1,
      }
    },

  mounted() {
    this.fetchViewerReports();
  },

  methods: {

    async fetchViewerReports() {
      this.viewerReports = await this.reportService.fetchViewerReports();
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

      await this.reportService.postReport(report);

      this.reportBody = '';
      alert('Your report was successfully sent!');
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
      const selectedReportIndex = this.selectedReports.findIndex((report) => report.id === this.viewerReports[index].id);

      if (selectedReportIndex === -1) {
        // If not already selected, add to the selectedReports array
        this.selectedReports.push(this.viewerReports[index]);
      } else {
        // If already selected, remove from the selectedReports array
        this.selectedReports.splice(selectedReportIndex, 1);
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
  },

}

</script>

<template>

  <!--- Persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="welcomeContainer">
      <h1>Hi, {{ capitalizeFirstLetter(viewerName) }}</h1>
      <div class="description">{{ viewerTeam }}</div>
    </div>

    <div class="dateContainer">
      <p class="dayOfTheWeek">{{ dayOfTheWeek }}</p>
      <p class="dayOfTheWeekNum">{{ numberOfTheDay }}</p>
    </div>

  </div>

  <!--- Agenda ---------------------------------------------------------------------------------->
<!--  <div class="sectionContainer agenda">-->

<!--      <div class="insightContainer">-->
<!--        <p class="medium"> 1 meeting today:</p>-->
<!--        <div class="meetingWrapper">-->
<!--          <span class="material-symbols-outlined">schedule</span>-->
<!--          <div> {{ meetingTime }} </div>-->
<!--        </div>-->
<!--        <div class="meetingWrapper">-->
<!--          <span class="material-symbols-outlined">location_on</span>-->
<!--          <div> {{ meetingLocation }} </div>-->
<!--        </div>-->

<!--      </div>-->

<!--  </div>-->

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Ongoing Projects</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in viewerProjects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle">{{ project.projectName }}</div>
          <div class="statusWrapper">
            <div class="projectStatus"> Status: </div>
            <div :style="{ backgroundColor: getRandomColor() }" class="statusColor"></div>
          </div>
        </div>

        <div class="projectDescription">
          <div class="infoTitle">
            <div class="descriptionTitle">Client:</div>
            <div class="descriptionTitle">Team:</div>
            <div class="descriptionTitle">Warehouse:</div>
            <div class="descriptionTitle">Install Date:</div>
          </div>
          <div class="infoValue">
            <div class="descriptionValue">{{ project.clientName }}</div>
            <div class="descriptionValue">{{ project.installTeamId }}</div>
            <div class="descriptionValue">{{ project.usedWarehouseId }}</div>
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
            v-for="(report, index) in viewerReports"
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
        <div class="containerTitle">Send a report</div>
        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>
        <button @click="postReport" class="sendReportButton">Send</button>

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
  width: 150px;
  height: 150px;
  background: #f5f5f5;
  border-radius: 10px;
}

.dayOfTheWeek {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 0.5;
  color: #222;
}

.dayOfTheWeekNum {
  font-size: 3rem;
  font-weight: 800;
  line-height: 1.2;
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
  overflow-x: scroll;
  margin-top: 1rem;
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
}

p {
  margin-top: 1rem;
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
  background: #e5e5e5;
}

</style>