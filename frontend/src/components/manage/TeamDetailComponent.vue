<template>
  <div class="text-center">
    <p>Edit a team by clicking on a row:</p>
    <TableArrayComponent :items="teams" :attributes="teamObject" :sortable="true" :removable="true" :clickable-rows="!selectedTeam" @row-click="handleRowClickEvent" @row-remove="handleRowRemoveEvent" />
    <button @click="addTeam" :disabled="selectedTeam">Create a new team</button>
  </div>
  <div class="line rounded mt-3 mb-3"></div>
  <TeamEditComponent :team="selectedTeam" @team-cancel="handleTeamCancelEvent" @team-save="handleTeamSaveEvent" v-if="selectedTeam" ref="TeamEditComponent" />
</template>

<script>
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
  methods: {
    handleRowClickEvent(team) {
      if (!Object.is(team, this.selectedTeam)) {
        this.selectedTeam = team;
        setTimeout(this.scrollToTeam, 1);
      } else {
        this.closeTeamEditPage();
      }
    },
    handleRowRemoveEvent(team) {
      if (this.selectedTeam === team) {
        window.alert("You cannot remove the team you are currently editing");
        return;
      }

      if (window.confirm(`Are you sure you want to remove this team ("${team.name}")?`)) {
        this.teams = this.teams.filter(t => t !== team);
        this.syncTeams();
      }
    },
    handleTeamCancelEvent() {
      this.closeTeamEditPage();
    },
    handleTeamSaveEvent(team) {
      let index = this.teams.indexOf(this.selectedTeam);
      if (index < 0) {
        this.teams.push(team);
      } else {
        this.teams[index] = team;
      }
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
      Team.teams = this.teams;
    }
  }
};
</script>

<style scoped>
.red {
  color: red;
}

.box{
  display: flex;
  justify-content: center;
  align-content: center;
}

.line {
  background-color: lightgray;
  height: 0.5vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

.content{
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 15px 0 30px 0;
}

.content-header{
  height: 175px;
  background: url("../../../static/images/manageDetailHeader.jpg") center no-repeat;
  background-size: cover;
  border-radius: 20px 20px 0 0 ;
}

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

label{
  width: 100%;
  margin: 10px 0 5px 0;
}

input{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid gray;
  width: 100%;
}

.number-input::-webkit-inner-spin-button,
.number-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

textarea{
  width: 100%;
  height: 125px;
  overflow: hidden;
  resize: none;
  padding: 5px;
  border-radius: 10px;
}

select{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid gray;
  width: 100%;
}

.h3-middle{
  margin-top: 25px;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

@media (max-width: 500px) {
  .content{
    width: 70%;
  }
}
</style>