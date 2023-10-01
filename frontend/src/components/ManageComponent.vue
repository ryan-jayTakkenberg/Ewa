<template>
  <nav class="navbar navbar-light bg-light">
    <div class="empty"></div>
    <div class="container rounded-3" @click="goToProductDetail">
      <div class="logoContainer">
        <span class="material-symbols-outlined">inventory_2</span>
      </div>
      <div class="textContainer rounded-bottom-3" style="background-color:aqua;">
        <span class="fw-semibold">Product</span>
      </div>
    </div>
    <div class="container rounded-3" @click="goToWarehouseDetail">
      <div class="logoContainer">
        <span class="material-symbols-outlined">warehouse</span>
      </div>
      <div class="textContainer rounded-bottom-3" style="background-color:yellow;">
        <span class="fw-semibold">Warehouses</span>
      </div>
    </div>
    <div class="container rounded-3" @click="goToProjectDetail">
      <div class="logoContainer">
        <span class="material-symbols-outlined">tactic</span>
      </div>
      <div class="textContainer rounded-bottom-3" style="background-color:orangered;">
        <span class="fw-semibold">Projects</span>
      </div>
    </div>
    <div class="container rounded-3">
      <div class="logoContainer">
        <span class="material-symbols-outlined">folder_shared</span>
      </div>
      <div class="textContainer rounded-bottom-3" style="background-color:limegreen;">
        <span class="fw-semibold">Teams</span>
      </div>
    </div>
    <div class="container rounded-3">
      <div class="logoContainer">
        <span class="material-symbols-outlined">person</span>
      </div>
      <div class="textContainer rounded-bottom-3" style="background-color:orange;">
        <span class="fw-semibold">Accounts</span>
      </div>
    </div>
    <div class="empty"></div>
  </nav>
<div>
  <router-view>
  </router-view>
</div>
</template>

<script>
export default {
  name: 'ManageComponent',
  methods: {
    goToProductDetail(){
      this.$router.push('/manage/product')
    },
    goToProjectDetail(){
      this.$router.push('/manage/project')
    },
    goToWarehouseDetail(){
      this.$router.push('/manage/warehouse')
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.empty {
  width: 0;
}

.container {
  display: flex;
  flex-direction: column;
  height: 100px;
  width: 150px;
  border: 2px solid black;
  margin: 0;
  padding: 0;
  cursor: pointer;
}

.logoContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 70%;
}

.textContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 30%;
  width: 100%;
  border-top: 2px solid black;
}

.material-symbols-outlined {
  font-size: 2em;
  font-variation-settings:
      'FILL' 0,
      'wght' 300,
      'GRAD' 0,
      'opsz' 24
}
</style>
