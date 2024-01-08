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
    <!-- Loop over warehouse products to show Add buttons -->
    <div v-for="productWrapper in warehouseProducts" :key="productWrapper.product.id" class="product-item">
      {{ productWrapper.product.name }}, Stock: {{ productWrapper.amount }}
      <button @click="addProduct(productWrapper)">Add</button>
    </div>


    <!-- Loop over added products to show amount dropdowns -->
    <div v-for="addedProduct in projectProducts" :key="addedProduct.productId" class="selected-product-item">
      <label>{{ addedProduct.productName }}</label>
      <select v-model="addedProduct.selectedAmount" class="modal-input">
        <option v-for="amount in addedProduct.amountOptions" :key="amount" :value="amount">
          {{ amount }}
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

export default {
  name: "CreateProject",
  inject: ['teamsService','warehouseService'],
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
      selectedTeam: null,
      warehouseProducts:[],
      projectProducts: []
    }
  },
  async created(){
    this.teamList = await this.teamsService.asyncFindAll();
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
    async createProject() {
      const productsUsed = this.projectProducts.map(p => ({
        productId: p.productId,
        amountUsed: p.selectedAmount
      }));
      const json = {
        projectId: this.project.projectId,
        projectName: this.project.projectName,
        clientName: this.project.clientName,
        installDate: this.project.installDate,
        notes: this.project.notes,
        teamId: this.project.team.id,
        products: this.projectProducts
      }
      console.log(this.projectProducts)
      this.$emit("create-project", json);
      await new Promise(resolve => setTimeout(resolve, 500));

      try {
        await this.warehouseService.asyncUpdateProductQuantities(this.project.team.warehouse.id, productsUsed);
        console.log("Warehouse product quantities updated successfully");
      } catch (error) {
        console.error("Error updating warehouse product quantities:", error);
      }

      this.closePopUp();
    },
    closePopUp() {
      this.$emit("close-pop-up")
    },
    addProduct(productWrapper) {
      const product = productWrapper.product;
      const stockAmount = productWrapper.amount; // The stock amount of the product

      if (this.projectProducts.find(p => p.productId === product.id)) {
        alert('This product has already been added.');
        return;
      }

      const amountOptions = Array.from({ length: stockAmount }, (_, i) => i + 1);

      this.projectProducts.push({
        productId: product.id,
        productName: product.name,
        amountOptions: amountOptions,
        selectedAmount: 1
      });
    },
  },

    watch: {
    'project.team': {
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