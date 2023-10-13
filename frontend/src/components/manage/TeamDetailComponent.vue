<template>
  <div class="box">
    <div class="content">
      <div class="content-header">
        <!-- text in background -->
      </div>
      <div class="content-box text-center">
        <p>Edit a team by clicking on a row:</p>
        <TableArrayComponent ref="tableRef" :items="teams" :attributes="teamObject" :sortable="true" :removable="true" :clickable-rows="true" @row-click="handleRowClickEvent" @row-remove="handleRowRemoveEvent" />
        <button @click="addTeam">Create a new team</button>
      </div>
    </div>
  </div>
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
      teams: [...Team.teams],
      teamObject: {
        name: s => s,
        users: a => a?.length,
      },
    };
  },
  created() {
    if (!this.teams?.length) {
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
    handleRowClickEvent(team) {
      if (!Object.is(team, this.selectedTeam)) {
        this.selectedTeam = team;
        setTimeout(this.scrollToTeam, 2);
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
        this.teams = this.teams.filter(t => t !== team);
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
      this.closeTeamEditPage();
    },
    addTeam() {
      this.selectedTeam = Team.createNewTeam();
      this.$refs.tableRef.current = null;
      setTimeout(this.scrollToTeam, 1);
    },
    scrollToTeam() {
      let y = this.$refs.TeamEditComponent?.$el.offsetTop;
      if (y > window.screen.height / 2) {
        window.scrollTo({top: y, behavior: "smooth"});
      }
    },
    closeTeamEditPage() {
      this.$refs.tableRef.current = null;
      this.selectedTeam = null;
    },
  }
};
</script>

<style scoped>

button {
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

.box {
  display: flex;
  justify-content: center;
  align-content: center;
}

.content {
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 15px 0 30px 0;
}

.content-header {
  height: 175px;
  background: url("../../../static/images/manageDetailHeader3.jpg") center no-repeat;
  background-size: cover;
  border-radius: 20px 20px 0 0;
}

.content-box {
  margin: 30px 0 30px 0;
}
</style>

<style>
tr.activeTableRow {
  background: #c7d02c !important;
}

tr.hoverTableRow:hover {
  color: #818181;
}
</style>