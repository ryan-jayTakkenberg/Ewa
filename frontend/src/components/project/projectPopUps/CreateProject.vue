<template>
  <SolarModal title="Create Project" @close-modal="closePopUp" class="modal">
    <div class="modal-grid">
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Project name</label>
        <input type="text" v-model="project.projectName" class="modal-input shadow-sm" placeholder="...">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Client name</label>
        <input type="text" v-model="project.clientName" class="modal-input shadow-sm" placeholder="...">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Install Date</label>
        <input v-model="project.installDate" type="date" class="modal-input shadow-sm">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Notes</label>
        <input v-model="project.notes" type="text" placeholder="...." class="modal-input shadow-sm">
      </div>
    </div>
    <div class="col-span-6 sm:col-span-3">
      <label class="modal-label">Assigned Team</label>
      <select v-model="project.team" class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
        <option v-for="team in teamList" :key="team.id" :value="team">
          {{ team.name }}
        </option>
      </select>
    </div>
    <template v-slot:footer>
      <button @click="closePopUp" class="cancel-button">Cancel</button>
      <button @click="createProject" type="submit" :disabled="isAnyFieldEmpty" class="ml-auto submit-button">Create Project</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";
import Project from "@/models/project";

export default {
  name: "CreateProject",
  inject: ['teams'],
  components: {
    SolarModal
  },
  data(){
    return {
      project: {
        projectId: 0,
        projectName: '',
        clientName: '',
        installDate: '',
        notes: '',
      },
      teamList: [],
    }
  },
  async created(){
    this.teamList = await this.teams.asyncFindAll();
  },
  computed: {
    isAnyFieldEmpty() {
      return (
          this.project.projectName.trim() === '' ||
          this.project.clientName.trim() === '' ||
          this.project.installDate.trim() === '' ||
          this.project.notes.trim() === '' ||
          !this.project.team
      );
    },
  },
  methods: {
    createProject(){
      const newProject = new Project(
          this.project.projectId,
          this.project.projectName,
          this.project.clientName,
          this.project.installDate,
          this.project.notes,
          this.project.team
      );
      this.$emit("create-project", newProject);
      this.closePopUp();
    },
    closePopUp(){
      this.$emit("close-pop-up")
    }
  }
}
</script>

<style scoped>
.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem;
}

.modal-label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  color: rgb(17 24 39);
}

.modal-input {
  background-color: rgb(249 250 251);
  border-width: 1px;
  border-color: rgb(209 213 219);
  color: rgb(17 24 39);
  font-size: 0.875rem;
  line-height: 1.25rem;
  border-radius: 0.5rem;
  display: block;
  width: 100%;
  padding: 0.625rem;
}

.submit-button {
  color: white;
  background-color: rgb(29 78 216);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.submit-button:hover {
  background-color: rgb(30 64 175);
}

.submit-button:disabled, .submit-button:disabled:hover {
  opacity: 40%;
  cursor: not-allowed;
}

.cancel-button {
  color: rgb(17 24 39);
  background-color: rgb(229 231 235);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}
</style>