<template>
  <div class="text-center">
    <p>Edit a team by clicking on a row:</p>
    <TableArrayComponent :items="teams" :attributes="teamObject" :sortable="true" :removable="true" :clickable-rows="!selectedTeam" @row-click="handleRowClickEvent" @row-remove="handleRowRemoveEvent" />
    <button @click="addTeam" :disabled="selectedTeam">Create a new team</button>
  </div>
  <div class="line rounded mt-3 mb-3"></div>
  <TeamEditComponent :team="selectedTeam" @team-cancel="handleTeamCancelEvent" @team-save="handleTeamSaveEvent" v-if="selectedTeam" ref="TeamEditComponent" />
</template>

<script>// TODO popups
import Team from "@/models/team";
import TableArrayComponent from "@/components/helpers/TableObjectComponent";
import TeamEditComponent from "@/components/manage/TeamEditComponent";

export default {
  name: 'TeamDetailComponent',
  components: {
    TableArrayComponent,
    TeamEditComponent,
  },
  data() {
    return {
      selectedTeam: null,
      teams: Team.teams,
      teamObject: {
        name: s => s,
        users: a => a?.length,
      }
    };
  },
  created() {
    if (!this.teams?.length) {
      const teamFetchingInterval = setInterval(() => {
        if (!Team.fetching) {
          this.teams = Team.teams;
          clearInterval(teamFetchingInterval);
        }
      }, 100);
    }
  },
  methods: {
    handleRowClickEvent(team) {
      if (!Object.is(team, this.selectedTeam)) {
        this.selectedTeam = team;
        setTimeout(this.scrollToTeam, 1);
      } else {
        this.closeTeamEditPage();
      }
    },
    async handleRowRemoveEvent(team) {
      if (this.selectedTeam === team) {
        window.alert("You cannot remove the team you are currently editing");
        return;
      }

      if (window.confirm(`Are you sure you want to remove this team ("${team.name}")?`)) {
        // show load popup
        if (!await team.delDatabase()) {
          // hide load popup
          // show popup with error message
          return;
        }
        // hide load popup
        this.syncTeams();
      }
    },
    handleTeamCancelEvent() {
      this.closeTeamEditPage();
    },
    async handleTeamSaveEvent(team) {
      let index = this.teams.indexOf(this.selectedTeam);
      if (index < 0) {
        this.teams.push(team);
      } else {
        this.teams[index] = team;
      }
      // show load popup
      if (!await team.putDatabase()) {
        // hide load popup
        // show popup with error message
        return;
      }
      // hide load popup
      this.syncTeams();
      this.closeTeamEditPage();
    },
    addTeam() {
      this.selectedTeam = Team.template;
      setTimeout(this.scrollToTeam, 1);
    },
    scrollToTeam() {
      let y = this.$refs.TeamEditComponent?.$el.offsetTop;
      if (y > window.screen.height / 2) {
        window.scrollTo({top: y, behavior: "smooth"});
      }
    },
    closeTeamEditPage() {
      this.selectedTeam = null;
    },
    syncTeams() {
      this.teams = Team.teams;
    },
  }
};
</script>

<style scoped>
.line {
  background-color: lightgray;
  height: 0.5vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}
</style>