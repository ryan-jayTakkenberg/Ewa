<template>
  <SolarModal title="Create Warehouse" @close-modal="closePopUp" class="modal">
    <div class="modal-grid">
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Name</label>
        <input type="text" v-model="warehouse.name" class="modal-input shadow-sm" placeholder="Name">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">City</label>
        <input type="text" v-model="warehouse.city" class="modal-input shadow-sm" placeholder="City">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Address</label>
        <input v-model="warehouse.address" type="text" placeholder="Address" class="modal-input shadow-sm">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Postal Code</label>
        <input v-model="warehouse.postalCode" type="text" placeholder="Postal Code" class="modal-input shadow-sm">
      </div>

      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Max Storage</label>
        <input type="text" v-model="warehouse.maxStorage"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Min Storage</label>
        <input type="text" v-model="warehouse.minStorage"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>

    </div>
    <template v-slot:footer>
      <button @click="closePopUp" class="cancel-button">Cancel</button>
      <button @click="createWarehouse" type="submit" :disabled="isAnyFieldEmpty" class="ml-auto submit-button">Create Warehouse</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";
import Warehouse from "@/models/warehouse";

export default {
  name: "CreateWarehouse",
  components: {
    SolarModal
  },
  data(){
    return {
      warehouse: {
        id: 0,
        name: '',
        city: '',
        address: '',
        postalCode: '',
        maxStorage: 0,
        minStorage: 0
      }
    }
  },
  computed: {
    isAnyFieldEmpty() {
      return (
          this.warehouse.name.trim() === '' ||
          this.warehouse.city.trim() === '' ||
          this.warehouse.address.trim() === '' ||
          this.warehouse.postalCode.trim() === '' ||
          this.warehouse.minStorage < 0 ||
          this.warehouse.maxStorage <= this.warehouse.minStorage
      );
    },
  },
  methods: {
    createWarehouse(){
      const newWarehouse = new Warehouse(
          0,
          this.warehouse.name,
          this.warehouse.address,
          this.warehouse.postalCode,
          this.warehouse.city,
          undefined,
          undefined,
          Number(this.warehouse.maxStorage),
          Number(this.warehouse.minStorage),
          0,
      );
      this.$emit("create-warehouse", newWarehouse);
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
  background-color: #c5ce2c;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
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