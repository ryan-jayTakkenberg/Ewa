<script>
import Order from "@/models/order";
import Warehouse from "../../models/warehouse";
import {isAdmin} from "@/data";

export default {
  name: "OrderRowComponent",
  computed: {
    Order() {
      return Order
    },
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
      <div class="flex items-center">
        <input
            type="checkbox"
            v-model="checked" @change="emitToggle"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded">
      </div>
    </td>
    <td class="px-6 py-4 font-semibold text-base">{{ order.id }}</td>
    <td class="px-6 py-4">{{ order.orderedFrom }}</td>
    <td class="px-6 py-4">{{ order.orderDate }}</td>
    <td class="px-6 py-4">{{ order.estimatedDeliveryDate }}</td>
    <td class="px-6 py-4">{{ order.teamId }}</td>
    <td class="px-6 py-4">{{ order.productId }}</td>
    <td class="px-6 py-4">{{ order.quantity }}</td>
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
</template>

<style scoped>

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
}

.cancel-btn:hover,
.report-btn:hover,
.complete-btn:hover,
.edit-btn:hover{
  text-decoration-line: underline;
}

.edit-btn{
  font-weight: 500;
  color: blue;
  cursor: pointer;
}

.report-btn {
  font-weight: 500;
  color: #333333;
  cursor: pointer;
}

.cancel-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.report-btn {
  font-weight: 500;
  color: #333333;
  cursor: pointer;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249 250 251);
}
</style>
