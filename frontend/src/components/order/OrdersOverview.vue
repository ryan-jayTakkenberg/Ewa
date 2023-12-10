<script>
import Order from "@/models/order";
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import OrderRowComponent from "@/components/order/OrderRowComponent.vue";
import SolarPagination from "@/components/general/SolarPagination.vue";

import CreateOrderModal from "@/components/order/order-modals/CreateOrderModal.vue";
import CancelOrderModal from "@/components/order/order-modals/CancelOrderModal.vue";
import ConfirmOrderModal from "@/components/order/order-modals/ConfirmOrderModal.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";

export default {
  name: "OrdersOverview",
  components: {
    ReportComponent,
    ConfirmOrderModal,
    CreateOrderModal,
    CancelOrderModal,
    SolarDropdownMenuItem,
    TitleComponent,
    OrderRowComponent,
    SolarDropdownMenuButton,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    SolarPagination,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching orders
      orders: [...Order.orders],
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
      console.log(this.orders)
      return Math.ceil(this.filterOrders.length / this.itemsPerPage);
    },
    paginatedOrders() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filterOrders.slice(startIndex, endIndex);
    },
    filterOrders() {
      return this.orders.filter(p => {
        return Object.keys(p).some(key => `${p[key]}`.toLowerCase().includes(this.inputValue));
      });
    },
    isActionButtonDisabled() {
      return this.checkedOrders.length === 0;
    },
  },
  methods: {
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      const lastPage = Math.ceil(this.filterOrders.length / this.itemsPerPage);
      if (this.currentPage < lastPage) {
        this.currentPage++;
      }
    },
    async createOrder(createdOrder) {
      this.closeModal();
      let newOrder = await createdOrder.putDatabase();
      if (newOrder) {
        this.orders.push(newOrder);
      }
    },

    // async editUser(updated) {
    //   // Assuming there is only one user in this.selectedProducts
    //   let edited = this.selectedUser;
    //
    //   if (edited) {
    //     let index = this.users.findIndex(p => p.id === edited.id);
    //     edited.injectAttributes(updated);
    //     let user = await edited.putDatabase();
    //
    //     if (user) {
    //       this.users[index] = user;
    //     }
    //   }
    //
    //   this.closeModal();
    // },
    //

    async confirmOrder() {
      this.closeModal();
      const canceledOrder = this.selectedOrder;
      await canceledOrder.confirmDatabase();
    },

    //todo cancel
    // async cancelOrder() {
    //   this.closeModal();
    //   const canceledOrder = this.selectedOrder;
    //   await canceledOrder.cancelDatabase();
    // },
    //
    // async deleteCheckedUsers() {
    //   // Close the modal
    //   this.closeModal();
    //
    //   // Uncheck the selected users in the UsersRowComponent
    //   this.users.forEach(user => {
    //     user.isChecked = false;
    //   });
    //
    //   // Get the IDs of the users to delete
    //   const userIdsToDelete = this.checkedUsers.map(user => user.id);
    //
    //   // Delete users one by one
    //   for (const userId of userIdsToDelete) {
    //     const deletedUserIndex = this.users.findIndex(user => user.id === userId);
    //
    //     if (deletedUserIndex !== -1) {
    //       const deletedUser = this.users[deletedUserIndex];
    //
    //       // Delete user
    //       await deletedUser.delDatabase();
    //
    //       // Remove the user from the users array
    //       this.users.splice(deletedUserIndex, 1);
    //     }
    //   }
    //   // Clear the checkedUsers array
    //   this.checkedUsers = [];
    // },
    // TODO edit checkedUsers oneByONe
    //  Check if there are any selected users
    // if (this.checkedUsers.length > 0) {
    //   const userToEdit = this.checkedUsers[0];
    //   this.openEditModal(userToEdit);
    // this.closeDropdown();
    //
    //   // Remove the first user from the array
    //   this.checkedUsers.splice(0, 1);
    // }
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
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
        <SolarDropdownMenuButton text-button="Action" ref="dropdownButton" :disabled="isActionButtonDisabled">
          <SolarDropdownMenuItem text-menu-item="Confirm Orders" @click="openConfirmMultipleOrdersModal"/>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Orders" @search="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="create-btn" button-text="Create Order" @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable
          :columns="['', 'order','ordered from', 'order date', 'estimated delivery date', 'ordered for team', 'project', 'product', 'status', 'action']">
        <OrderRowComponent
            v-for="(order) in paginatedOrders" :key="order.id" :order="order"
            @confirm="openConfirmModal"
            @report="openReportModal"
            @edit="openEditModal"
            @delete="openCancelModal"
            @toggle="toggleCheckbox(order, $event)"> <!-- Pass user and checkbox state -->
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
  <!--  <EditOrderModal-->
  <!--      v-if="showEditModal"-->
  <!--      :on-close="closeModal"-->
  <!--      :user="selectedUser"-->
  <!--      @edit-order="editOrder"-->
  <!--  />-->

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
