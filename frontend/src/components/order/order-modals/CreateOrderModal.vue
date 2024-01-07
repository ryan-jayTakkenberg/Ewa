<script>
import Order from "@/models/order";
import Team from "@/models/team";
import Product from "@/models/product";

import SolarModal from "@/components/general/SolarModal.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import Product_Order from "@/models/product_order";

export default {
  name: "CreateOrderModal",
  emits: ['create'],
  components: {SolarTable, SolarButton, SolarModal},
  data() {
    return {
      order: {
        name: '',
        orderedFrom:'',
        orderDate: '',
        estimatedDeliveryDate: '',
        team: '',
        status: '',
      },
      OrderStatusOptions: Order.Status,
      teamOptions: Team.teams,
      productOptions: [...Product.products],
      productOrders: [], // Used for Product_Orders creation containing amount, productId and orderId
      orderedProducts:[], // Used for keeping track of products for creating product orders and setting amount
      selectedProduct: null,
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

      const productOrders = this.orderedProducts.map((orderedProduct) => {
        return new Product_Order(orderedProduct.amount, orderedProduct.product.id, null);
      });
      this.productOrders.push(...productOrders); // Merge arrays

      let newOrder = Order.createNewOrder();
      newOrder.injectAttributes(this.order);
      const json = {
        name: newOrder.name,
        orderedFrom: newOrder.orderedFrom,
        status: newOrder.status,
        orderDate: newOrder.orderDate,
        estimatedDeliveryDate: newOrder.estimatedDeliveryDate,
        teamId: newOrder.team.id,
      };
      this.$emit('create', json, this.productOrders);
      console.log(newOrder);
    },
    addProductToOrder() {
      if (this.selectedProduct) {
        // Create a new Product_Order instance and add it to the orderedProducts array
        this.orderedProducts.unshift({ product: this.selectedProduct, amount: null });

        // Remove the selected product from the product options
        const selectedIndex = this.productOptions.findIndex((product) => product.id === this.selectedProduct.id);
        if (selectedIndex !== -1) this.productOptions.splice(selectedIndex, 1);

        // Reset selectedProduct and selectedProductAmount after adding to orderedProducts
        this.selectedProduct = null;
      }
    },
    removeProduct(index) {
      this.productOrders.splice(index, 1);
    },

  }
}
</script>

<template>
  <form @submit.prevent="createOrder" @keydown.enter.prevent="">
    <SolarModal title="Create Order" @close-modal="onClose" class="modal">
      <div class="modal-grid">

        <!-- Order name-->
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label">Order name</label>
          <input
              type="text"
              v-model="order.name"
              class="modal-input shadow-sm"
              placeholder="Order name"
              required>
        </div>

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
            <select id="products" v-model="selectedProduct" class="product-select">
              <option v-for="product in productOptions" :key="product.id" :value="product">{{ product.name }}</option>
            </select>
            <SolarButton class="ml-2 add-productInfo-btn" button-text="Add" @click="addProductToOrder"
                         :disabled="!selectedProduct"
            ></SolarButton>
          </div>
        </div>
      </div>

      <!-- Display ordered products -->
      <div class="order-list" v-if="orderedProducts.length > 0">
        <h2>Ordered Products:</h2>
        <SolarTable :columns="['Name', 'Price', 'Quantity', 'Action']">
          <tr class="table-row" v-for="(orderedProduct, index) in orderedProducts" :key="index">
            <td class="px-6 py-4 font-semibold text-base">{{ orderedProduct.product.name}}</td>
            <td class="px-6 py-4">€ {{ orderedProduct.product.price}}</td>
            <td class="px-6 py-4">
              <input v-model="orderedProduct.amount"
                     type="number"
                     id="number-input"
                     class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg block w-full p-2.5"
                     placeholder="0"
                     required>
            </td>
            <td class="px-6 py-4">
              <div @click="removeProduct(index)" class="remove-order-btn">Remove product</div>
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

