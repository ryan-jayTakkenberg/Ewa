<script>
import Order from "@/models/order";
import Team from "@/models/team";
import Product from "@/models/product";

import SolarModal from "@/components/general/SolarModal.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import Product_Order from "@/models/product_order";

export default {
  name: "EditOrderModal",
  emits: ['edit'],
  components: {SolarTable, SolarButton, SolarModal},
  data() {
    return {
      OrderStatusOptions: Order.Status,
      clonedOrder: null,
      teamOptions: Team.teams,
      productOptions: [...Product.products],
      productOrders: [], // Used for Product_Orders creation containing amount, productId and orderId
      orderedProducts:[], // Used for keeping track of products for creating product orders and setting amount
      selectedProduct: null,
    }
  },
  props: {
    order: {
      type: Order,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  created() {
    this.clonedOrder = { ...this.order };
    this.orderedProducts = this.clonedOrder.orderedProducts.map((orderedProduct) => ({
      product: {...orderedProduct.product},
      amount: orderedProduct.amount,
    }));
    // Check if product already in productOptions and remove them from the list
    this.orderedProducts.forEach((orderedProduct) => {
      const index = this.productOptions.findIndex(product => product.id === orderedProduct.product.id);
      if (index !== -1) {
        this.productOptions.splice(index, 1);
      }
    });
  },
  methods: {
    editOrder() {
      this.productOrders.forEach((existingProductOrder) => {
        const matchingOrderedProduct = this.orderedProducts.find((orderedProduct) => {
          return (
              existingProductOrder.product.id === orderedProduct.product.id &&
              existingProductOrder.amount !== orderedProduct.amount
          );
        });

        if (matchingOrderedProduct) {
          // Update the existing Product_Order with the new amount
          existingProductOrder.amount = matchingOrderedProduct.amount;
        }
      });

      // Add new Product_Order instances for products not found in existing productOrders
      const newProductOrders = this.orderedProducts
          .filter((orderedProduct) => {
            return !this.productOrders.some((existingProductOrder) => {
              return existingProductOrder.product.id === orderedProduct.product.id;
            });
          })
          .map((orderedProduct) => {
            return new Product_Order(orderedProduct.amount, orderedProduct.product.id, null);
          });

      this.productOrders.push(...newProductOrders);
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
    removeProductFromOrder(index) {
      // add the removed product back to productOptions
      this.productOptions.push(this.orderedProducts[index].product);
      this.orderedProducts.splice(index, 1);
    },

  }
}
</script>

<template>
  <form @submit.prevent="editOrder" @keydown.enter.prevent="">
    <SolarModal title="Update Order" @close-modal="onClose" class="modal">
      <div class="modal-grid">

        <!-- Order name-->
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label">Order name</label>
          <input
              type="text"
              v-model="clonedOrder.name"
              class="modal-input shadow-sm"
              placeholder="Order name"
              required>
        </div>

        <!-- Ordered from -->
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label">Ordered From</label>
          <input
              type="text"
              v-model="clonedOrder.orderedFrom"
              class="modal-input shadow-sm"
              placeholder="Ordered From"
              required>
        </div>

        <!-- Select team -->
        <div class="col-span-6 sm:col-span-3">
          <label for="team" class="modal-label">For Team</label>
          <select id="team" v-model="clonedOrder.team" class="team-select" required>
            <option v-for="team in teamOptions" :key="team.id" :value="team">{{ team.name }}</option>
          </select>
        </div>

        <!-- Order Status-->
        <div class="col-span-6 sm:col-span-3">
          <label for="order-status" class="modal-label">Order status</label>
          <select id="order-status" v-model="clonedOrder.status" class="status-select" required>
            <option v-for="status in OrderStatusOptions" :key="status" :value="status">{{ status }}</option>
          </select>
        </div>

        <!-- Order date-->
        <div class="col-span-6 sm:col-span-6">
          <label for="order-date" class="modal-label">Order date</label>
          <input
              id="order-date"
              type="date"
              v-model="clonedOrder.orderDate"
              class="modal-input shadow-sm"
              placeholder="Order date"
              required>
        </div>

        <!-- Estimated delivery date -->
        <div class="col-span-6 sm:col-span-6">
          <label for="delivery-date" class="modal-label">Estimated delivery date</label>
          <input
              id="delivery-date"
              type="date"
              v-model="clonedOrder.estimatedDeliveryDate"
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
          <tr class="table-row" v-for="(orderedProduct, index) in this.orderedProducts" :key="index">
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
              <div @click="removeProductFromOrder(index)" class="remove-order-btn">Remove product</div>
            </td>
          </tr>
        </SolarTable>
      </div>

      <!-- Modal footer -->
      <template v-slot:footer>
        <button @click="onClose" class="cancel-button">Cancel</button>
        <button type="submit" class="ml-auto submit-button">Update Order</button>
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
  height: 45px;
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
  height: 45px;
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
