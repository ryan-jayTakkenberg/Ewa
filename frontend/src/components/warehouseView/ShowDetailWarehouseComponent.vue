<!-- ShowDetailWarehouseComponent.vue -->
<template>
  <div v-if="isOpen" class="detail-warehouse">
    <!-- Hier kun je de inhoud van je detailcomponent plaatsen -->
    <h2>Details voor {{ warehouse.name }}</h2>
    <p>Warehouse ID: {{ warehouse.id }}</p>
    <p>Adres: {{ warehouse.address }}</p>
    <p>Postcode: {{ warehouse.postalCode }}</p>
    <p>Stad: {{ warehouse.city }}</p>
  </div>
</template>

<script>
import Warehouse from "@/models/warehouse";

export default {
  props: {
    warehouse: Warehouse,
    isOpen: Boolean,
  },
  methods: {
    closeDetail() {
      this.$emit('close-detail');
    },
  },
};
</script>

<style scoped>
/* Voeg hier je CSS-stijlen toe voor de detailcomponent */
.detail-warehouse {
  border: 1px solid #ccc;
  padding: 10px;
  background-color: #fff;
}
</style>
