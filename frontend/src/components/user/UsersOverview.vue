<script>
import User from "@/models/user";

import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import UsersRowComponent from "@/components/user/UsersRowComponent.vue";
import SolarPagination from "@/components/general/SolarPagination.vue";

import CreateUserModal from "@/components/user/user-modals/CreateUserModal.vue";
import EditUserModal from "@/components/user/user-modals/EditUserModal.vue";
import DeleteUserModal from "@/components/user/user-modals/DeleteUserModal.vue";
import DeleteMultipleUsersModal from "@/components/user/user-modals/DeleteMultipleUsersModal.vue";

export default {
  name: "UsersOverview",
  components: {
    SolarPagination,
    TitleComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    UsersRowComponent,
    CreateUserModal,
    EditUserModal,
    DeleteUserModal,
    DeleteMultipleUsersModal,
  },
  inject: ['userService'],
  data() {
    return {
      filterValue: '', // Store the input value for searching
      selectedUser: null,  // The selected user for editing / deleting
      checkedUsers: [], // A list of the selected users for editing / deleting multiple users at once
      currentPage: 1, // Tracking the current page for navigation
      itemsPerPage: 10, // Total displayed items per page
      showCreateModal: false,
      showUpdateModal: false,
      showDeleteModal: false,
      showDeleteMultipleModal: false,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    },
    isActionButtonDisabled() {
      return this.checkedUsers.length === 0;
    },
    paginatedUsers() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredUsers.slice(startIndex, endIndex);
    },
    filteredUsers() {
      return User.users.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.filterValue)) {
            return true;
          }
        }
        return false;
      });
    },
  },
  methods: {
    getSelected() {
      return User.users.filter(p => this.checkedUsers.includes(p.id));
    },
    updateTable() {
      this.filterValue += ' ';
      this.filterValue = this.filterValue.trim();
    },
    async createUser(createdUser) {
      this.closeModal();
      await this.userService.asyncCreate(createdUser);
      this.updateTable();
    },
    async editUser(updated) {
      this.closeModal();
      await this.userService.asyncUpdate(updated);
      this.updateTable();
    },
    async deleteUser() {
      this.closeModal();
      const deletedUser = this.selectedUser;
      await this.userService.asyncDeleteById(deletedUser.id);
      this.updateTable();
    },
    async deleteCheckedUsers() {
      this.closeModal();
      for (const user of this.checkedUsers) {
        user.isChecked = false;
        try {
          await this.userService.asyncDeleteById(user.id);
        } catch (error) {
          console.error(`Error deleting user with ID ${user.id}:`, error);
        }
      }
      this.checkedUsers = [];
      this.updateTable();
    },
    handleFilterValueChange(value) {
      this.filterValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
    },
    openCreateModal() {
      this.showCreateModal = true;
    },
    openEditModal(user) {
      this.selectedUser = user;
      this.showUpdateModal = true;
    },
    openDeleteModal(user) {
      this.selectedUser = user;
      this.showDeleteModal = true;
    },
    openDeleteMultipleUsersModal() {
      this.showDeleteMultipleModal = true;
      this.closeDropdown();
    },
    closeModal() {
      this.showCreateModal = false;
      this.showUpdateModal = false;
      this.showDeleteModal = false;
      this.showDeleteMultipleModal = false;
    },
    toggleCheckbox(user) {
      const userIndex = this.checkedUsers.findIndex(u => u.id === user.id);

      if (userIndex !== -1) {
        // User is checked, so remove them from the array
        this.checkedUsers = this.checkedUsers.filter(u => u.id !== user.id);
      } else {
        // User is not checked, so add them to the array
        this.checkedUsers = [...this.checkedUsers, user];
      }
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
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      const lastPage = Math.ceil(this.filteredUsers.length / this.itemsPerPage);
      if (this.currentPage < lastPage) {
        this.currentPage++;
      }
    },
  },
}

</script>

<template>

  <TitleComponent class="header" page-title="Users"></TitleComponent>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton text-button="Action" ref="dropdownButton" :disabled="isActionButtonDisabled">
          <SolarDropdownMenuItem text-menu-item="Delete Users" @click="openDeleteMultipleUsersModal"/>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Users" @search="handleFilterValueChange"></SolarSearchbar>
        <SolarButton class="create-btn" button-text="Create User" @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable :columns="[' ', 'User', 'Function', 'Team', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user) in paginatedUsers" :key="user.id" :user="user"
            @edit="openEditModal"
            @delete="openDeleteModal"
            @toggle="toggleCheckbox(user, $event)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
      <SolarPagination @previous="prevPage" :current-page="currentPage" :total-pages="totalPages" @next="nextPage"/>
    </div>
  </div>
  <!-- Conditionally render modals based on boolean modal states -->
  <CreateUserModal v-if="showCreateModal" :on-close="closeModal" @create-user="createUser"/>
  <EditUserModal v-if="showUpdateModal" :on-close="closeModal" :user="selectedUser" @edit-user="editUser"/>
  <DeleteUserModal v-if="showDeleteModal" :on-close="closeModal" :user="selectedUser" @delete-user="deleteUser"/>
  <DeleteMultipleUsersModal v-if="showDeleteMultipleModal" :on-close="closeModal" :users-to-delete="checkedUsers"
      @delete-users="deleteCheckedUsers"/>
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
