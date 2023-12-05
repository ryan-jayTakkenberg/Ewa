<script>
import Order from "@/models/order";
import SolarModal from "@/components/general/SolarModal.vue";
import Team from "@/models/team";
import Product from "@/models/product";
import SolarButton from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";

export default {
  name: "CreateOrderModal",
  emits: ["create-order"],
  components: {SolarTable, SolarButton, SolarModal},
  data() {
    return {
      OrderStatusOptions: Order.Status,
      teamOptions: Team.teams,
      productOptions: Product.products,
      selectedProducts: [],
      selectedProduct: null,
      order: {
        orderNumber: '',
        orderDate: '',
        estimatedDeliveryDate: '',
        team: '',
        products: [],
        status: '',
      },
    }
  },
  props: {
    onClose: {
      type: Function,
      required: true,
    },
  },
  computed: {
    isAnyFieldEmpty() {
      return (
          !this.order.team ||
          !this.order.product ||
          !this.order.status
      );
    },
  },
  methods: {
    createOrder() {
      let orderClass = new Order();
      orderClass.injectAttributes(this.order);
      this.$emit('create-order', orderClass);
    },
    addProductsToOrder() {
      // Add selected products to the order.products array
      this.selectedProducts.push(this.selectedProduct)
    },
    removeProduct() {
    }
  }
}
</script>

<template>
  <form @submit.prevent="createOrder" @keydown.enter.prevent="">
    <SolarModal title="Create Order" @close-modal="onClose" class="modal">
      <div class="modal-grid">
        <!-- Ordered from -->
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label">Ordered From</label>
          <input
              type="text"
              v-model="order.orderedFrom"
              class="modal-input shadow-sm"
              placeholder="Ordered From"
              required>
        </div>

        <!-- Order Status-->
        <div class="col-span-6 sm:col-span-3">
          <label for="order-status" class="modal-label">Order status</label>
          <select v-model="order.status" class="status-select" required>
            <option v-for="status in OrderStatusOptions" :key="status" :value="status">{{ status }}</option>
          </select>
        </div>

        <!-- Select team -->
        <div class="col-span-6 sm:col-span-3">
          <label for="team" class="modal-label">For Team</label>
          <select v-model="order.team" class="team-select" required>
            <option v-for="team in teamOptions" :key="team.id" :value="team.name">{{ team.name }}</option>
          </select>
        </div>

        <!-- Select team -->
        <div class="col-span-6 sm:col-span-3">
          <label for="team" class="modal-label">Project</label>
          <select v-model="order.team" class="team-select" required>
            <option v-for="team in teamOptions" :key="team.id" :value="team.name">{{ team.name }}</option>
          </select>
        </div>


        <!-- Order date-->
        <div class="col-span-6 sm:col-span-6">
          <label for="date" class="modal-label">Order date</label>
          <input
              type="date"
              v-model="order.orderDate"
              class="modal-input shadow-sm"
              placeholder="Order date"
              required>
        </div>

        <!-- Estimated delivery date -->
        <div class="col-span-6 sm:col-span-6">
          <label for="date" class="modal-label">Estimated delivery date</label>
          <input
              type="date"
              v-model="order.estimatedDeliveryDate"
              class="modal-input shadow-sm"
              placeholder="Order date"
              required>
        </div>

        <!-- Select products -->
        <div class="col-span-6 sm:col-span-6">
          <label for="team" class="modal-label">Add Product</label>
          <div class="w-full flex">
            <select v-model="selectedProduct" class="product-select" required>
              <option v-for="product in productOptions" :key="product.id" :value="product">{{ product.name }}</option>
            </select>

            <SolarButton class=" ml-2 add-product-btn" button-text="Add" @click="addProductsToOrder"></SolarButton>
          </div>
        </div>
      </div>

      <!-- Display selected products -->
      <div class="order-list" v-if="selectedProducts.length > 0">
        <h2>Ordered Products:</h2>
        <SolarTable :columns="['Name', 'Price', 'Quantity', 'Action']">
          <tr class="table-row" v-for="product in selectedProducts" :key="product.id">
            <td class="px-6 py-4 font-semibold text-base">{{ product.name }}</td>
            <td class="px-6 py-4">€ {{ product.price }}</td>
            <td class="px-6 py-4">
              <input
                  type="number"
                  id="number-input"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg block w-full p-2.5"
                  placeholder="0"
                  required>
            </td>
            <td class="px-6 py-4">
              <div @click="removeProduct" class="remove-order-btn">Remove product</div>
            </td>
          </tr>
        </SolarTable>
      </div>


      <!-- Modal footer -->
      <template v-slot:footer>
        <button @click="onClose" class="cancel-button">Cancel</button>
        <button type="submit" :disabled="isAnyFieldEmpty" class="ml-auto submit-button">Create Order</button>
      </template>
    </SolarModal>
  </form>
</template>


<style scoped>


.remove-order-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.order-list {
  padding-top: 2rem;
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem;
}

.modal-label {
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
  height: 42px;
  padding: 0.625rem;
}

.product-select {
  display: flex;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  line-height: 1.5rem;
  color: rgb(17 24 39);
  border-width: 1px;
  border-color: rgb(209 213 219);
  border-radius: 0.5rem;
  background-color: rgb(249 250 251);
  height: 42px;
  cursor: pointer;
}

.team-select,
.status-select {
  display: block;
  width: 100%;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  line-height: 1.5rem;
  color: rgb(17 24 39);
  border-width: 1px;
  border-color: rgb(209 213 219);
  border-radius: 0.5rem;
  background-color: rgb(249 250 251);
  height: 42px;
  cursor: pointer;
}

.submit-button:disabled, .submit-button:disabled:hover {
  opacity: 40%;
  cursor: not-allowed; /* Change the cursor to not-allowed */
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

.cancel-button:hover {
  background-color: rgb(206 212 218);
}

@media (max-width: 768px) {
  .modal-grid {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
}
</style>

