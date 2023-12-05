<script>
import Order from "@/models/order";
import Warehouse from "../../models/warehouse";
import {isAdmin} from "@/data";
import SolarTable from "@/components/general/SolarTable.vue";

export default {
  name: "OrderRowComponent",
  components: {SolarTable},
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
      productsVisible: false,
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
    displayProducts() {
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
    <!-- Order number  -->
    <td class="px-6 py-4 font-semibold text-base">{{ order.id }}</td>
    <!-- Order from  -->
    <td class="px-6 py-4">{{ order.orderedFrom }}</td>
    <!-- Order date  -->
    <td class="px-6 py-4">{{ order.orderDate }}</td>
    <!-- Order estimated delivery date  -->
    <td class="px-6 py-4">{{ order.estimatedDeliveryDate }}</td>
    <!-- Order team and warehouse  -->
    <td class="px-6 py-4">
      <div class="font-semibold">{{ order.team.name }}</div>
      {{ order.team.warehouse }}
    </td>

    <!-- Order team all projects names and install dates -->
    <td class="px-6 py-4">
      <div v-if="order.team.projects && order.team.projects.length > 0">
        <div v-for="project in order.team.projects" :key="project.id">
          <div class="font-semibold">{{ project.projectName }}</div>
          Installation Date:<br>{{ project.installDate }}
        </div>
      </div>
      <div v-else>No projects available.</div>
    </td>

    <td class="px-6 py-4">
      <div v-if="!productsVisible" class="view-productInfos" @click="displayProducts">View Products</div>
      <div v-if="productsVisible" class="view-productInfos" @click="displayProducts">Hide Products</div>
    </td>

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

  <SolarTable v-if="productsVisible" :columns="['Name', 'Price', 'Quantity']">
    <tr class="table-row" v-for="productInfo in order.productInfos" :key="productInfo.id">
      <td class="px-6 py-4 font-semibold text-base">{{ productInfo.name }}</td>
      <td class="px-6 py-4">€ {{ productInfo.price }}</td>
      <td class="px-6 py-4">{{ productInfo.quantity }}</td>
    </tr>
  </SolarTable>

</template>


<style scoped>
.productInfo-row {
  width: 100vw;
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
}

.cancel-btn:hover,
.report-btn:hover,
.complete-btn:hover,
.edit-btn:hover,
.view-productInfos:hover {
  text-decoration-line: underline;
  cursor: pointer;
}

.edit-btn {
  font-weight: 500;
  color: blue;

}

.report-btn {
  font-weight: 500;
  color: #333333;
}

.cancel-btn {
  font-weight: 500;
  color: red;
}

.report-btn {
  font-weight: 500;
  color: #333333;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249 250 251);
}
</style>
