<template>
  <div class="box">
    <div class="content">
      <div class="content-header">
      </div>
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h3>Team information</h3>
            <label class="fw-semibold">Team Name<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="Enter the team name" v-model="editTeam.name" ref="name">
            <label class="fw-semibold">Users</label>
            <select v-model="userSelector" @change="e => addUserToTeam(e.target.value)">
              <option value="defaultOption" selected disabled>Click to add an user to the team</option>
<!--              Not all browsers support click events on Option Elements-->
              <option v-for="user of this.otherUsers" :key="user">{{ user.name }}</option>
            </select>
            <TableArrayComponent :items="userList" :attributes="{user: u => u.name}" :sortable="true" :clickable-rows="false" :removable="true" @row-remove="handleRowRemoveEvent" class="mt-3" />
          </form>
          <button :disabled="team.equals(editTeam)" @click="emitSave">Save</button>
          <button @click="emitCancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TableArrayComponent from "@/components/helpers/TableObjectComponent";
import User from "@/models/user";

export default {
  name: "TeamDetailComponent",
  components: {
    TableArrayComponent,
  },
  props: {
    team: Object,
  },
  data() {
    return {
      userList: [],
      otherUsers: [],
      editTeam: {},
      userSelector: null,
    };
  },
  created() {
    this.editTeam = this.team.clone();
    this.updateUsers();
  },
  methods: {
    handleRowRemoveEvent(item) {
      if (window.confirm(`Are you sure you want to remove this user from the team ("${item.user.name}")?`)) {
        this.editTeam.users = this.editTeam.users.filter(u => u !== item.user);
        console.log(this.editTeam.users);
        this.updateUsers();
      }
    },
    emitSave() {
      if (this.editTeam) {
        this.removeRequiredTags();
        if (!this.editTeam.name) {
          this.$refs.name?.classList.add("required");
          this.startShakeAnimation(this.$refs.name);
        }

        let requiredY = document.querySelector('.required')?.offsetTop;
        if (requiredY) {
          window.scrollTo({top: requiredY, behavior: "smooth"});
          return;
        }

        this.$emit("team-save", this.editTeam);
      }
    },
    emitCancel() {
      if (!this.team.equals(this.editTeam)) {
        if (!window.confirm('You have unsaved changes, are you sure you want to cancel?')) {
          return;
        }
      }
      this.$emit("team-cancel");
    },
    addUserToTeam(username) {
      let user = this.otherUsers.find(u => u.name === username);
      this.editTeam.users.push(user);
      this.updateUsers();
    },
    updateUsers() {
      if (User.fetching) {
        setTimeout(this.updateUsers, 100);
        return;
      }
      this.userSelector = "defaultOption";
      this.userList = [];
      for (let u of this.editTeam.users) {
        this.userList.push({user: u});
      }
      this.otherUsers = User.users.filter(user => !this.editTeam.users.includes(user));
    },
    removeRequiredTags() {
      let elements = document.getElementsByClassName("required");
      for (let element of elements) {
        element.classList.remove("required");
      }
    },
    startShakeAnimation(element) {
      if (!element) {
        return;
      }
      element.classList.add("shakeAnimation");
      setTimeout(() => element.classList.remove("shakeAnimation"), 1000);
    },
  },
};
</script>

<style scoped>
.required {
  border: 1px solid red;
}

.red {
  color: red;
}

.box{
  display: flex;
  justify-content: center;
  align-content: center;
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

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

/* This class is used! */
.shakeAnimation {
  animation: shakeAnimation 0.5s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
  transform: translate3d(0, 0, 0);
}

@keyframes shakeAnimation {
  10%, 90% {
    transform: translate3d(min(-1vw, -5px), 0, 0);
  }

  20%, 80% {
    transform: translate3d(min(1vw, 5px), 0, 0);
  }

  30%, 50%, 70% {
    transform: translate3d(min(-1vw, -5px), 0, 0);
  }

  40%, 60% {
    transform: translate3d(min(1vw, 5px), 0, 0);
  }
}

@media (max-width: 500px) {
  .content{
    width: 70%;
  }
}
</style>