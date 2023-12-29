<script>
import {AdminOverviewAdaptor} from "@/service/admin-overview-adaptor";
import {getId, getUsername} from "@/data";
import OverviewModal from "@/components/overview/OverviewModal.vue";
import NotificationComponent from "@/components/general/NotificationComponent.vue";
import Project from "@/models/project";
import {getAPI, responseOk} from "@/backend";
import Warehouse from "@/models/warehouse";
import SolarTable from "@/components/general/SolarTable";
import Order from "@/models/order";

export default {

  name: "AdminOverviewComponent",
  components: {
    OverviewModal,
    SolarTable,
  },
  inject: ['reportService', 'userService', 'projectService'],

  data() {
    return {
      productsSold: '75',
      unresolvedReports: '0',
      warehousesLowStock: '3',
      globalTotalStock: '350',
      username: getUsername(),
      projects: [],
      reports: [],
      selectedReports: [],
      reportBody: "",
      receiverId: "",
      senderId: getId(),
      senderUsername: getUsername(),
      users: [],
      modal: false,
      ongoingProjects: "",
      warehouses: Warehouse.warehouses,
      orders: Order.orders,
    }
  },

  mounted() {
    this.fetchAllUsers();
    this.loadOngoingProjectsCount()
    this.fetchReports()
    this.fetchAllProjects()
  },

  methods: {
    async fetchAllUsers() {
      this.users = await this.userService.fetchAll();
    },

    async fetchAllProjects() {
      try {
        const response = await getAPI("api/projects");

        if (!responseOk(response)) {
          console.error('Error fetching projects:', response.data);
          return;
        }

        if (!Array.isArray(response.data)) {
          console.error('Invalid JSON array received:', response.data);
          return;
        }

        this.projects = response.data.map(data => new Project(
            data.projectId,
            data.projectName,
            data.clientName,
            data.installDate,
            data.notes,
            data.team
        ));
      } catch (error) {
        console.error('An error occurred while fetching projects:', error);
      }
    },

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
        // console.log('Successfully posted report:', postedReport.data);
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

          // console.log('Successfully deleted report:', deletedReport);

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
      this.unresolvedReports = this.loadOngoingProjectsCount()

    },

    replyReport() {
      if (this.selectedReports.length !== 1) {
        alert("Please select exactly one report to reply to.");
        return;
      }

      // Set the receiverId to the id of the selected report's sender
      this.receiverId = this.selectedReports[0].senderId;

      // Optionally, you can update the UI to reflect the selected user in the dropdown
      // For example, you can find the index of the selected user and set it as the selected index
      const selectedIndex = this.filteredUsers.findIndex(user => user.id === this.receiverId);
      if (selectedIndex !== -1) {
        this.$refs.selectUser.selectedIndex = selectedIndex;
      }
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

    getStatusColor(project) {
      const today = new Date();
      const installDate = new Date(project.installDate);
      return installDate < today ? '#FF0000' : '#5DDB88'; // Red if install date has passed, Green otherwise
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

    productTotals() {
      const totals = {};

      this.warehouses.forEach(warehouse => {
        warehouse.products.forEach(product => {
          const id = product['product']['id'];
          console.log(product['product']);

          if (!totals[id]) {
            totals[id] = {
              name: product['product']['name'],
              inWarehouse: 0,
              inOrders: 0,
            };
          }

          totals[id].inWarehouse += product.amount;
        });
      });

      this.orders.forEach(order => {
        order.products.forEach(product => {
          const id = product['product']['id'];
          console.log(product['product']);

          if (!totals[id]) {
            totals[id] = {
              name: product['product']['name'],
              inWarehouse: 0,
              inOrders: 0,
            };
          }

          totals[id].inOrders += product.amount;
        });
      });

      return totals;
    },

  },

  watch: {
    '$route': {
      immediate: true,
      handler(to, from) {
        // Call your data fetching methods here
        this.loadOngoingProjectsCount();
        this.fetchReports();

      }
    }
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

  <!--- Persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="header">
      <h1>Hi, {{ capitalizeFirstLetter(username) }}</h1>

      <div class="dateContainer">
        <div class="dayOfTheWeek">{{ dayOfTheWeek }}</div>
        <div class="dayOfTheWeekNum">{{ numberOfTheDay }}</div>
      </div>
    </div>

  </div>

  <!--- Agenda ---------------------------------------------------------------------------------->
  <div class="sectionContainer agenda">

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

  <!--- inventory ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Inventory</h1>

    <SolarTable :columns="['product', 'quantity', 'ordered']">
      <tr class="border-gray-100 border-b text-base font-medium" v-for="(product, index) in this.productTotals" v-bind:key="index">
        <!--Product Name -->
        <td class="pl-6 text-gray-900 whitespace-nowrap">{{ product.name }}</td>
        <!--Products In Warehouse -->
        <td class="px-6 py-4" :class="{'text-red-600': product.inWarehouse + product.inOrders < 10}">{{ product.inWarehouse }}</td>
        <!--Products In Warehouse -->
        <td class="px-6 py-4" :class="{'text-red-600': product.inWarehouse < 10 && !product.inOrders}">{{ product.inOrders }}</td>
      </tr>
    </SolarTable>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Ongoing Projects</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in projects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle">{{ project.projectName }}</div>
          <div class="statusWrapper">
<!--            <div class="projectStatus"> Status: </div>-->
<!--            <div class="statusColor" :style="{ background: getStatusColor(project) }"></div>-->
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
            <button class="replyReport" @click="replyReport">
              <span class="material-symbols-outlined">reply</span>
            </button>
            <button class="deleteReport" @click="showModal">
              <span class="material-symbols-outlined">delete</span>
            </button>
            <button class="filterReport">
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

        <div class="wrapper">
          <label>Send a report to:</label>
          <select v-model="receiverId" class="reportReceiver" ref="selectUser">
            <option v-for="user in filteredUsers" :value="user.id" :key="user.id">{{ user.name }}</option>
          </select>
        </div>

        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>
        <button @click="postReport" class="sendReportButton">Send</button>

      </div>

    </div>

  </div>


</template>

<style scoped>

.insightContainer:nth-child(4) {
  border-right: 2px solid #e5e5e5;
  padding-right: 1rem;
  margin-right: 1rem;
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

.filterReport,
.deleteReport,
.replyReport {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  background: none;
  outline: none;
  cursor: pointer;
}

.filterReport:hover,
.deleteReport:hover,
.replyReport:hover {
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

label {
  font-weight: 500;
  width: 35%;
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

