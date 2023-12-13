<script>
import Order from "@/models/order";
import Team from "@/models/team";
import Product from "@/models/product";

import SolarModal from "@/components/general/SolarModal.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarButton from "@/components/general/SolarButton.vue";

export default {
  name: "CreateOrderModal",
  emits: ['create'],
  components: {SolarTable, SolarButton, SolarModal},
  data() {
    return {
      order: {
        orderNumber: '',
        orderDate: '',
        estimatedDeliveryDate: '',
        team: '',
        products: [],
        status: '',
      },
      OrderStatusOptions: Order.Status,
      teamOptions: Team.teams,
      selectedProduct: null,
      productOptions: Product.products,
      selectedProducts: [],

    }
  },
  props: {
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    createOrder() {
      let orderClass = new Order();
      orderClass.injectAttributes(this.order);
      this.$emit('create', orderClass);
    },
    addProductsToOrder() {
      // Add selected product to the selected products array
      this.selectedProducts.unshift(this.selectedProduct)
    },
    removeProduct(index) {
      this.selectedProducts.splice(index, 1);
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

        <!-- Select team -->
        <div class="col-span-6 sm:col-span-3">
          <label for="team" class="modal-label">For Team</label>
          <select id="team" v-model="order.team" class="team-select" required>
            <option v-for="team in teamOptions" :key="team.id" :value="team">{{ team.name }}</option>
          </select>
        </div>

        <!-- Order Status-->
        <div class="col-span-6 sm:col-span-3">
          <label for="order-status" class="modal-label">Order status</label>
          <select id="order-status" v-model="order.status" class="status-select" required>
            <option v-for="status in OrderStatusOptions" :key="status" :value="status">{{ status }}</option>
          </select>
        </div>

        <!-- Order date-->
        <div class="col-span-6 sm:col-span-6">
          <label for="date" class="modal-label">Order date</label>
          <input
              id="date"
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
              id="date"
              type="date"
              v-model="order.estimatedDeliveryDate"
              class="modal-input shadow-sm"
              placeholder="Order date"
              required>
        </div>

        <!-- Select products -->
        <div class="col-span-6 sm:col-span-6">
          <label for="products" class="modal-label">Add Product</label>
          <div class="w-full flex">
            <select id="products" v-model="selectedProduct" class="product-select" required>
              <option v-for="product in productOptions" :key="product.id" :value="product">{{ product.name }}</option>
            </select>
            <SolarButton class=" ml-2 add-productInfo-btn" button-text="Add" @click="addProductsToOrder"></SolarButton>
          </div>
        </div>
      </div>

      <!-- Display selected productInfos -->
      <div class="order-list" v-if="selectedProducts.length > 0">
        <h2>Ordered Products:</h2>
        <SolarTable :columns="['Name', 'Price', 'Quantity', 'Action']">
          <tr class="table-row" v-for="productInfo in selectedProducts" :key="productInfo.id">
            <td class="px-6 py-4 font-semibold text-base">{{ productInfo.name }}</td>
            <td class="px-6 py-4">€ {{ productInfo.price }}</td>
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
        <button type="submit" class="ml-auto submit-button">Create Order</button>
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
  height: 45px;
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

