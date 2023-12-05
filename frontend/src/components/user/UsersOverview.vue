<script>
import User from "@/models/user";
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import DeleteUserModal from "@/components/user/user-modals/DeleteUserModal.vue";
import UpdateUserModal from "@/components/user/user-modals/UpdateUserModal.vue";
import CreateUserModal from "@/components/user/user-modals/CreateUserModal.vue";
import DeleteMultipleUsersModal from "@/components/user/user-modals/DeleteMultipleUsersModal.vue";
import UsersRowComponent from "@/components/user/UsersRowComponent.vue";
import SolarPagination from "@/components/general/SolarPagination.vue";
import {getId} from "@/data";
import NotificationComponent from "@/components/general/NotificationComponent.vue";

export default {
  name: "UsersOverview",
  components: {
    NotificationComponent,
    SolarPagination,
    TitleComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    UsersRowComponent,
    CreateUserModal,
    UpdateUserModal,
    DeleteUserModal,
    DeleteMultipleUsersModal,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching users
      users: [...User.users].filter(user => user.id !== getId()),
      selectedUser: null,  // The selected user for editing / deleting
      checkedUsers: [], // A list of the selected users for editing / deleting multiple users at once
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
      return Math.ceil(this.filterUsers.length / this.itemsPerPage);
    },
    paginatedUsers() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filterUsers.slice(startIndex, endIndex);
    },
    filterUsers() {
      return this.users.filter(p => {
        // Perform case-insensitive search
        return Object.keys(p).some(key => `${p[key]}`.toLowerCase().includes(this.inputValue));
      });
    },
    isActionButtonDisabled() {
      return this.checkedUsers.length === 0;
    },
  },
  methods: {
    async createUser(createdUser) {
      this.closeModal();
      let newUser = await createdUser.putDatabase();
      if (newUser) {
        this.users.push(newUser);
      }

      this.$refs.notificationComponent.createSuccessfulNotification('User successfully created'); // TODO implement properly, added for sprint review 3
    },
    async editUser(updated) {
      this.closeModal();
      let edited = this.selectedUser;
      if (edited) {
        let index = this.users.findIndex(p => p.id === edited.id);
        edited.injectAttributes(updated);
        let user = await edited.putDatabase();

        if (user) {
          this.users[index] = user;
        }

        this.$refs.notificationComponent.createSuccessfulNotification('User successfully updated'); // TODO implement properly, added for sprint review 3
      }
    },
    async deleteUser() {
      this.closeModal();
      const deletedUser = this.selectedUser;
      this.users = this.users.filter(user => user.id !== deletedUser.id);
      await deletedUser.delDatabase();

      this.$refs.notificationComponent.createSuccessfulNotification('User successfully deleted'); // TODO implement properly, added for sprint review 3
    },
    async deleteCheckedUsers() {
      this.closeModal();
      // Uncheck the selected users in the UsersRowComponent
      this.users.forEach(user => {
        user.isChecked = false;
      });
      const userIdsToDelete = this.checkedUsers.map(user => user.id);

      // Delete users one by one
      for (const userId of userIdsToDelete) {
        const deletedUserIndex = this.users.findIndex(user => user.id === userId);

        if (deletedUserIndex !== -1) {
          const deletedUser = this.users[deletedUserIndex];

          // Delete user from database
          await deletedUser.delDatabase();

          // Remove the user from the users array
          this.users.splice(deletedUserIndex, 1);
        }
      }
      this.checkedUsers = [];
    },
    handleInputValueChange(value) {
      this.inputValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
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
      const lastPage = Math.ceil(this.filterUsers.length / this.itemsPerPage);
      if (this.currentPage < lastPage) {
        this.currentPage++;
      }
    },
  },
  watch: {
    '$route'(to) {
      this.selectedUser = this.findSelectedRouteFromParam(to);
    }
  }
}
</script>

<template>

  <NotificationComponent ref="notificationComponent" />

  <TitleComponent class="header" page-title="Users"></TitleComponent>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton text-button="Action" ref="dropdownButton" :disabled="isActionButtonDisabled">
          <SolarDropdownMenuItem text-menu-item="Delete Users" @click="openDeleteMultipleUsersModal"/>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Users" @search="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="create-btn" button-text="Create User" @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable :columns="[' ', 'User', 'Function', 'Last Logged In', 'Action']">
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
  <CreateUserModal
      v-if="showCreateModal"
      :on-close="closeModal"
      @create-user="createUser"
  />
  <UpdateUserModal
      v-if="showUpdateModal"
      :on-close="closeModal"
      :user="selectedUser"
      @update-user="editUser"
  />
  <DeleteUserModal
      v-if="showDeleteModal"
      :on-close="closeModal"
      :user="selectedUser"
      @delete-user="deleteUser"
  />
  <DeleteMultipleUsersModal
      v-if="showDeleteMultipleModal"
      :on-close="closeModal"
      :users-to-delete="checkedUsers"
      @delete-users="deleteCheckedUsers"
  />
</template>

<style scoped>
.create-btn{
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
