<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";

import Team from "@/models/team";
import TeamsRowComponent from "@/Teams/TeamsRowComponent.vue";
import TeamsEditComponent from "@/Teams/TeamsEditComponent.vue";
import TeamsAddComponent from "@/Teams/TeamsAddComponent.vue";


export default {
  name: "UsersOverview",
  components: {
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
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.inputValue to search in the table
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
    }
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
          <SolarDropdownMenuItem text-menu-item="Delete Team" @click="openDeleteUserModal"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Teams" class="ml-auto" @input="handleInputValueChange"
        ></SearchBarComponent>
        <ButtonComponent button-text="Add Team" @click="openAddTeam"></ButtonComponent>
      </div>

      <SolarTable :columns="['Team', 'warehouse', 'project', 'Action']">
        <TeamsRowComponent
            v-for="(team, index) in teams"
            :key="index"
            :teams="team"
            :isChecked="team.isChecked"
            @click-edit-user="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(team, $event)" ><!-- Pass user and checkbox state -->
        </TeamsRowComponent>
      </SolarTable>
    </div>
  </div>
  <TeamsAddComponent v-if="isAddTeamsOpen" :on-close="closeAddTeamsModal" @addUser="addTeam"></TeamsAddComponent>
  <TeamsEditComponent v-if="isEditTeamModalOpen" :on-close="closeEditUserModal" :team="selectedTeam" >

  </TeamsEditComponent>
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
