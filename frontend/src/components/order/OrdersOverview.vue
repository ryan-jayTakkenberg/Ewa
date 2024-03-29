<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import OrderRowComponent from "@/components/order/OrderRowComponent.vue";

import CreateOrderModal from "@/components/order/order-modals/CreateOrderModal.vue";
import EditOrderModal from "@/components/order/order-modals/EditOrderModal.vue";
import CancelOrderModal from "@/components/order/order-modals/CancelOrderModal.vue";
import ConfirmOrderModal from "@/components/order/order-modals/ConfirmOrderModal.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import Order from "@/models/order";
import {isAdmin} from "@/data";
import DeleteOrderModal from "@/components/order/order-modals/DeleteOrderModal.vue";
import DeleteMultipleUsersModal from "@/components/order/order-modals/DeleteMultipleOrdersModal.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";


export default {
  name: "OrdersOverview",
  components: {
    SolarDropdownMenuButton, SolarDropdownMenuItem,
    DeleteMultipleUsersModal,
    DeleteOrderModal,
    ReportComponent,
    ConfirmOrderModal,
    CreateOrderModal,
    EditOrderModal,
    CancelOrderModal,
    TitleComponent,
    OrderRowComponent,
    SolarSearchbar,
    SolarButton,
    SolarTable,
  },
  inject: ['orderService'],
  data() {
    return {
      filterValue: '', // Store the input value for searching orders
      selectedOrder: null,  // The selected order for editing / deleting
      checkedOrders: [], // A list of the selected orders for editing multiple orders at once
      showCreateModal: false,
      showEditModal: false,
      showDeleteModal: false,
      showCancelModal: false,
      showConfirmModal: false,
      showDeleteMultipleModal: false,
      showReportModal: false,
      currentPage: 1,
      itemsPerPage: 10,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredOrders.length / this.itemsPerPage);
    },
    filteredOrders() {
      return Order.orders.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.filterValue)) {
            return true;
          }
        }
        return false;
      });
    },
    isActionButtonDisabled() {
      return this.checkedOrders.length === 0;
    },
  },
  methods: {
    isAdmin,
    updateTable() {
      const oldFilterValue = this.filterValue;
      this.filterValue += Math.random().toString();
      setTimeout(() => this.filterValue = oldFilterValue, 0);
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      const lastPage = Math.ceil(this.filteredOrders.length / this.itemsPerPage);
      if (this.currentPage < lastPage) {
        this.currentPage++;
      }
    },

    async createOrder(createdJson) {
      console.log(createdJson)
      this.closeModal();
      let createdOrder = await this.orderService.asyncCreate(createdJson)
      console.log(createdOrder)
      this.updateTable();
    },

    async editOrder(updateJson) {
      this.closeModal();
      let updatedOrder = await this.orderService.asyncUpdate(updateJson);
      this.updateTable();
    },

    async deleteOrder() {
      this.closeModal();
      const deletedOrder = this.selectedOrder;
      await this.orderService.asyncDeleteById(deletedOrder.id);
      this.updateTable();
    },

    async deleteCheckedOrders() {
      this.closeModal();
      for (const order of this.checkedOrders) {
      order.isChecked = false;
        try {
          await this.orderService.asyncDeleteById(order.id);
        } catch (error) {
          console.error(`Error deleting user with ID ${order.id}:`, error);
        }
      }
      this.checkedOrders = [];
      this.updateTable();
    },

    async confirmOrder() {
      this.closeModal();
      const confirmedOrder = this.selectedOrder;
      await this.orderService.asyncConfirmById(confirmedOrder.id);
      // Update status display by refreshing the order rendering
      this.updateTable();
    },

    async cancelOrder() {
      this.closeModal();
      const canceledOrder = this.selectedOrder;
      await this.orderService.asyncCancelById(canceledOrder.id);
      // Update status display by refreshing the order rendering
      this.updateTable();
    },

    handleFilterValueChange(value) {
      this.filterValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
    },
    openCreateModal() {
      this.showCreateModal = true;
    },
    openEditModal(order) {
      this.selectedOrder = order;
      this.showEditModal = true;
    },
    openDeleteModal(order){
      this.selectedOrder = order;
      this.showDeleteModal = true;
    },
    openDeleteMultipleModal(order){
      this.selectedOrder = order;
      this.showDeleteMultipleModal = true;
    },
    openCancelModal(order) {
      this.selectedOrder = order;
      this.showCancelModal = true;
    },
    openConfirmModal(order) {
      this.selectedOrder = order;
      this.showConfirmModal = true;
    },
    closeModal() {
      this.showCreateModal = false;
      this.showEditModal = false;
      this.showCancelModal = false;
      this.showConfirmModal = false;
      this.showDeleteModal = false;
      this.showDeleteMultipleModal = false;
    },
    toggleCheckbox(order) {
      const orderIndex = this.checkedOrders.findIndex(u => u.id === order.id);

      if (orderIndex !== -1) {
        // User is checked, so remove them from the array
        this.checkedOrders = this.checkedOrders.filter(u => u.id !== order.id);
      } else {
        // User is not checked, so add them to the array
        this.checkedOrders = [...this.checkedOrders, order];
      }
      console.log(this.checkedOrders);
    },
    closeDropdown() {
      this.$refs.dropdownButton.hideDropdown(); // Call the hideDropdown method from the ref
    },
    findSelectedRouteFromParam(route) {
      if (route && route.params.id) {
        const userId = parseInt(route.params.id);
        return this.users.find(user => user.id === userId);
      }
      return null;
    },
  },
  watch: {
    '$route'(to) {
      this.selectedOrder = this.findSelectedRouteFromParam(to);
    }
  }
}
</script>

<template>
  <TitleComponent class="header" page-title="Orders"></TitleComponent>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton text-button="Action" ref="dropdownButton" :disabled="isActionButtonDisabled">
          <SolarDropdownMenuItem text-menu-item="Delete Orders" @click="openDeleteMultipleModal"/>
        </SolarDropdownMenuButton>
        <SolarSearchbar class="ml-2" place-holder="Search For Orders"
                        @search="handleFilterValueChange"></SolarSearchbar>
        <SolarButton v-if="isAdmin()" class="create-btn" button-text="Create Order"
                     @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable
          :columns="['', 'order','ordered from', 'order date', 'estimated delivery date', 'ordered for team', 'products', 'status', 'action']">
        <OrderRowComponent
            v-for="(order) in filteredOrders" :key="order.id" :order="order"
            @toggle="toggleCheckbox(order, $event)"
            @edit="openEditModal"
            @cancel="openCancelModal"
            @confirm="openConfirmModal"
            @delete="openDeleteModal"
        >
        </OrderRowComponent>
      </SolarTable>
<!--      <SolarPagination :current-page="currentPage" :total-pages="totalPages" @previous="prevPage" @next="nextPage"/>-->
    </div>
  </div>

  <!-- Conditionally render modals based on boolean modal states -->
  <CreateOrderModal
      v-if="showCreateModal"
      :on-close="closeModal"
      @create="createOrder"
  />
  <EditOrderModal
      v-if="showEditModal"
      :on-close="closeModal"
      :order="selectedOrder"
      @edit="editOrder"
  />

  <CancelOrderModal
      v-if="showCancelModal" :order="selectedOrder"
      :on-close="closeModal"
      @cancel="cancelOrder"
  />

  <ConfirmOrderModal
      v-if="showConfirmModal" :order="selectedOrder"
      :on-close="closeModal"
      @confirm="confirmOrder"
  />

  <DeleteMultipleUsersModal
      v-if="showDeleteMultipleModal" :orders-to-delete="checkedOrders"
      on-close="closeModal"
      @delete="deleteCheckedOrders"

  />

  <DeleteOrderModal
      v-if="showDeleteModal" :order="selectedOrder"
      :on-close="closeModal"
      @delete="deleteOrder"
  />

  <ReportComponent
      v-if="showReportModal"
  />
</template>

<style scoped>

.create-btn {
  margin-left: auto;
  margin-right: 0.5rem;
}

.header {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  background-color: white;
}

.action-row {
  display: flex;
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}

@media only screen and (max-width: 750px) {
  .create-btn {
    margin-right: 0.5rem;
    margin-left: 0.5rem;
  }
}
</style>
