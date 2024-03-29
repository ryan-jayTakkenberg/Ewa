<template>
  <SolarModal title="Update Project" @close-modal="closePopUp" class="modal">
    <div class="modal-grid">
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label ">Name</label>
        <input type="text" v-model="this.projectClone.projectName"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Client name</label>
        <input type="text" v-model="this.projectClone.clientName"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Install Date</label>
        <input type="date" v-model="this.projectClone.installDate"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Notes</label>
        <input type="text" v-model="this.projectClone.notes"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Assigned Team</label>
        <select v-model="projectClone.team" class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
          <option v-for="team in teamList" :key="team.id" :value="team">
            {{ team.name }}
          </option>
        </select>
      </div>

    </div>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="closePopUp" class="cancel-button">Cancel</button>
      <button @click="saveChanges" class="ml-auto submit-button" :disabled="!hasChanged">Save changes</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";

export default {
  name: "UpdateProject",
  inject: ['teamsService', 'warehouseService'],
  components: {
    SolarModal
  },
  props: {
    project: Object,
  },
  data (){
    return{
      projectClone: null,
      teamList: [],
      selectedTeam: null,
      warehouseProducts:[],
      projectProducts: []
    }
  },
  async created() {
    this.projectClone = this.project.clone();
    this.teamList = await this.teamsService.asyncFindAll();
    if (this.project.team) {
      this.projectClone.team = this.teamList.find(team => team.id === this.project.team.id);
    }
  },
  computed: {
    hasChanged() {
      if (!this.projectClone) {
        return false;
      }

      for (const key in this.project) {
        if (this.project[key] !== this.projectClone[key]) {
          return true;
        }
      }
      return false;
    },
  },
  methods: {
    saveChanges() {
      const json = {
        projectId: this.projectClone.projectId,
        projectName: this.projectClone.projectName,
        clientName: this.projectClone.clientName,
        installDate: this.projectClone.installDate,
        notes: this.projectClone.notes,
        teamId: this.projectClone.team.id
      }
      this.$emit("update-project", json);
      this.closePopUp();
    },
    closePopUp(){
      this.$emit("close-pop-up")
    }
  },

  watch: {
    'projectClone.team': {
      immediate: true,
      async handler(newVal) {
        if (newVal && newVal.warehouse) {
          try {
            this.selectedTeam = newVal.warehouse.id;
            const warehouse = await this.warehouseService.asyncFindById(this.selectedTeam);
            if (warehouse && warehouse.products) {
              this.warehouseProducts = warehouse.products;
            } else {
              this.warehouseProducts = [];
              console.log("No products found in the warehouse.");
            }
          } catch (error) {
            console.error("Error fetching warehouse:", error);
            this.warehouseProducts = [];
          }
        } else {
          console.log("No team or warehouse selected");
          this.warehouseProducts = [];
        }
      }
    }
  },
}
</script>

<style scoped>
.submit-button:disabled, .submit-button:disabled:hover {
  opacity: 0.4;
  cursor: not-allowed; /* Change the cursor to not-allowed */
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

.modal-label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  color: rgb(17 24 39);
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem;
}

.cancel-button {
  color: rgb(17 24 39);
  background-color: #bfbfbf;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.cancel-button:hover {
  background-color: rgb(206 212 218);
}
</style>
