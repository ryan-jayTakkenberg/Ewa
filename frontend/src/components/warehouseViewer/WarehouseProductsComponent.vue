<template>
  <div class="warehouseProductsContainer">
    <div class="title">All products in: {{this.selectedWarehouse.name}} </div>
    <div v-if="selectedWarehouse">
      <table>
        <thead>
        <tr>
          <th>Product Name</th>
          <th>Amount</th>
          <th>Price</th>
          <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="product in selectedWarehouse.products" :key="product.id">
          <td>{{ product.product.name }}</td>
          <td>{{ product.amount }}</td>
          <td>{{ product.product.price }}</td>
          <td>{{ product.product.description }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "WarehouseProductsComponent",
  props: {
    selectedWarehouse: {
      type: Object,
      default: null,
    },
  },
};
</script>

<style scoped>

.warehouseProductsContainer {
  padding: 25px;
  border-bottom: 1px solid var(--col-white-200);
  background: var(--col-white-150);
}

.title, li {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th, td {
  border: 1px solid var(--col-white-500);
  padding: 8px;
  text-align: left;
}

th {
  font-size: 15px;
  font-weight: 600;
  background-color: var(--col-white-200);
}

td {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-medium);
}

</style>