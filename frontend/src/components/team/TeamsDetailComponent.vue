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


export default {
  name: "UsersOverview",
  components: {
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
    };
  },
  created() {
    if (!this.projects?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!Team.fetching) {
          this.teams = [...Team.teams];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
  },computed: {
    filteredTeams() {
      return this.teams.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.inputValue)) {
            return true;
          }
        }
        return false;
      });},
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
    addTeam(newTeam){
      this.teams.push(newTeam)
    },
    openAddTeam(){
      this.isAddTeamsOpen = true;
    },
    closeEditUserModal() {
      this.isEditTeamModalOpen = false;
    },
    closeDeleteTeamModal(){
      this.isDeleteTeamModalOpen = false;
    },
    openDeleteUserModal(team){
      if (!team) {
        // No user selected
        return;
      }
      this.selectedTeam = team;  // Set the selected user// Open the modal

      this.isDeleteTeamModalOpen = true;
    },
    editTeam(editedTeam) {
      const index = this.teams.findIndex(team => team.id === editedTeam.id);
      if (index !== -1) {
        // Update the team data in the array
        this.teams[index] = editedTeam;
      }
      this.closeEditUserModal();

    },

    closeAddTeamsModal(){
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
    deleteTeam(teamID) {
      this.teams = this.teams.filter(team => team.id !== teamID)
      this.closeDeleteTeamModal()
    },
  },
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
            v-for="(team, index) in filteredTeams"
            :key="index"
            :teams="team"
            :isChecked="team.isChecked"
            @click-delete-team="openDeleteUserModal"
            @click-edit-team="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(team, $event)" ><!-- Pass user and checkbox state -->
        </TeamsRowComponent>
      </SolarTable>
    </div>
  </div>
  <TeamsAddComponent v-if="isAddTeamsOpen" :on-close="closeAddTeamsModal" @addUser="addTeam"></TeamsAddComponent>
  <TeamsEditComponent v-if="isEditTeamModalOpen" :on-close="closeEditUserModal" :team="selectedTeam" @edit-team="editTeam">

  </TeamsEditComponent>
  <TeamsDeleteComponent
      v-if="isDeleteTeamModalOpen "
      :team="selectedTeam"
      :on-close="closeDeleteTeamModal"
      @delete-team="deleteTeam"
  >
  </TeamsDeleteComponent>
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
