<template>

  <OverviewModal
      v-if="modal"
      @confirm-delete="deleteReport"
      @cancel-delete="closeModal"
      :selectedReports="selectedReports"
  />

  <!--- user ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <div class="header">
      <h1 class="hiText">Hi, {{ capitalizeFirstLetter(username) }}</h1>

      <div class="dateContainer">
        <div class="dayOfTheWeek">{{ dayOfTheWeek }}</div>
        <div class="dayOfTheWeekNum">{{ numberOfTheDay }}</div>
      </div>
    </div>

  </div>

  <!--- forecasting ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <div class="forecastingWrapper">
      <div class="dataContainer">
        <p class="title">Products to be installed:</p>
        <p class="value">{{ productsSold }}</p>
      </div>

      <div class="dataContainer">
        <p class="title">Total Ongoing Projects:</p>
        <p class="value">{{ allProjects.length }}</p>
      </div>

      <div class="dataContainer">
        <p class="title">Reports:</p>
        <p class="value">{{ reports.length }}</p>
      </div>

      <div class="dataContainer">
        <p class="title">Global Amount of Stock:</p>
        <p class="value">{{ globalTotalStock }}</p>
      </div>

      <div class="dataContainer">
        <p class="title">Amount of warehouses with low stock:</p>
        <p class="value">{{ warehousesLowStock }}</p>
      </div>
    </div>

  </div>

  <!--- inventory ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="inventoryTitle">Inventory</h1>

    <SolarTable :columns="['product', 'quantity', 'ordered']">
      <tr class="border-gray-100 border-b text-base font-medium" v-for="(product, index) in this.productTotals" v-bind:key="index">
        <!--Product Name -->
        <td class="pl-6 text-gray-900 whitespace-nowrap">{{ product?.name }}</td>
        <!--Products In Warehouse -->
        <td class="px-6 py-4" :class="{'text-red-600': product.inWarehouse + product.inOrders < 10}">{{ product.inWarehouse }}</td>
        <!--Products In Warehouse -->
        <td class="px-6 py-4" :class="{'text-red-600': product.inWarehouse < 10 && !product.inOrders}">{{ product.inOrders }}</td>
      </tr>
    </SolarTable>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">All ongoing projects:</h1>

    <div class="projectContainer" v-if="allProjects?.length > 0">

      <div class="projectWrapper" v-for="(project, index) in allProjects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle">Project: {{ project.projectName }}</div>
        </div>

        <div class="projectDescription">
          <div class="infoTitleProjects">
            <div class="descriptionTitle">Client:</div>
            <div class="descriptionTitle">Install Team:</div>
            <div class="descriptionTitle">Install Date:</div>
          </div>
          <div class="infoValueProjects">
            <div class="descriptionValue">{{ project.clientName }}</div>
            <div class="descriptionValue">{{ project.team.name }}</div>
            <div class="descriptionValue">{{ project.installDate }}</div>
          </div>
        </div>

      </div>
    </div>

    <div class="noProjectsMessage" v-else>There are currently no projects</div>

  </div>

  <!--- reports ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

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

          <div class="selectDropdown">
            <select v-model="receiverId" class="reportReceiverSelect" ref="selectUser">
              <option v-for="user in this.availableUsers" :value="user.id" :key="user.id">{{ capitalizeFirstLetter(user.name) }}</option>
            </select>
            <span class="material-symbols-outlined arrow">expand_more</span>
          </div>

        </div>

        <textarea v-model="reportBody" placeholder="Type your report here..." class="reportInput"></textarea>
        <button @click="postReport" class="sendReportButton">Send</button>

      </div>

    </div>

  </div>

</template>

<script>

import {getId, getUsername} from "@/data";
import OverviewModal from "@/components/overview/OverviewModal.vue";
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
  inject: ['reportService', 'userService', 'projectService', 'warehouseService'],

  data() {
    return {
      productsSold: '75',
      warehousesLowStock: '3',
      globalTotalStock: '350',
      username: getUsername(),
      allProjects: [],
      reports: [],
      selectedReports: [],
      reportBody: "",
      receiverId: "",
      userId: getId(),
      senderId: getId(),
      senderName: getUsername(),
      allUsers: [],
      availableUsers: [],
      modal: false,
      warehouses: Warehouse.warehouses,
      orders: Order.orders,
    }
  },

  mounted() {
    this.fetchAllUsers()
    this.fetchUserReports()
    this.fetchAllProjects()
    this.fetchAndUpdateWarehouseData()
  },

  methods: {

    async fetchAndUpdateWarehouseData() {
      try {
        const warehousesData = await this.warehouseService.asyncFindAll();
        console.log('Fetched warehouses data:', warehousesData);

        // Ensure the data is an array before setting it to this.warehouses
        if (Array.isArray(warehousesData)) {
          this.warehouses = warehousesData;
          this.calculateTotalStock();
          this.calculateLowStockWarehouses();
        } else {
          console.error('Expected warehouses data to be an array, received:', warehousesData);
        }
      } catch (error) {
        console.error('Error fetching warehouses data:', error);
      }
    },


    calculateTotalStock() {
      let totalStock = 0;

      this.warehouses.forEach(warehouse => {
        warehouse.products.forEach(product => {
          totalStock += product.amount;
        });
      });

      this.globalTotalStock = totalStock.toString();
    },

    calculateLowStockWarehouses() {
      let lowStockCount = 0;

      this.warehouses.forEach(warehouse => {
        let currentStorage = 0;

        // Calculate current storage for each warehouse
        warehouse.products.forEach(product => {
          currentStorage += product.amount;
        });

        // Check if the current storage is less than the minimum storage
        if (currentStorage < warehouse.minStorage) {
          lowStockCount++;
        }
      });

      this.warehousesLowStock = lowStockCount.toString();
    },

    async fetchAllUsers() {
      this.allUsers = await this.userService.fetchAll();
      // console.log('all users: ', [...this.allUsers]);
      this.availableUsers = this.allUsers.filter(user => user.id !== this.userId);
      // console.log('available users: ', [...this.availableUsers]);
    },

    async fetchUserReports() {
      this.reports = await this.reportService.fetchReports();
      // console.log('Fetched reports: ', [...this.reports]);
    },

    async fetchAllProjects() {
      this.allProjects = await this.projectService.asyncFindAll();
      // console.log('all projects: ', [...this.allProjects]);
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

      // console.log('Your current reports after delete: ', [...this.reports]);
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

      // TODO geeft error
      // this.orders.forEach(order => {
      //   order.products.forEach(product => {
      //     const id = product['product']['id'];
      //     console.log(product['product']);
      //
      //     if (!totals[id]) {
      //       totals[id] = {
      //         name: product['product']['name'],
      //         inWarehouse: 0,
      //         inOrders: 0,
      //       };
      //     }
      //
      //     totals[id].inOrders += product.amount;
      //   });
      // });

      return totals;
    },

  },



}

</script>

<style scoped>

.sectionContainer {
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
}

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
  padding-top: 5px;
  background: var(--col-light-grey);
  border-radius: 10px;
}

.dayOfTheWeek {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--col-black);
}

.dayOfTheWeekNum {
  font-size: 2rem;
  font-weight: 800;
  color: var(--col-black);
}

.forecastingWrapper {
  display: flex;
  justify-content: space-between;
}

.dataContainer {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
}

.title {
  font-size: 1rem;
  font-weight: 400;
}

.value {
  font-size: 3rem;
  font-weight: 600;
}

.inventoryTitle {
  text-align: left;
  font-size: var(--font-size-medium);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
  margin-bottom: 2rem;
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

.noProjectsMessage {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-medium);
  margin-top: 2rem;
}

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
  min-width: 400px;
  height: auto;
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
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
  height: 25px;
}

.descriptionValue {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-medium);
  color: var(--col-black);
  height: 25px;
}

.reportsContainerWrapper {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  width: 100%;
  margin-top: 2rem;
}

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

.reportContainerTitle {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
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
  height: auto;
}

.sendReportInput {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
  width: 200px;
}

.selectDropdown {
  position: relative;
  height: 50px;
  width: 100%;
  border-radius: 5px;
  padding: 10px;
}

.material-symbols-outlined.arrow {
  position: absolute;
  top: 28%;
  right: 25px;
  color: var(--col-black);
  font-variation-settings:
      'FILL' 0,
      'wght' 500,
      'GRAD' 0,
      'opsz' 40
}

.reportReceiverSelect {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  padding-left: 15px;
  cursor: pointer;
  -webkit-appearance: none;
  -moz-appearance: none;
}

.reportInput {
  height: 50%;
  padding: 0.5rem;
  border-radius: 5px;
  resize: vertical;
  min-height: 20%;
  max-height: 70%;
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

.material-symbols-outlined {
  font-variation-settings:
      'FILL' 0,
      'wght' 500,
      'GRAD' 0,
      'opsz' 40
}

@media only screen and (max-width: 1400px) {

  .dataContainer {
    justify-content: space-between;
    width: 15%;
  }

  .title {
    font-size: 1rem;
  }

  .value {
    font-size: 2rem;
  }
}

@media only screen and (max-width: 950px) {

  .dataContainer {
    justify-content: space-between;
    width: 15%;
  }

  .title {
    font-size: 0.8rem;
  }

  .value {
    font-size: 2rem;
  }
}

@media only screen and (max-width: 700px) {

  .dataContainer {
    justify-content: space-between;
    width: 10%;
  }
}

</style>

