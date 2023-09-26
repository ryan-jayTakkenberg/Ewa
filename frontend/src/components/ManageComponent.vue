<template>
  <nav class="navbar navbar-light bg-light">
    <div class="empty"></div>
    <div class="container rounded">
      <div class="logoContainer">
        <span class="material-symbols-outlined">inventory_2</span>
      </div>
      <div class="textContainer rounded-bottom" style="background-color:aqua;">
        <span class="fw-semibold">Product</span>
      </div>
    </div>
    <div class="container rounded">
      <div class="logoContainer">
        <span class="material-symbols-outlined">warehouse</span>
      </div>
      <div class="textContainer rounded-bottom" style="background-color:yellow;">
        <span class="fw-semibold">Projects</span>
      </div>
    </div>
    <div class="container rounded">
      <div class="logoContainer">
        <span class="material-symbols-outlined">tactic</span>
      </div>
      <div class="textContainer rounded-bottom" style="background-color:orangered;">
        <span class="fw-semibold">Projects</span>
      </div>
    </div>
    <div class="container rounded">
      <div class="logoContainer">
        <span class="material-symbols-outlined">folder_shared</span>
      </div>
      <div class="textContainer rounded-bottom" style="background-color:limegreen;">
        <span class="fw-semibold">Teams</span>
      </div>
    </div>
    <div class="container rounded">
      <div class="logoContainer">
        <span class="material-symbols-outlined">person</span>
      </div>
      <div class="textContainer rounded-bottom" style="background-color:orange;">
        <span class="fw-semibold">Accounts</span>
      </div>
    </div>
    <div class="empty"></div>
  </nav>
</template>

<script>
export default {
  name: 'ManageComponent'
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
</style>
