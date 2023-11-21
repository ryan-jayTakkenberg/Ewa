<script>
import Team from "@/models/team";
import SolarTitle from "@/components/general/SolarTitle.vue";

import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import TeamsRowComponent from "@/components/team/TeamsRowComponent.vue";
import TeamsEditComponent from "@/components/team/TeamsEditComponent.vue";
import TeamsAddComponent from "@/components/team/TeamsAddComponent.vue";
import TeamsDeleteComponent from "@/components/team/TeamsDeleteComponent.vue";


import {TeamsAdaptor} from "@/service/teams-adaptor";
export default {
  name: "TeamsOverview",
  inject: ["teamsAdaptor"],
  components: {
    SolarButton,
    TeamsDeleteComponent,
    TeamsRowComponent,
    SolarTable,
    SolarTitle,
    SolarSearchbar,
    TeamsEditComponent,
    TeamsAddComponent,
  },
  provide: {
    teamsAdaptor: new TeamsAdaptor("http://localhost:8085/api/teams"),
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching teams
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
  },
  computed: {
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
    },
    methods: {},

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
    },
    getSelectedTeams() {
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
    },
    async getTeams() {
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
  <SolarTitle class="header" page-title="Teams"/>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarSearchbar place-holder="Search For Teams" @search="handleInputValueChange"/>
        <SolarButton class="ml-auto" button-text="Create Team" @click="openAddTeam"></SolarButton>
      </div>
      <SolarTable :columns="['Team', 'warehouse', 'project', 'Action']">
        <TeamsRowComponent
            v-for="(team) in filteredTeams" :key="team.id" :teams="team"
            :isChecked="team.isChecked"
            @click-delete-team="openDeleteUserModal"
            @click-edit-team="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(team, $event)"><!-- Pass user and checkbox state -->
        </TeamsRowComponent>
      </SolarTable>
    </div>
  </div>

  <TeamsAddComponent v-if="isAddTeamsOpen" :on-close="closeAddTeamsModal" @addUser="asyncAddTeam"></TeamsAddComponent>
  <TeamsEditComponent
      v-if="isEditTeamModalOpen"
      :on-close="closeEditUserModal"
      :team="selectedTeam"
      @editTeam="asyncUpdateTeamById">
  </TeamsEditComponent>
  <TeamsDeleteComponent
      v-if="isDeleteTeamModalOpen "
      :team="selectedTeam"
      :on-close="closeDeleteTeamModal"
      @delete-team="asyncDeleteTeamById">
  </TeamsDeleteComponent>

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
