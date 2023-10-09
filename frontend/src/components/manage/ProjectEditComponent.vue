<template>
  <div class="box">
    <div class="content">
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h3>Project information</h3>
            <label class="fw-semibold">Name Project<span class="red fw-normal">*</span></label>
            <input type="text" placeholder=". . ." v-model="selectedProjectName">
            <label class="fw-semibold">Name Client<span class="red fw-normal">*</span></label>
            <input type="text" placeholder=". . ." v-model="selectedClientName">
            <label class="fw-semibold">Time of installation<span class="red fw-normal">*</span></label>
            <input type="date" v-model="selectedInstallDate">
            <label class="fw-semibold">Install team<span class="red fw-normal">*</span></label>
            <select >
              <option selected disabled>Select a team</option>
              <option>Team Solar Sedum</option>
              <option>Team 1</option>
            </select>
            <label class="fw-semibold">Warehouse used for storage<span class="red fw-normal">*</span></label>
            <select>
              <option selected disabled>Select a warehouse</option>
              <option>Solar Sedum warehouse</option>
              <option>Team 1 warehouse</option>
            </select>
            <label class="fw-semibold">Note</label>
            <textarea placeholder="Notes . . ." v-model="selectedNotes"></textarea>
          </form>
          <button @click="handleSave()">{{isNewProject ? 'Add project' : 'Save'}}</button>
          <button @click="cancel()">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectEditComponent",
  props: {
    projectName: String,
    clientName: String,
    installDate: String,
    installTeam: String,
    usedWarehouse: String,
    notes: String,
  },
  data() {
    return{
      selectedProjectName: this.projectName,
      selectedClientName: this.clientName,
      selectedInstallDate: this.installDate,
      selectedInstallTeam: this.installTeam,
      selectedUsedWarehouse: this.usedWarehouse,
      selectedNotes: this.notes,
      isNewProject: !this.projectName
    }
  },
  methods: {
    cancel() {
      this.$emit('cancel')
    },
    handleSave() {
      if (this.isNewProject) {
        this.$emit('add-project', {
          projectName: this.selectedProjectName,
          clientName: this.selectedClientName,
          installDate: this.selectedInstallDate,
          installTeam: this.selectedInstallTeam,
          usedWarehouse: this.selectedUsedWarehouse,
          notes: this.selectedNotes,
        });
      } else {
        this.$emit('save-project', {
          projectName: this.selectedProjectName,
          clientName: this.selectedClientName,
          installDate: this.selectedInstallDate,
          installTeam: this.selectedInstallTeam,
          usedWarehouse: this.selectedUsedWarehouse,
          notes: this.selectedNotes,
        });
      }
    }
  },
  watch: {
    projectName(newProjectName) {
      this.selectedProjectName = newProjectName;
    },
    clientName(newClientName) {
      this.selectedClientName = newClientName;
    },
    installDate(newInstallDate) {
      this.selectedInstallDate = newInstallDate;
    },
    installTeam(newInstallTeam) {
      this.selectedInstallTeam = newInstallTeam;
    },
    usedWarehouse(newUsedWarehouse) {
      this.selectedUsedWarehouse = newUsedWarehouse;
    },
    notes(newNotes) {
      this.selectedNotes = newNotes;
    }
  }
}
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

.content{
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 15px 0 30px 0;
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
  border: 1px solid grey;
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