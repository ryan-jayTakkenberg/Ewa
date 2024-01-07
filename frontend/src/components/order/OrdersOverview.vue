<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import OrderRowComponent from "@/components/order/OrderRowComponent.vue";
import SolarPagination from "@/components/general/SolarPagination.vue";

import CreateOrderModal from "@/components/order/order-modals/CreateOrderModal.vue";
import EditOrderModal from "@/components/order/order-modals/EditOrderModal.vue";
import CancelOrderModal from "@/components/order/order-modals/CancelOrderModal.vue";
import ConfirmOrderModal from "@/components/order/order-modals/ConfirmOrderModal.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import Order from "@/models/order";


export default {
  name: "OrdersOverview",
  components: {
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
    SolarPagination,
  },
  inject: ['orderService', 'product_OrderService'],
  data() {
    return {
      filterValue: '', // Store the input value for searching orders
      selectedOrder: null,  // The selected order for editing / deleting
      checkedOrders: [], // A list of the selected orders for editing multiple orders at once
      showCreateModal: false,
      showEditModal: false,
      showCancelModal: false,
      showConfirmModal: false,
      showReportModal: false,
      currentPage: 1,
      itemsPerPage: 10,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredOrders.length / this.itemsPerPage);
    },
    paginatedOrders() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredOrders.slice(startIndex, endIndex);
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
    updateTable() {
      this.filterValue += ' ';
      this.filterValue = this.filterValue.trim();
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
    async createOrder(createdOrder, orderedProducts) {
      this.closeModal();

      createdOrder = await this.orderService.asyncCreate(createdOrder);
      for (const orderedProduct of orderedProducts) {
        // Set orderId for each orderedProduct in orderedProducts
        orderedProduct.orderId = createdOrder.id;
        // Convert object to json and
        const orderedProductJson = JSON.stringify(orderedProduct);
        // Create orderedProduct
        await this.product_OrderService.asyncCreate(orderedProductJson);
      }
      this.updateTable();
    },

    // todo edit order
    async editOrder(updatedOrder, updatedOrderedProducts) {
      updatedOrder = await this.orderService.asyncUpdate(updatedOrder);
      this.closeModal();
      for (const updatedOrderedProduct of updatedOrderedProducts) {
        // Set orderId for each orderedProduct in orderedProducts
        updatedOrderedProduct.orderId = updatedOrder.id;
        // Convert object to json and
        const orderedProductJson = JSON.stringify(updatedOrderedProduct);
        // Create orderedProduct
        await this.product_OrderService.asyncUpdate(orderedProductJson);
      }

      this.updateTable();
    },

    // todo Deletion possible or not ?
    // async deleteOrder() {
    //   this.closeModal();
    //   const deletedOrder = this.selectedOrder;
    //   await this.orderService.asyncDeleteById(deletedOrder.id);
    //   this.updateTable();
    // },

    async confirmOrder() {
      this.closeModal();
      const confirmedOrder = this.selectedOrder;
      await this.orderService.asyncConfirmById(confirmedOrder.id);
    },

    async cancelOrder() {
      this.closeModal();
      const canceledOrder = this.selectedOrder;
      await this.orderService.asyncCancelById(canceledOrder.id);
    },


    handleFilterValueChange(value) {
      console.log(value);
      this.filterValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
    },
    openCreateModal() {
      this.showCreateModal = true;
    },
    openEditModal(order) {
      this.selectedOrder = order;
      this.showEditModal = true;
    },
    openCancelModal(order) {
      this.selectedOrder = order;
      this.showCancelModal = true;
    },
    openConfirmModal(order) {
      this.selectedOrder = order;
      this.showConfirmModal = true;
    },
    openReportModal() {
      this.showReportModal = true;
    },
    closeModal() {
      this.showCreateModal = false;
      this.showEditModal = false;
      this.showCancelModal = false;
      this.showConfirmModal = false;
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
        <SolarSearchbar class="ml-2" place-holder="Search For Orders" @search="handleFilterValueChange"></SolarSearchbar>
        <SolarButton class="create-btn" button-text="Create Order" @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable
          :columns="['', 'order','ordered from', 'order date', 'estimated delivery date', 'ordered for team', 'products', 'status', 'action']">
        <OrderRowComponent
            v-for="(order) in paginatedOrders" :key="order.id" :order="order"
            @toggle="toggleCheckbox(order, $event)"
            @edit="openEditModal"
            @cancel="openCancelModal"
            @confirm="openConfirmModal"
            @report="openReportModal"
        >
        </OrderRowComponent>
      </SolarTable>
      <SolarPagination :current-page="currentPage" :total-pages="totalPages" @previous="prevPage" @next="nextPage"/>
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
