<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
// import DeleteUserModal from "@/components/user/user-modals/DeleteUserModal.vue";
// import UpdateUserModal from "@/components/user/user-modals/UpdateUserModal.vue";
// import CreateUserModal from "@/components/user/user-modals/CreateUserModal.vue";
// import DeleteMultipleUsersModal from "@/components/user/user-modals/DeleteMultipleUsersModal.vue";
import SolarPagination from "@/components/general/SolarPagination.vue";
import Order from "@/models/order";
import OrderRowComponent from "@/components/order/OrderRowComponent.vue";

export default {
  name: "OrdersOverview",
  components: {
    SolarDropdownMenuItem,
    TitleComponent,
    OrderRowComponent,
    SolarDropdownMenuButton,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    SolarPagination,
  },
  created() {
    this.fetchOrders();
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching orders
      orders: [...Order.orders],
      selectedOrder: null,  // The selected order for editing / deleting
      checkedOrders: [], // A list of the selected orders for editing multiple orders at once
      showCreateModal: false,
      showUpdateModal: false,
      showDeleteModal: false,
      showDeleteMultipleModal: false,
      currentPage: 1,
      itemsPerPage: 10,
    };
  },
  computed: {
    totalPages() {
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
    fetchOrders() {
      if (!this.orders?.length) {
        // Keep updating the list if the database has not returned all the data yet
        const fetchingInterval = setInterval(() => {
          if (!Order.fetching) {
            this.orders = [...Order.orders];
            clearInterval(fetchingInterval);
          }
        }, 100);
      }
      console.log(this.orders)
    },
    // async createUser(createdUser) {
    //   this.closeModal();
    //   let newUser = await createdUser.putDatabase();
    //   if (newUser) {
    //     this.users.push(newUser);
    //   }
    // },
    //
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
    // async deleteUser() {
    //   this.closeModal();
    //   const deletedUser = this.selectedUser;
    //   this.users = this.users.filter(user => user.id !== deletedUser.id);
    //   await deletedUser.delDatabase();
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
    // openCreateModal() {
    //   this.showCreateModal = true;
    // },
    // openEditModal(user) {
    //   this.selectedUser = user;
    //   this.showUpdateModal = true;
    // },
    // openDeleteModal(user) {
    //   this.selectedUser = user;
    //   this.showDeleteModal = true;
    // },
    // openDeleteMultipleUsersModal() {
    //   console.log("test before");
    //   this.showDeleteMultipleModal = true;
    //   console.log("test after");
    //   this.closeDropdown();
    // },
    closeModal() {
      this.showCreateModal = false;
      this.showUpdateModal = false;
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
          <SolarDropdownMenuItem text-menu-item="Confirm Orders" @click="openConfirmMultipleOrdersModal"/>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Orders" @search="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Create Order" @click="openCreateOrderModal"></SolarButton>
      </div>
      <SolarTable
          :columns="['order number', 'order date', 'estimated delivery date', 'ordered for warehouse', 'total price', 'status', 'products', 'action']">
        <OrderRowComponent
            v-for="(order) in paginatedOrders" :key="order.id" :order="order"
            @edit="openEditOrderModal"
            @delete="openDeleteOrderModal"
            @toggle="toggleCheckbox(order, $event)"> <!-- Pass user and checkbox state -->
        </OrderRowComponent>
      </SolarTable>
      <SolarPagination @previous="prevPage" :current-page="currentPage" :total-pages="totalPages" @next="nextPage"/>
    </div>
  </div>

  <!--  &lt;!&ndash; Conditionally render modals based on boolean modal states &ndash;&gt;-->
  <!--  <CreateUserModal-->
  <!--      v-if="showCreateModal"-->
  <!--      :on-close="closeModal"-->
  <!--      @create-user="createUser"-->
  <!--  />-->
  <!--  <UpdateUserModal-->
  <!--      v-if="showUpdateModal"-->
  <!--      :on-close="closeModal"-->
  <!--      :user="selectedUser"-->
  <!--      @update-user="editUser"-->
  <!--  />-->
  <!--  <DeleteUserModal-->
  <!--      v-if="showDeleteModal"-->
  <!--      :on-close="closeModal"-->
  <!--      :user="selectedUser"-->
  <!--      @delete-user="deleteUser"-->
  <!--  />-->
  <!--  <DeleteMultipleUsersModal-->
  <!--      v-if="showDeleteMultipleModal"-->
  <!--      :on-close="closeModal"-->
  <!--      :users-to-delete="checkedUsers"-->
  <!--      @delete-users="deleteCheckedUsers"-->
  <!--  />-->
</template>

<style scoped>
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
</style>
