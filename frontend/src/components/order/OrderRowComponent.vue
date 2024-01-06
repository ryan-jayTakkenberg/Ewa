<script>
import Order from "@/models/order";
import Warehouse from "../../models/warehouse";
import {isAdmin} from "@/data";
import SolarTable from "@/components/general/SolarTable.vue";

export default {
  name: "OrderRowComponent",
  components: {SolarTable},
  emits: ['toggle', 'edit', 'cancel', 'confirm', 'report'],
  props: {
    order: {
      type: Order,
      required: true,
    },
  },
  data() {
    return {
      checked: false,
      productsVisible: false,
    };
  },
  computed: {
    Order() {
      return Order
    },
    Warehouse() {
      return Warehouse
    }
  },
  methods: {
    isAdmin,
    emitToggle() {
      this.$emit("toggle", this.order);
    },
    emitEdit() {
      this.$emit("edit", this.order);
    },
    emitCancel() {
      this.$emit("cancel", this.order);
    },
    emitConfirm() {
      this.$emit("confirm", this.order);
    },
    emitReport() {
      this.$emit("report", this.order);
    },
    toggleProducts() {
      this.productsVisible = !this.productsVisible;
    },
    getStatusClass() {
      switch (this.order.status) {
        case Order.Status.DELIVERED:
          return "delivered";
        case Order.Status.PENDING:
          return "pending";
        case Order.Status.CANCELED:
          return "canceled";
        default:
          return "";
      }
    },
  }
}
</script>

<template>
  <tr class="table-row">
    <!-- Check box  -->
    <td class="w-4 p-4">
      <div class="items-center">
        <input
            type="checkbox"
            v-model="checked" @change="emitToggle"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded">
      </div>
    </td>
    <!-- Order number and name -->
    <td class="px-6 py-4">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ order.id }}</div>
        <div class="font-normal text-gray-500">{{ order.name }}</div>
      </div>
    </td>
    <!--          <div class="pl-3">-->
    <!--            <div class="text-base font-semibold">{{ order.id }}</div>-->
    <!--            <div class="font-normal text-gray-500">{{ order.name }}</div>-->
    <!--          </div>-->
    <!-- Order from  -->
    <td class="px-6 py-4">{{ order.orderedFrom }}</td>
    <!-- Order date  -->
    <td class="px-6 py-4 whitespace-nowrap">{{ order.orderDate }}</td>
    <!-- Order estimated delivery date  -->
    <td class="px-6 py-4 whitespace-nowrap">{{ order.estimatedDeliveryDate }}</td>
    <!-- Order team and warehouse  -->
    <td class="px-6 py-4">
      <div class="font-semibold">{{ order.team.name }}</div>
      {{ order && order.team && order.team.warehouse ? order.team.warehouse.name : 'N/A' }}
    </td>

    <!-- Order products  -->
    <td class="px-6 py-4">
      <div v-if="!productsVisible" class="toggle-product-btn" @click="toggleProducts">View Products</div>
      <div v-if="productsVisible" class="toggle-product-btn" @click="toggleProducts">Hide Products</div>
    </td>

    <!-- Order status  -->
    <td class="px-6 py-4">
      <div :class="['status', getStatusClass(),]">{{ order.status }}</div>
    </td>

    <td class="px-4 py-4">
      <div v-if="order.status === Order.Status.PENDING" @click="emitConfirm" class="complete-btn">Confirm order</div>
      <div v-if="order.status === Order.Status.PENDING" @click="emitCancel" class="cancel-btn">Cancel order</div>
      <div v-if="!isAdmin()" @click="emitReport" class="report-btn">Report order</div>
      <div v-if="isAdmin()" @click="emitReport" class="edit-btn">Edit order</div>
    </td>
  </tr>

  <!-- Order status  -->
  <SolarTable v-if="productsVisible" :columns="['Product', 'Price', 'Quantity', 'Total Price']"
              class="table-row whitespace-nowrap">
    <tr class="table-row" v-for="(orderedProduct, index) in order.orderedProducts" :key="index">
      <td class="px-6 py-4 whitespace-nowrap">{{ orderedProduct.product.name}}</td>
      <td class="px-6 py-4 whitespace-nowrap">€ {{ orderedProduct.product.price }}</td>
      <td class="px-6 py-4 whitespace-nowrap">{{ orderedProduct.amount }} x</td>
      <td class="px-6 py-4 whitespace-nowrap">
        € {{(parseFloat(orderedProduct.product.price) * orderedProduct.amount).toFixed(2)}}

      </td>
    </tr>
    <tr>
      <!-- todo Total price -->
      <td class="px-6 py-4 font-semibold">Total price</td>
      <td class="px-6 py-4"></td>
      <td class="px-6 py-4"></td>
      <td class="px-6 py-4 font-semibold underline">€ {{ (order.totalPrice).toFixed(2) }}</td>
    </tr>
  </SolarTable>

</template>


<style scoped>

.row-outline {
  border: 1px solid #ccc;
  padding: 10px;
  background-color: #fff;
}

.status {
  display: inline-flex;
  align-items: center;
  background-color: orange;
  color: white;
  font-size: 0.75rem;
  line-height: 1rem;
  font-weight: 500;
  padding: 0.125rem 0.625rem;;
  border-radius: 9999px;
}

.delivered {
  background-color: #C7D02C;
  color: white;
}

.pending {
  background-color: darkgray;
  color: white;
}

.canceled {
  background-color: red;
  color: white;
}

.complete-btn {
  font-weight: 500;
  color: #C7D02C;
  cursor: pointer;
  white-space: nowrap;
}

.cancel-btn:hover,
.report-btn:hover,
.complete-btn:hover,
.edit-btn:hover,
.toggle-product-btn:hover {
  text-decoration-line: underline;
  cursor: pointer;
}

.edit-btn {
  font-weight: 500;
  color: blue;
  white-space: nowrap;

}

.report-btn {
  font-weight: 500;
  color: #333333;
  white-space: nowrap;
}

.cancel-btn {
  font-weight: 500;
  color: red;
  white-space: nowrap;
}

.report-btn {
  font-weight: 500;
  color: #333333;
  white-space: nowrap;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249 250 251);
}
</style>
