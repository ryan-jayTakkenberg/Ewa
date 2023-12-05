<template>
  <div v-if="isOpen" class="detail-warehouse">
    <div v-for="(productInfo, index) in productInfos" :key="index">
      <!-- Hier kun je de inhoud van je detailcomponent plaatsen -->
      <h2>Details voor {{ productInfo.name }}</h2>
      <p>productInfo ID: {{ productInfo.id }}</p>
      <p>Price: {{ productInfo.price }}</p>
      <p>Description: {{ productInfo.description }} </p>
      <p> </p>
    </div>
  </div>
</template>
<script>


import Product from "@/models/productInfo";


export default {
  data(){
    return{
      productInfos: [...Product.productInfos]
    }
  },
  props: {
    productInfo: Product,
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
