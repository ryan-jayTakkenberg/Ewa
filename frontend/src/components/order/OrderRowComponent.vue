<script>
import Order from "@/models/order";
import Warehouse from "../../models/warehouse";

export default {
  name: "OrderRowComponent",
  computed: {
    Warehouse() {
      return Warehouse
    }
  },
  emits: ["toggle", "edit", "delete"],
  data() {
    return {
      checked: false,
    };
  },
  props: {
    order: {
      type: Order,
      required: true,
    },
  },
  methods: {
    emitToggle() {
      this.$emit("toggle", this.order);
    },
    emitViewProducts() {
      this.$emit("view-products", this.order.products);
    },
    emitEdit() {
      this.$emit("edit", this.order);
    },
    emitCancel() {
      this.$emit("cancel", this.order);
    },
    emitComplete() {
      this.$emit("complete", this.order);
    },
  }
}
</script>

<template>
  <tr class="table-row">
    <!-- Check box  -->
    <td class="w-4 p-4">
      <div class="flex items-center">
        <input
            type="checkbox"
            v-model="checked" @change="emitToggle"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded">
      </div>
    </td>
    <td class="px-6 py-4 font-semibold text-base">{{ order.orderNumber }}</td>
    <td class="px-6 py-4">{{ order.orderDate }}</td>
    <td class="px-6 py-4">{{ order.estimatedDeliveryDate }}</td>
    <td class="px-6 py-4">Warehouse Solar 4</td>
    <td class="px-6 py-4">{{ order.totalPrice }}</td>
    <td class="px-6 py-4">{{ order.status }}</td>
    <td class=" px-6 py-4 view-btn" @click="emitViewProducts">View products({{ order.products.length }})</td>
    <td class="px-6 py-4 row">
      <!-- Complete order-->
      <div @click="emitComplete" class="complete-btn">Complete order</div>
      <!-- Cancel order -->
      <div @click="emitCancel" class="cancel-btn">Cancel order</div>
    </td>
  </tr>
</template>

<style scoped>
.view-btn {
  font-weight: 500;
  color: #C7D02C;
  cursor: pointer;
}

.view-btn:hover {
  text-decoration-line: underline;
}

.complete-btn {
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.complete-btn:hover {
  text-decoration-line: underline;
}

.cancel-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.cancel-btn:hover {
  text-decoration-line: underline;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249 250 251);
}
</style>
