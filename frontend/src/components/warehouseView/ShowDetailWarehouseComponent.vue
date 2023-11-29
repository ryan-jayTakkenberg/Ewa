<template>
  <div v-if="isOpen" class="detail-warehouse">
    <div v-for="(product, index) in products" :key="index">
      <!-- Hier kun je de inhoud van je detailcomponent plaatsen -->
      <h2>Details voor {{ product.name }}</h2>
      <p>product ID: {{ product.id }}</p>
      <p>Price: {{ product.price }}</p>
      <p>Description: {{ product.description }} </p>
      <p> </p>
    </div>
  </div>
</template>
<script>


import Product from "@/models/product";


export default {
  data(){
    return{
      products: [...Product.products]
    }
  },
  props: {
    product: Product,
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
