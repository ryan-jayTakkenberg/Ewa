<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";

import Team from "@/models/team";
import TeamsRowComponent from "@/components/team/TeamsRowComponent.vue";
import TeamsEditComponent from "@/components/team/TeamsEditComponent.vue";
import TeamsAddComponent from "@/components/team/TeamsAddComponent.vue";

import TeamsDeleteComponent from "@/components/team/TeamsDeleteComponent.vue";

import DeleteMultipleTeams from "@/components/team/DeleteMultipleTeams.vue";

import { TeamsAdaptor } from "@/service/teams-adaptor";
export default {
  name: "TeamsDetailComponent",
  inject: ["teamsAdaptor"],

  components: {
    DeleteMultipleTeams,
    TeamsDeleteComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    TeamsRowComponent,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    ButtonComponent,
    TeamsEditComponent,
    TeamsAddComponent,
  }, provide: {
    teamsAdaptor: new TeamsAdaptor("http://localhost:8085/api/teams"),
  },
  data() {

    return {
      inputValue: '', // Store the input value for searching users
      teams: [...Team.teams],
      selectedTeam: null,  // Track the selected user for editing
      isEditTeamModalOpen: false,
      isAddTeamsOpen: false,
      checkedTeams: [],
      isDeleteTeamModalOpen: false,
      teamsAdaptor: new TeamsAdaptor("http://localhost:8085/api/teams"),
    };
  },
  async created() {
    try {
      this.teams = await this.teamsAdaptor.asyncFindAll()
      console.log(this.teams)
    } catch (error) {
      console.error("Error occurred while getting the data from the backend", error)
    }

  }, computed: {
    filteredTeams() {
      return this.teams.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.inputValue)) {
            return true;
          }
        }
        return false;
      });
    },
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value.trim().toLowerCase();
      // Use this.filterValue to search in the table
    },

    openEditUserModal(team) {
      if (!team) {
        // No user selected
        return;
      }
      this.selectedTeam = team;  // Set the selected user
      this.isEditTeamModalOpen = true;  // Open the modal

    },
    openAddTeam() {
      this.isAddTeamsOpen = true;
    },
    closeEditUserModal() {
      this.isEditTeamModalOpen = false;
    },
    closeDeleteTeamModal() {
      this.isDeleteTeamModalOpen = false;
    },
    openDeleteUserModal(team) {
      if (!team) {
        // No user selected
        return;
      }
      this.selectedTeam = team;  // Set the selected user// Open the modal

      this.isDeleteTeamModalOpen = true;
    },
    async editTeam(editedTeam) {
      const index = this.teams.findIndex(team => team.id === editedTeam.id);
      if (index !== -1) {
        // Update the team data in the array
        this.teams[index] = editedTeam;
      }
      this.closeEditUserModal();
    },
    deleteCheckedTeams() {
      // Get the IDs of the users to delete
      const teamsIdsToDelete = this.checkedTeams.map(team => team.id);

      // Uncheck the selected users in the UsersRowComponent
      this.teams.forEach(team => {
        team.isChecked = false;
      });

      // Remove the selected users from the users array based on their IDs
      this.teams = this.teams.filter(user => !teamsIdsToDelete.includes(user.id));

      // Clear the checkedUsers array
      this.checkedTeams = [];

      this.closeModal();
    }, methods: {

    },


    closeAddTeamsModal() {
        this.isAddTeamsOpen = false;
      },
      toggleCheckbox(team, isChecked) {
        if (isChecked) {
          this.checkedTeams.push(team.id);
        } else {
          this.checkedTeams = this.checkedTeams.filter(id => id !== team.id);
        }
        console.log(this.checkedTeams);
      }, getSelectedteams() {
        return this.projects.filter(team => this.checkedTeams.includes(team.id));
      },
      async asyncDeleteTeamById(teamId) {
        try {
          await this.teamsAdaptor.asyncDeleteById(teamId);
          this.teams = this.teams.filter((team) => team.id !== teamId);
          this.isDeleteTeamModalOpen = false;
        } catch (error) {
          console.error("Error deleting team:", error);
        }
      },
      async asyncUpdateTeamById(teamId) {
        console.log("asyncUpdateTeamById - Team ID:", teamId);

        try {
          if (teamId) {
            await this.teamsAdaptor.asyncUpdateTeam(teamId);
            await this.getTeams();
          } else {
            console.error("Team ID is undefined or not valid");
          }
        } catch (error) {
          console.error("Error updating team:", error);
        }
      },

      async asyncAddTeam(newTeam) {
        try {
          await this.teamsAdaptor.asyncSaveTeam(newTeam);
          await this.getTeams();
          console.log(this.teams);
          this.isAddTeamsOpen = false; // Close the modal

        } catch (error) {
          console.error("Error adding team:", error);
        }
      }, async getTeams() {
        try {
          this.teams = await this.teamsAdaptor.asyncFindAll()
        } catch (error) {
          console.error("Error occurred while getting the data from the backend", error)
        }
      },
    }
  }


</script>

<template>
  <div class="users-header">
    <TitleComponent page-title="Team"></TitleComponent>
  </div>

  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">
        <!-- Action Dropdown Button -->
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem text-menu-item="Edit Team" @click="openEditUserModal(getSelectedteams()[0])"></SolarDropdownMenuItem>
          <SolarDropdownMenuItem text-menu-item="Delete Team" @click="openDeleteUserModal(getSelectedteams()[0])"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Teams" class="ml-auto" @search="handleInputValueChange"
        ></SearchBarComponent>
        <ButtonComponent button-text="Add Team" @click="openAddTeam"></ButtonComponent>
      </div>

      <SolarTable :columns="['Team', 'warehouse', 'project', 'Action']">
        <TeamsRowComponent
            v-for="(team) in teams"
            :key="team.id"
            :teams="team"
            :isChecked="team.isChecked"
            @click-delete-team="openDeleteUserModal"
            @click-edit-team="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(team, $event)" ><!-- Pass user and checkbox state -->
        </TeamsRowComponent>
      </SolarTable>
    </div>
  </div>
  <TeamsAddComponent v-if="isAddTeamsOpen" :on-close="closeAddTeamsModal" @addUser="asyncAddTeam"></TeamsAddComponent>
  <TeamsEditComponent
      v-if="isEditTeamModalOpen"
      :on-close="closeEditUserModal"
      :team="selectedTeam"
      @editTeam="asyncUpdateTeamById"
  ></TeamsEditComponent>

  <TeamsDeleteComponent
      v-if="isDeleteTeamModalOpen "
      :team="selectedTeam"
      :on-close="closeDeleteTeamModal"
      @delete-team="asyncDeleteTeamById"
  >
  </TeamsDeleteComponent>
  <DeleteMultipleTeams
      v-if="$route.path.includes('delete-teams')"
      :users-to-delete="checkedTeams" :on-close="closeDeleteTeamModal"
      @delete-teams="deleteCheckedTeams">
  </DeleteMultipleTeams>
</template>

<style scoped>
.users-header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.users-action-row {
  display: flex;
  margin-bottom: 1rem /* 16px */;
}

.users-body {
  position: relative;
  overflow-x: auto;
}

.users-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem /* 16px */;
  background-color: white;
}
</style>
