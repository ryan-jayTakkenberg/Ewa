<!-- ShowDetailWarehouseComponent.vue -->
<template>
  <div v-if="isOpen" class="detail-warehouse">
    <!-- Hier kun je de inhoud van je detailcomponent plaatsen -->
    <h2>Details voor {{ product.name}}</h2>
    <p>Warehouse ID: {{ product.id }}</p>
    <p>Adres: {{ product.price }}</p>
    <p>Postcode: {{ product.description }}</p>
  </div>
</template>

<script>


export default {
  props: {
    product: Object,
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
