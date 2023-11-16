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
      required:true,
    },
  },
  methods: {
    emitToggle() {
      this.$emit("toggle", this.order);
    },
    emitView(){
      this.$emit("view", this.order.products);
    },
    emitEdit() {
      this.$emit("edit", this.order);
    },
    emitDelete() {
      this.$emit("delete", this.order);
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
    <!--User Item-->
<!--    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">-->
<!--      <div class="pl-3">-->
<!--        <div class="text-base font-semibold">{{ order.orderNumber}}</div>-->
<!--        <div class="font-normal text-gray-500">{{ order.orderDate }}</div>-->
<!--      </div>-->
<!--    </th>-->
    <td class="px-6 py-4 font-semibold text-base">{{ order.orderNumber }}</td>
    <td class="px-6 py-4">{{ order.orderDate}}</td>
    <td class="px-6 py-4">{{order.estimatedDeliveryDate}}</td>
    <td class="px-6 py-4">Warehouse Solar 4</td>
    <!-- Todo for i product in products -->
    <td class="px-6 py-4">{{order.totalPrice}}</td>
    <td class="px-6 py-4">{{order.status}}</td>
    <!-- Edit User -->
    <td class="px-6 py-4 row">
      <div @click="emitView" class="view-btn">View products</div>
      <div @click="emitComplete" class="complete-btn">Complete</div>
      <div @click="emitDelete" class="delete-btn">Report</div>
    </td>
  </tr>
</template>

<style scoped>
.complete-btn {
  font-weight: 500;
  color: #C7D02C;
  cursor: pointer;
}

.view-btn {
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.delete-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.delete-btn:hover {
  text-decoration-line: underline;
}
.view-btn:hover {
  text-decoration-line: underline;
}

.complete-btn:hover {
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
