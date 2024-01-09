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

      <!-- Assigned Team -->
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Assigned Team</label>
        <select v-model="project.team" class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
          <option v-for="team in teamList" :key="team.id" :value="team">{{ team.name }}</option>
        </select>
      </div>

      <!-- Select products -->
      <div v-if="project.team" class="col-span-6 sm:col-span-6">
        <label for="products" class="modal-label">Add Product</label>
        <div class="w-full flex">
          <select id="products" v-model="selectedProduct" class="product-select">
            <option v-for="warehouseProduct in warehouseProducts" :key="warehouseProduct.id"
                    :value="warehouseProduct">{{ warehouseProduct.product.name }}, In stock: {{
                warehouseProduct.amount
              }}
            </option>
          </select>
          <SolarButton class="ml-2 add-productInfo-btn" button-text="Add" @click="addProductProject"
                       :disabled="!selectedProduct"
          ></SolarButton>
        </div>
      </div>
    </div>

    <!-- Display ordered products -->
    <div class="order-list">
      <h2>Ordered Products:</h2>
      <SolarTable :columns="['Name', 'In Stock', 'Used Quantity', 'Action']">
        <tr class="table-row" v-for="(projectProduct) in projectProducts" :key="projectProduct.productId">
          <td class="px-6 py-4 font-semibold text-base">{{ projectProduct.productName }}</td>
          <td class="px-6 py-4">{{ projectProduct.stockAmount }}</td>
          <td class="px-6 py-4">
            <select v-model="projectProduct.selectedAmount" class="modal-input">
              <option v-for="option in projectProduct.amountOptions" :key="option" :value="option">{{ option }}</option>
            </select>
          </td>
<!--          <td class="px-6 py-4">-->
<!--            <div @click="removeProduct(index)" class="remove-order-btn">Remove product</div>-->
<!--          </td>-->
        </tr>
      </SolarTable>
    </div>

    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="closePopUp" class="cancel-button">Cancel</button>
      <button @click="createProject" type="submit" :disabled="isAnyFieldEmpty" class="ml-auto submit-button">Create
        Project
      </button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarButton from "@/components/general/SolarButton.vue";

export default {
  name: "CreateProject",
  inject: ['teamsService', 'warehouseService'],
  components: {
    SolarButton,
    SolarTable,
    SolarModal
  },
  data() {
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
      selectedProduct: null,
      warehouseProducts: [], // Products stored in warehouse
      projectProducts: [] // Products needed for the project
    }
  },
  async created() {
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
    addProductProject() {
      if (this.selectedProduct) {
        const product = this.selectedProduct.product;
        const stockAmount = this.selectedProduct.amount; // The stock amount of the product

        if (this.projectProducts.find(p => p.productId === product.id)) {
          alert('This product has already been added.');
          return;
        }

        const amountOptions = Array.from({length: stockAmount}, (_, i) => i + 1);

        this.projectProducts.unshift({
          productId: product.id,
          productName: product.name,
          stockAmount: stockAmount,
          amountOptions: amountOptions,
          selectedAmount: 1
        });

        // Remove the selected product from the warehouse product options
        const selectedIndex = this.warehouseProducts.findIndex((product) => product.id === this.selectedProduct.id);
        if (selectedIndex !== -1) this.warehouseProducts.splice(selectedIndex, 1);

        // Reset selectedProduct and selectedProductAmount after adding to orderedProducts
        this.selectedProduct = null;
      }
    },
    removeProduct(index) {
      // add the removed product back to productOptions
      this.warehouseProducts.push(this.projectProducts[index].product);
      this.projectProducts.splice(index, 1);
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
  background-color: #C7D02C;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.submit-button:hover {
  background-color: #a3b825;
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
