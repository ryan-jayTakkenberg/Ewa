<template>
  <SolarModal title="Update Warehouse" @close-modal="closePopUp" class="modal">
    <div class="modal-grid">
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label ">Name</label>
        <input type="text" v-model="this.warehouseClone.name"
            class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">City</label>
        <input type="text" v-model="this.warehouseClone.address"
            class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Address</label>
        <input type="text" v-model="this.warehouseClone.city"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Postal Code</label>
        <input type="text" v-model="this.warehouseClone.postalCode"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
      </div>
        <div class="col-span-6 sm:col-span-3">
          <label class="modal-label">Max Storage</label>
          <input type="text" v-model="this.warehouseClone.maxStorage"
                 class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
        </div>
        <div class="col-span-6 sm:col-span-3">
          <label class="modal-label">Min Storage</label>
          <input type="text" v-model="this.warehouseClone.minStorage"
                 class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
        </div>
      <div class="col-span-6 sm:col-span-3">
        <label class="modal-label">Current Storage</label>
        <input type="text" v-model="this.warehouseClone.currentStorage"
               class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
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
  name: "UpdateWarehouse",
  components: {
    SolarModal
  },
  props: {
    warehouse: Object,
  },
  data (){
    return{
      warehouseClone: null,
    }
  },
  created() {
    if (this.warehouse) {
      // Using object spread to create a shallow clone
      this.warehouseClone = { ...this.warehouse };
    } else {
      console.error('Warehouse prop is not set');
    }
  },
  computed: {
    hasChanged() {
      if (!this.warehouseClone) {
        return false;
      }

      for (const key in this.warehouse) {
        if (this.warehouse[key] !== this.warehouseClone[key]) {
          return true;
        }
      }
      return false;
    },
  },
  methods: {
    saveChanges(){
      const json = {
        id: this.warehouseClone.id,
        name: this.warehouseClone.name,
        address: this.warehouseClone.address,
        city: this.warehouseClone.city,
        postalCode: this.warehouseClone.postalCode,
        maxStorage: parseInt(this.warehouseClone.maxStorage, 10),
        minStorage: parseInt(this.warehouseClone.minStorage, 10),
        currentStorage: parseInt(this.warehouseClone.currentStorage, 10)
      };

      console.log(json)
      this.$emit("update-warehouse", json)
      this.closePopUp()
    },
    closePopUp(){
      this.$emit("close-pop-up")
    }
  }
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